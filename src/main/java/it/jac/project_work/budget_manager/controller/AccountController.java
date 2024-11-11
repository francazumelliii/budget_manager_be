package it.jac.project_work.budget_manager.controller;


import it.jac.project_work.budget_manager.dto.*;
import it.jac.project_work.budget_manager.entity.Account;
import it.jac.project_work.budget_manager.repository.AccountRepository;
import it.jac.project_work.budget_manager.repository.ExpenseRepository;
import it.jac.project_work.budget_manager.security.JwtService;
import it.jac.project_work.budget_manager.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.oauth2.client.OAuth2ClientSecurityMarker;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/account")
public class AccountController {
    @Autowired
    public final AccountService accountService;
    @Autowired
    public final JwtService jwtService;
    @Autowired
    public final AccountRepository accountRepository;
    @Autowired
    public final ExpenseService expenseService;
    @Autowired
    public final ExpenseRepository expenseRepository;
    @Autowired
    public final IncomeService incomeService;
    @Autowired
    public final ProjectService projectService;
    public AccountController(AccountService accountService, JwtService jwtService, AccountRepository accountRepository, ExpenseService expenseService, ExpenseRepository expenseRepository, IncomeService incomeService, ProjectService projectService){
        this.accountService = accountService;
        this.jwtService = jwtService;
        this.accountRepository = accountRepository;
        this.expenseService = expenseService;
        this.expenseRepository = expenseRepository;
        this.incomeService = incomeService;
        this.projectService = projectService;
    }
    @GetMapping("/updateValues")
    public void updateValues(){
        this.expenseService.updateExpenseDates();
        this.incomeService.updateIncomeDates();
    }

    @GetMapping("/me/expenses/recent")
    public List<ExpenseOutDTO> getLastMonthExpenses(@Param("limit") Integer limit){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Optional<Account> account = this.accountRepository.findByEmail(authentication.getName());
        if(!account.isPresent()){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Entity not found");
        }
        return this.expenseService.getLastMonthExpenses(account.get(), limit);

    }
    @GetMapping("/me/incomes/recent")
    public List<IncomeOutDTO> getLastMonthIncomes(@Param("limit") Integer limit){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName();
        Optional<Account> account = this.accountRepository.findByEmail(userEmail);
        if(!account.isPresent()){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Entity not found");
        }
        return this.incomeService.getLastMonthIncomes(account.get(), limit);
    }

    @GetMapping("/me/expenses/all")
    public PaginationDTO<ExpenseOutDTO> getAllExpenses(
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "size", defaultValue = "15") Integer size,
            @RequestParam(value = "order", defaultValue = "date") String orderBy,
            @RequestParam(value = "direction", defaultValue = "asc") String direction) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName();

        PageInDTO dto = new PageInDTO(orderBy, page, size, direction);
        return this.expenseService.getAllExpenses(userEmail, dto);
    }
    @GetMapping("/me/incomes/all")
    public PaginationDTO<IncomeOutDTO> getAllIncomes(
            @RequestParam(value="page",defaultValue = "0") Integer page,
            @RequestParam(value="size",defaultValue = "15") Integer size,
            @RequestParam(value="order",defaultValue = "date") String orderBy,
            @RequestParam(value="direction",defaultValue = "asc") String direction
    ){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName();
        PageInDTO dto = new PageInDTO(orderBy, page,size, direction);
        return this.incomeService.getAllIncomes(userEmail,dto);
    }


    @GetMapping("/me/projects")
    public List<ProjectOutDTO> getAllUserProjects(@Param("expensesLimit") Integer limit){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName();
        return this.projectService.getProjectListByAccount(userEmail, limit);
    }

    @PostMapping("/me/expenses")
    public ExpenseOutDTO saveExpense(@RequestBody ExpenseInDTO dto){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName();
        return this.expenseService.saveExpense(dto, userEmail);
    }
    @PostMapping("/me/incomes")
    public IncomeOutDTO saveIncome(@RequestBody IncomeInDTO dto){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName();
        return this.incomeService.saveIncome(dto, userEmail);
    }

    @PostMapping("/me/projects")
    public ProjectOutDTO saveProject(@RequestBody ProjectInDTO dto){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName();
        return this.projectService.saveProject(userEmail, dto);
    }
    @PostMapping("/me/children")
    public AccountOutDTO saveChild(@RequestBody AccountInDTO dto){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName();
        return this.accountService.saveChild(dto, userEmail);
    }

    @GetMapping("/me/expenses/stats/monthly")
    public List<MonthlyStatsPerWeekDTO> getMonthlyStats(@RequestParam("date") LocalDate date, @RequestParam("weeklyDivided") boolean weeklyDivided){
      Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
      String userEmail = authentication.getName();
      if(weeklyDivided){
        return this.expenseService.monthlyStatsPerWeek(userEmail, date);
      }
      return null;
    }
    @GetMapping("/me/stats/monthly")
    public MonthlyStatsDTO monthlyStats(@RequestParam("date") LocalDate date){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName();
        return this.accountService.monthlyStats(date, userEmail);

    }
    @GetMapping("/me/parent/{id}/expenses")
    public List<ExpenseOutDTO> allChildExpenses(@PathVariable("id") Long id){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName();

        return this.expenseService.allChildExpenses(id, userEmail);
    }

    @PatchMapping("/me/expenses/{id}")
    public ExpenseOutDTO updateExpense(@PathVariable("id") Long id, @RequestBody ExpenseInDTO dto){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName();
        return this.expenseService.updateExpense(userEmail, dto, id);
    }

    @DeleteMapping("/me/expenses/{id}")
    public void deleteExpense(@PathVariable("id") Long id){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName();
        this.expenseService.deleteExpense(userEmail, id);
    }

    @PatchMapping("/me/incomes/{id}")
    public IncomeOutDTO updateIncome(@PathVariable("id") Long id, @RequestBody IncomeInDTO dto){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName();
        return this.incomeService.updateIncome(userEmail, dto, id);
    }

    @DeleteMapping("/me/incomes/{id}")
    public void deleteIncome(@PathVariable("id") Long id){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName();
         this.incomeService.deleteIncome(userEmail, id);
    }




}
