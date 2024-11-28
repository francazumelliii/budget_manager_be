package it.jac.project_work.budget_manager.service;

import it.jac.project_work.budget_manager.dto.*;
import it.jac.project_work.budget_manager.entity.*;
import it.jac.project_work.budget_manager.repository.*;
import org.hibernate.resource.transaction.backend.jta.internal.JtaTransactionAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.data.cassandra.CassandraReactiveRepositoriesAutoConfiguration;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.server.ResponseStatusException;

import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ExpenseService {

    @Autowired
    public final ExpenseRepository expenseRepository;
    @Autowired
    public final AccountRepository accountRepository;
    @Autowired
    public final CategoryRepository categoryRepository;
    @Autowired
    public final ProjectRepository projectRepository;
    @Autowired
    public final ShareRepository shareRepository;
    @Autowired
    public final ExpenseSplitRepository expenseSplitRepository;

    public ExpenseService(ExpenseRepository expenseRepository, AccountRepository accountRepository, CategoryRepository categoryRepository, ProjectRepository projectRepository, ExpenseSplitRepository expenseSplitRepository, ShareRepository shareRepository){
        this.accountRepository = accountRepository;
        this.expenseRepository = expenseRepository;
        this.categoryRepository = categoryRepository;
        this.projectRepository = projectRepository;
        this.expenseSplitRepository = expenseSplitRepository;
        this.shareRepository = shareRepository;
    }

    public List<ExpenseOutDTO> getLastMonthExpenses(Account account, Integer limit){

        // get first day of current month
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH,1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        Timestamp firstDayOfMonth = new Timestamp(calendar.getTimeInMillis());
        System.out.println(firstDayOfMonth.toLocalDateTime());
        List<ExpenseOutDTO> list = new ArrayList<>();
        list = this.expenseRepository.findByAccountAndCreatedAtGreaterThanEqualOrderByDateDesc(account, firstDayOfMonth)
                .stream().map(exp -> ExpenseOutDTO.build(exp)).collect(Collectors.toList());

        if(limit == null || limit > list.size()){
            return list;
        }else if(limit <= list.size()){
            return list.subList(0,limit);
        }
        return List.of();
    }

    public ExpenseOutDTO saveExpense(ExpenseInDTO dto, String userEmail) {
        Expense expense = new Expense();
        Optional<Category> category;
        Optional<Project> project = null;
        Optional<Account> account = this.accountRepository.findByEmail(userEmail);

        if (!account.isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }
        expense.setAccount(account.get());

        if (dto.getCategoryId() == null) {
            category = this.categoryRepository.findById(10L); // 'EXTRA' category
        } else {
            category = this.categoryRepository.findById(dto.getCategoryId().longValue());
            if (!category.isPresent()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Category Entity not found");
            }
        }
        expense.setCategory(category.get());

        if (dto.getProjectId() == null) {
            expense.setProject(null);
        } else {
            project = this.projectRepository.findById(dto.getProjectId().longValue());
            if (!project.isPresent()) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Project Entity not found");
            }
            expense.setProject(project.get());
        }

        if (dto.getName() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Missing required parameter: name");
        }
        expense.setName(dto.getName());

        if (dto.getAmount() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Missing required parameter: amount");
        }
        expense.setAmount(dto.getAmount());

        if (dto.getFrequency() == null) {
            expense.setFrequency('S');
        } else {
            expense.setFrequency(dto.getFrequency().charAt(0));
        }

        expense.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        expense.setDescription(dto.getDescription());
        expense.setImage(dto.getImage());

        if (dto.getDate() == null) {
            expense.setDate(Date.valueOf(LocalDate.now()));
        } else {
            expense.setDate(dto.getDate());
        }

        Expense savedExpense = this.expenseRepository.save(expense);

        if (dto.getProjectId() != null) {
            List<Share> participants = this.shareRepository.findByProjectId(dto.getProjectId());

            if (participants.isEmpty()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No participants found for the project");
            }

            participants.add(new Share(project.get().getAccount(), project.get(),new Timestamp( System.currentTimeMillis())));

            double splitAmount = dto.getAmount() / participants.size();

            for (Share participant : participants) {
                ExpenseSplit split = new ExpenseSplit();
                split.setExpense(savedExpense);
                split.setAccount(participant.getAccount());
                split.setAmount(splitAmount);
                this.expenseSplitRepository.save(split);
            }
        }

        return ExpenseOutDTO.build(savedExpense);
    }

    public List<MonthlyStatsPerWeekDTO> monthlyStatsPerWeek(String userEmail, LocalDate date) {
        if (date == null) {
            date = LocalDate.now();
        }
        int weekCount = 1;

        LocalDate startDate = date.withDayOfMonth(1);
        LocalDate endDate = date.withDayOfMonth(date.lengthOfMonth());

        Optional<Account> account = this.accountRepository.findByEmail(userEmail);
        if (!account.isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Account entity not found");
        }
        List<Expense> expenses = this.expenseRepository.findAllBetweenDates(account.get().getId(), startDate, endDate);
        List<MonthlyStatsPerWeekDTO> weeklyStats = new ArrayList<>();

        LocalDate currentWeekStart = startDate;
        while (!currentWeekStart.isAfter(endDate)) {
            LocalDate currentWeekEnd = currentWeekStart.plusDays(6).isAfter(endDate) ? endDate : currentWeekStart.plusDays(6);

            // Filter expenses for the current week
            LocalDate finalCurrentWeekStart = currentWeekStart;
            List<Expense> weeklyExpenses = expenses.stream()
                    .filter(expense -> {
                        LocalDate expenseDate = expense.getDate().toLocalDate();
                        return !expenseDate.isBefore(finalCurrentWeekStart) && !expenseDate.isAfter(currentWeekEnd);
                    })
                    .collect(Collectors.toList());

            List<WeekStatsDTO> dailyStats = new ArrayList<>();

            // Create a list of all days in the current week
            for (LocalDate day = currentWeekStart; !day.isAfter(currentWeekEnd); day = day.plusDays(1)) {
                LocalDate finalDay = day;
                double dailyTotal = weeklyExpenses.stream()
                        .filter(expense -> expense.getDate().toLocalDate().equals(finalDay))
                        .mapToDouble(Expense::getAmount)
                        .sum();

                // Add the day to the stats, using 0 if no expenses were found
                dailyStats.add(new WeekStatsDTO(finalDay.getDayOfWeek().toString(), dailyTotal, new ExtraDTO("it")));
            }

            String[] days = {"", "st", "nd", "rd", "th"};
            String suffix = (weekCount < days.length) ? days[weekCount] : "th";
            weeklyStats.add(new MonthlyStatsPerWeekDTO(weekCount + suffix + " Week", dailyStats));
            weekCount ++;

            currentWeekStart = currentWeekEnd.plusDays(1);

        }

        return weeklyStats;
    }

    // Assuming ExtraDTO is defined as follows:
    public class ExtraDTO {
        private String code;

        public ExtraDTO(String code) {
            this.code = code;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }
    }



    public List<ExpenseOutDTO> monthlyStats(String userEmail, LocalDate date){
        if(date == null){
            date = LocalDate.now();
        }
        LocalDate startDate = date.withDayOfMonth(1);
        LocalDate endDate = date.withDayOfMonth(date.lengthOfMonth());
        Optional<Account> account = this.accountRepository.findByEmail(userEmail);
        if(!account.isPresent()){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Account entity not found");
        }
        return this.expenseRepository.findAllBetweenDates(account.get().getId(), startDate, endDate)
                .stream().map(expense -> {
                    return ExpenseOutDTO.build(expense);
                }).collect(Collectors.toList());

    }
    public List<ExpenseOutDTO> allChildExpenses(Long id, String userEmail, Integer limit){
        if(id == null) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Missing required parameter: id");
        Optional<Account> child = this.accountRepository.findById(id);
        if(!child.isPresent()){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Account entity not found");
        }
        Optional<Account> parent = this.accountRepository.findByEmail(userEmail);
        if(!parent.isPresent()){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Account entity (parent) not found");
        }
        child = parent.get().getChildren()
                .stream().filter(c -> c.getId() == id).findAny();
        if(!child.isPresent()){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Cannot see other user's children data");
        }

        List<ExpenseOutDTO> list = this.expenseRepository.findAllChildExpenses(child.get().getId(), parent.get().getId())
                .stream().map(expense -> ExpenseOutDTO.build(expense)).collect(Collectors.toList());

        if(limit == null || limit > list.size()){
            return list;
        }else if(limit <= list.size()){
            return list.subList(0,limit);
        }
        return List.of();

    }

    public ExpenseOutDTO updateExpense(String userEmail, ExpenseInDTO dto, Long id){
        Account account = this.accountRepository.findByEmail(userEmail)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Account entity not found"));

        if(dto == null ){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Missing required parameter");
        }
        if(id == null){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Missing required parameter: id");
        }
        Optional<Expense> entity = this.expenseRepository.findById(id);
        if(entity.get().getAccount().getId() != account.getId()){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Cannot update other user's expenses");
        }
        if(!entity.isPresent()){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Expense entity not found");
        }
        if(dto.getName() != null){
            entity.get().setName(dto.getName());
        }
        if(dto.getDescription() != null){
            entity.get().setDescription(dto.getDescription());
        }
        if(dto.getAmount() != null && dto.getAmount() > 0){
            entity.get().setAmount(dto.getAmount());
        }
        if(dto.getFrequency() != null){
            entity.get().setFrequency(dto.getFrequency().charAt(0));
        }
        if(dto.getDate() != null ){
            entity.get().setDate(dto.getDate());
        }
        if(dto.getImage() != null){
            entity.get().setImage(dto.getImage());
        }
        if(dto.getCategoryId() != null){
            Optional<Category> category = this.categoryRepository.findById(dto.getCategoryId().longValue());
            if (!category.isPresent()) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Category entity not found");
            }
            entity.get().setCategory(category.get());
        }
        if(dto.getProjectId() != null){
            Optional<Project> project = this.projectRepository.findById(dto.getProjectId().longValue());
            if(!project.isPresent()){
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Project entity not found");
            }
            entity.get().setProject(project.get());
        }

        return ExpenseOutDTO.build(this.expenseRepository.save(entity.get()));
    }

    public void deleteExpense(String userEmail, Long id){
        Account account = this.accountRepository.findByEmail(userEmail)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Account entity not found"));
        Expense expense = this.expenseRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Expense entity not found"));
        if(expense.getAccount().getId() != account.getId()){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Cannot delete other user's expense");
        }
        this.expenseRepository.delete(expense);
    }


    public PaginationDTO<ExpenseOutDTO> getAllExpenses(String userEmail, PageInDTO dto) {
        Account account = this.accountRepository.findByEmail(userEmail)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Account entity not found"));

        Sort.Direction direction = dto.getOrderDirection().equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
        Sort sort = Sort.by(direction, dto.getOrderBy());

        PageRequest pageRequest = PageRequest.of(
                Math.max(dto.getPage(), 0),
                dto.getSize() > 0 ? dto.getSize() : 15,
                sort
        );

        Page<Expense> expensePage = this.expenseRepository.findAllWithPagination(account.getId(), pageRequest);

        PaginationDTO<ExpenseOutDTO> result = new PaginationDTO<>();
        result.setRecords(expensePage.getContent().stream()
                .map(ExpenseOutDTO::build)
                .collect(Collectors.toList()));
        result.setOrderBy(dto.getOrderBy());
        result.setSize(expensePage.getSize());
        result.setPage(expensePage.getNumber());
        result.setTotalRecords((int) expensePage.getTotalElements());
        return result;
    }
    public PaginationDTO<ExpenseOutDTO> getAllChildExpenses(String userEmail, PageInDTO dto, Long id) {
        Account account = this.accountRepository.findByEmail(userEmail)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Account entity not found"));
        Optional<Account> child = this.accountRepository.findById(id);
        if(!child.isPresent()){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Account entity not found");
        }
        child = account.getChildren().stream().filter(c -> c.getId() == id).findAny();
        if(!child.isPresent()){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Cannot see other user's children data");
        }
        Sort.Direction direction = dto.getOrderDirection().equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
        Sort sort = Sort.by(direction, dto.getOrderBy());

        PageRequest pageRequest = PageRequest.of(
                Math.max(dto.getPage(), 0),
                dto.getSize() > 0 ? dto.getSize() : 15,
                sort
        );

        Page<Expense> expensePage = this.expenseRepository.findAllChildWithPagination(child.get().getId(), pageRequest);

        PaginationDTO<ExpenseOutDTO> result = new PaginationDTO<>();
        result.setRecords(expensePage.getContent().stream()
                .map(ExpenseOutDTO::build)
                .collect(Collectors.toList()));
        result.setOrderBy(dto.getOrderBy());
        result.setSize(expensePage.getSize());
        result.setPage(expensePage.getNumber());
        result.setTotalRecords((int) expensePage.getTotalElements());
        return result;
    }


    public void updateExpenseDates() {
        List<Expense> expenses = expenseRepository.findAllByFrequencyNot('S');

        for (Expense expense : expenses) {
            LocalDate currentDate = LocalDate.now();
            LocalDate nextDate = calculateNextDate(expense.getDate().toLocalDate(), expense.getFrequency());

            if (currentDate.isEqual(expense.getDate().toLocalDate()) || currentDate.isAfter(expense.getDate().toLocalDate())) {
                if (!expense.getDate().toLocalDate().isEqual(nextDate)) {
                    expense.setDate(Date.valueOf(nextDate));
                }
            }
        }

        expenseRepository.saveAll(expenses);
    }


    public static LocalDate calculateNextDate(LocalDate currentDate, char frequency) {
        switch (frequency) {
            case 'W':
                return currentDate.plusWeeks(1);
            case 'M':
                return currentDate.plusMonths(1);
            case 'Y':
                return currentDate.plusYears(1);
            default:
                return currentDate;
        }
    }

    public List<ExpenseOutDTO> getAllDueInNextFiveDays(String userEmail){
        Account account = this.accountRepository.findByEmail(userEmail)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Account entity not found"));
        return this.expenseRepository.findAllDueInNextFiveDays(LocalDate.now(), LocalDate.now().plusDays(5), account.getId())
                .stream().map(expense -> ExpenseOutDTO.build(expense)).collect(Collectors.toList());
}

}
