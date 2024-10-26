package it.jac.project_work.budget_manager.controller;


import it.jac.project_work.budget_manager.dto.AccountInDTO;
import it.jac.project_work.budget_manager.dto.AuthResponseDTO;
import it.jac.project_work.budget_manager.entity.Account;
import it.jac.project_work.budget_manager.repository.AccountRepository;
import it.jac.project_work.budget_manager.security.JwtUtil;
import it.jac.project_work.budget_manager.service.AccountService;
import it.jac.project_work.budget_manager.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {
    @Autowired
    public final AccountRepository accountRepository;
    @Autowired
    public final AuthService authService;

    public AuthController(AccountRepository accountRepository, AuthService authService){
        this.accountRepository = accountRepository;
        this.authService = authService;
    }

    @PostMapping("/signup")
    public AuthResponseDTO signup(@RequestBody AccountInDTO dto){
        return this.authService.signup(dto);


    }



}
