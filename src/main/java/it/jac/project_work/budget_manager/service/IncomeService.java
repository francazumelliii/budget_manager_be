package it.jac.project_work.budget_manager.service;

import it.jac.project_work.budget_manager.dto.IncomeInDTO;
import it.jac.project_work.budget_manager.dto.IncomeOutDTO;
import it.jac.project_work.budget_manager.entity.Account;
import it.jac.project_work.budget_manager.entity.Income;
import it.jac.project_work.budget_manager.repository.AccountRepository;
import it.jac.project_work.budget_manager.repository.IncomeRepository;
import org.springframework.beans.factory.annotation.Autowired;
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


}
