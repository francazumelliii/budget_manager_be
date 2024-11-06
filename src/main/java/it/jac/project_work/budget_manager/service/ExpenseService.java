package it.jac.project_work.budget_manager.service;

import it.jac.project_work.budget_manager.dto.ExpenseInDTO;
import it.jac.project_work.budget_manager.dto.ExpenseOutDTO;
import it.jac.project_work.budget_manager.dto.MonthlyStatsPerWeekDTO;
import it.jac.project_work.budget_manager.dto.WeekStatsDTO;
import it.jac.project_work.budget_manager.entity.Account;
import it.jac.project_work.budget_manager.entity.Category;
import it.jac.project_work.budget_manager.entity.Expense;
import it.jac.project_work.budget_manager.entity.Project;
import it.jac.project_work.budget_manager.repository.AccountRepository;
import it.jac.project_work.budget_manager.repository.CategoryRepository;
import it.jac.project_work.budget_manager.repository.ExpenseRepository;
import it.jac.project_work.budget_manager.repository.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.data.cassandra.CassandraReactiveRepositoriesAutoConfiguration;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
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
    public ExpenseService(ExpenseRepository expenseRepository, AccountRepository accountRepository, CategoryRepository categoryRepository, ProjectRepository projectRepository){
        this.accountRepository = accountRepository;
        this.expenseRepository = expenseRepository;
        this.categoryRepository = categoryRepository;
        this.projectRepository = projectRepository;
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

    public ExpenseOutDTO saveExpense(ExpenseInDTO dto, String userEmail){

        Expense expense = new Expense();
        Optional<Category> category;
        Optional<Project> project;
        Optional<Account> account = this.accountRepository.findByEmail(userEmail);
        if(!account.isPresent()){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }
        expense.setAccount(account.get());

        if(dto.getCategoryId() == null){
            category = this.categoryRepository.findById(10L); // 'EXTRA' category
        }else{
            category = this.categoryRepository.findById(dto.getCategoryId().longValue());
            if(!category.isPresent()){
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Category Entity not found");
            }
        }
        expense.setCategory(category.get());

        if(dto.getProjectId() == null){
            expense.setProject(null);
        }else{
            project = this.projectRepository.findById(dto.getProjectId().longValue());
            if(!project.isPresent()){
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Project Entity not found");
            }
            expense.setProject(project.get());
        }
        if(dto.getName() == null){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Missing required parameter: name");
        }
        expense.setName(dto.getName());
        if(dto.getAmount() == null){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Missing required parameter: amount");
        }
        expense.setAmount(dto.getAmount());
        if(dto.getFrequency() == null){
            expense.setFrequency('S');
        }else{
            expense.setFrequency(dto.getFrequency().charAt(0));
        }
        expense.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        expense.setDescription(dto.getDescription());
        expense.setImage(dto.getImage());
        if(dto.getDate() == null){
            expense.setDate(Date.valueOf(LocalDate.now()));
        }else{
            expense.setDate(dto.getDate());
        }

        return ExpenseOutDTO.build(this.expenseRepository.save(expense));
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

}
