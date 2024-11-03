package it.jac.project_work.budget_manager.service;

import it.jac.project_work.budget_manager.dto.ExpenseInDTO;
import it.jac.project_work.budget_manager.dto.ExpenseOutDTO;
import it.jac.project_work.budget_manager.entity.Account;
import it.jac.project_work.budget_manager.entity.Category;
import it.jac.project_work.budget_manager.entity.Expense;
import it.jac.project_work.budget_manager.entity.Project;
import it.jac.project_work.budget_manager.repository.AccountRepository;
import it.jac.project_work.budget_manager.repository.CategoryRepository;
import it.jac.project_work.budget_manager.repository.ExpenseRepository;
import it.jac.project_work.budget_manager.repository.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
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
        list = this.expenseRepository.findByAccountAndCreatedAtGreaterThanEqual(account, firstDayOfMonth)
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

}
