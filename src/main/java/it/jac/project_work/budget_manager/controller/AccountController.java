package it.jac.project_work.budget_manager.controller;


import io.jsonwebtoken.Jwt;
import it.jac.project_work.budget_manager.dto.AccountInDTO;
import it.jac.project_work.budget_manager.dto.ExpenseOutDTO;
import it.jac.project_work.budget_manager.entity.Account;
import it.jac.project_work.budget_manager.repository.AccountRepository;
import it.jac.project_work.budget_manager.security.JwtService;
import it.jac.project_work.budget_manager.security.JwtUtil;
import it.jac.project_work.budget_manager.service.AccountService;
import it.jac.project_work.budget_manager.service.AuthService;
import it.jac.project_work.budget_manager.service.ExpenseService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
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
    public AccountController(AccountService accountService, JwtService jwtService, AccountRepository accountRepository, ExpenseService expenseService){
        this.accountService = accountService;
        this.jwtService = jwtService;
        this.accountRepository = accountRepository;
        this.expenseService = expenseService;
    }


    @GetMapping("")
    public Collection<GrantedAuthority> getLastMonthExpenses(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        authentication.getAuthorities();
        return (Collection<GrantedAuthority>) authentication.getAuthorities();
    }

}
