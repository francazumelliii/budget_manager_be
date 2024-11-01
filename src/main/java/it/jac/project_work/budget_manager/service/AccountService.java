package it.jac.project_work.budget_manager.service;


import it.jac.project_work.budget_manager.dto.AccountInDTO;
import it.jac.project_work.budget_manager.dto.ExpenseOutDTO;
import it.jac.project_work.budget_manager.entity.Account;
import it.jac.project_work.budget_manager.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class AccountService {
    @Autowired
    private final AccountRepository accountRepository;
    @Autowired

    public AccountService(AccountRepository accountRepository){
        this.accountRepository = accountRepository;
    }



}
