package it.jac.project_work.budget_manager.service;

import it.jac.project_work.budget_manager.dto.IncomeInDTO;
import it.jac.project_work.budget_manager.dto.IncomeOutDTO;
import it.jac.project_work.budget_manager.dto.PageInDTO;
import it.jac.project_work.budget_manager.dto.PaginationDTO;
import it.jac.project_work.budget_manager.entity.Account;
import it.jac.project_work.budget_manager.entity.Income;
import it.jac.project_work.budget_manager.repository.AccountRepository;
import it.jac.project_work.budget_manager.repository.IncomeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static it.jac.project_work.budget_manager.service.ExpenseService.calculateNextDate;

@Service
public class IncomeService {

    @Autowired
    public final IncomeRepository incomeRepository;
    @Autowired
    public final AccountRepository accountRepository;

    public IncomeService(IncomeRepository incomeRepository, AccountRepository accountRepository){
        this.accountRepository = accountRepository;
        this.incomeRepository = incomeRepository;
    }

    public List<IncomeOutDTO> getLastMonthIncomes(Account account, Integer limit){
        List<Income> list = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH,1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        java.util.Date utilDate = calendar.getTime();

        Date sqlDate = new Date(utilDate.getTime());
        System.out.println(sqlDate);
        list = this.incomeRepository.findByAccountAndDateGreaterThanEqualOrderByDateDesc(account,sqlDate);
        if(limit == null || limit > list.size()){
            return list.stream().map(income -> IncomeOutDTO.build(income)).collect(Collectors.toList());
        }
        else if(limit <= list.size()){
            return list.stream().map(income -> IncomeOutDTO.build(income)).collect(Collectors.toList()).subList(0,limit);

        }
        // TODO controller and testing
        return List.of();

    }


    public IncomeOutDTO saveIncome(IncomeInDTO dto, String userEmail){
        Optional<Account> account = this.accountRepository.findByEmail(userEmail);
        if(!account.isPresent()){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Account entity not found");
        }
        Income income = new Income();
        if(dto.getName() == null){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Missing required parameter: name");
        }
        income.setName(dto.getName());
        if(dto.getAmount() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Missing required parameter: amount");
        }
        if(dto.getImage() == null) {
            income.setImage("default.png");
        }else{
            income.setImage(dto.getImage());
        }
        income.setAmount(dto.getAmount());
        income.setDescription(dto.getDescription());
        income.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        if(dto.getFrequency() == null){
            income.setFrequency("S".charAt(0));
        }else{
            income.setFrequency(dto.getFrequency().charAt(0));
        }
        if(dto.getDate() == null){
            income.setDate(Date.valueOf(LocalDate.now()));
        }else{
            income.setDate(dto.getDate());
        }
        income.setAccount(account.get());

        return IncomeOutDTO.build(this.incomeRepository.save(income));
    }

    public IncomeOutDTO updateIncome(String userEmail, IncomeInDTO dto, Long id){
        Account account = this.accountRepository.findByEmail(userEmail)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Account entity not found"));
        if (id == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Missing required parameter: id");
        }
        Income income = this.incomeRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Income entity not found"));
        if(income.getAccount().getId() != account.getId()){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Cannot update other user's income");
        }

        if(dto.getName() != null){
            income.setName(dto.getName());
        }
        if(dto.getDescription() != null){
            income.setDescription(dto.getDescription());
        }
        if(dto.getAmount() != null && dto.getAmount() > 0){
            income.setAmount(dto.getAmount());
        }
        if(dto.getFrequency() != null){
            income.setFrequency(dto.getFrequency().charAt(0));
        }
        if(dto.getDate() != null ){
            income.setDate(dto.getDate());
        }
        if(dto.getImage() != null ){
            income.setImage(dto.getImage());
        }
        return IncomeOutDTO.build( this.incomeRepository.save(income));

    }


    public void deleteIncome(String userEmail, Long id){
        Account account = this.accountRepository.findByEmail(userEmail)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Account entity not found"));
        if(id == null){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Missing required parameter: id");
        }
        Income income = this.incomeRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Income entity not found"));
        if(income.getAccount().getId() != account.getId()){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Cannot delete other user's income");
        }
        this.incomeRepository.delete(income);
    }


    public PaginationDTO<IncomeOutDTO> getAllIncomes(String userEmail, PageInDTO dto){
        Account account = this.accountRepository.findByEmail(userEmail)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Account entity not foun"));

        Sort.Direction direction = dto.getOrderDirection().equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
        Sort sort = Sort.by(direction, dto.getOrderBy());

        PageRequest pageRequest = PageRequest.of(
                Math.max(dto.getPage(), 0),
                dto.getSize() > 0 ? dto.getSize() : 15,
                sort);
        Page<Income> incomesPage = this.incomeRepository.findAllWithPagination(account.getId(), pageRequest);
        PaginationDTO<IncomeOutDTO> result = new PaginationDTO<>();
        result.setRecords(incomesPage.getContent().stream().map(income-> IncomeOutDTO.build(income)).collect(Collectors.toList()));
        result.setSize(incomesPage.getSize());
        result.setOrderBy(incomesPage.getSort().toString());
        result.setPage(incomesPage.getNumber());
        result.setTotalRecords((int) incomesPage.getTotalElements());
        return result;
    }

    public void updateIncomeDates() {
        List<Income> incomes = incomeRepository.findAllByFrequencyNot('S');

        for (Income income : incomes) {
            LocalDate currentDate = LocalDate.now();
            LocalDate nextDate = calculateNextDate(income.getDate().toLocalDate(), income.getFrequency());

            if (currentDate.isEqual(income.getDate().toLocalDate()) || currentDate.isAfter(income.getDate().toLocalDate())) {
                if (!income.getDate().toLocalDate().isEqual(nextDate)) {
                    income.setDate(Date.valueOf(nextDate));
                }
            }

        }

        incomeRepository.saveAll(incomes);
    }

}
