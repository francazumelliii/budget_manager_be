package it.jac.project_work.budget_manager.controller;


import io.jsonwebtoken.Jwt;
import it.jac.project_work.budget_manager.dto.*;
import it.jac.project_work.budget_manager.entity.Account;
import it.jac.project_work.budget_manager.entity.Expense;
import it.jac.project_work.budget_manager.repository.AccountRepository;
import it.jac.project_work.budget_manager.repository.ExpenseRepository;
import it.jac.project_work.budget_manager.security.JwtService;
import it.jac.project_work.budget_manager.security.JwtUtil;
import it.jac.project_work.budget_manager.service.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collection;
import java.util.Collections;
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
}
