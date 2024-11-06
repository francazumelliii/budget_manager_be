package it.jac.project_work.budget_manager.service;


import it.jac.project_work.budget_manager.dto.AccountInDTO;
import it.jac.project_work.budget_manager.dto.AccountOutDTO;
import it.jac.project_work.budget_manager.dto.MonthlyStatsDTO;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Period;
import it.jac.project_work.budget_manager.entity.Account;
import it.jac.project_work.budget_manager.entity.Role;
import it.jac.project_work.budget_manager.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.*;

@Service
public class AccountService {
    @Autowired
    private final AccountRepository accountRepository;
    PasswordEncoder passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
    @Autowired
    public AccountService(AccountRepository accountRepository){
        this.accountRepository = accountRepository;
    }

    public AccountOutDTO saveChild(AccountInDTO dto, String userEmail){
        Optional<Account> account = this.accountRepository.findByEmail(userEmail);
        Account child = new Account();
        if(!account.isPresent()){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Account entity not found");
        }
        if(this.accountRepository.findByEmail(dto.getEmail()).isPresent()){
            throw new ResponseStatusException(HttpStatus.CONFLICT, "A user with the same email already exists");
        }
        if(dto.getName() == null){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Missing required parameter: name");
        }
        child.setName(dto.getName());
        if(dto.getSurname() == null){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Missing required parameter: surname");
        }
        child.setSurname(dto.getSurname());
        if(dto.getEmail() == null){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Missing required parameter: email");
        }
        child.setEmail(dto.getEmail());
        if(dto.getPassword() == null){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Missing required parameter: password");
        }
        child.setPassword(passwordEncoder.encode(dto.getPassword()));
        if(dto.getBirthdate() == null){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Missing required parameter: birthdate");
        }
        child.setImage(dto.getImage() == null ? "default.png" : dto.getImage());
        if(dto.getBirthdate() == null){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Missing required parameter: birthdate");
        }
        LocalDate today = LocalDate.now();
        int age = Period.between(dto.getBirthdate().toLocalDate(), today).getYears();
        if(age >= 18){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "The user must be < 18 years old");
        }

        child.setBithdate(dto.getBirthdate());
        child.setParent(account.get());
        Set<Role> role = new HashSet<>();
        role.add(Role.USER);
        child.setRoles(role);
        child.setMenuList("[{\"name\": \"Menu\", \"value\": \"/homepage\", \"icon\": \"fa-solid fa-house\"}]");
        child.setCreatedAt(new Timestamp(System.currentTimeMillis()));

        return AccountOutDTO.build(this.accountRepository.save(child));
    }

    public MonthlyStatsDTO monthlyStats(LocalDate date, String userEmail) {
        Account account = accountRepository.findByEmail(userEmail)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Account entity not found"));

        LocalDate startDate = (date == null ? LocalDate.now() : date).withDayOfMonth(1);
        LocalDate endDate = startDate.withDayOfMonth(startDate.lengthOfMonth());
        Double totalIncomes = this.accountRepository.findMonthlyIncome(account.getId(), startDate, endDate);
        if(totalIncomes == null || totalIncomes.isNaN()){
            totalIncomes = (double) 0;
        }
        Double totalExpenses = this.accountRepository.findLastMonthExpense(account.getId(), startDate, endDate);
        if(totalExpenses == null || totalExpenses.isNaN()){
            totalExpenses = (double) 0;
        }
        return new MonthlyStatsDTO(round(totalExpenses,2), round(totalIncomes,2));

    }


    private Double round(Double value, int places) {
        if (value == null) return 0.0;
        BigDecimal bd = BigDecimal.valueOf(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }



}
