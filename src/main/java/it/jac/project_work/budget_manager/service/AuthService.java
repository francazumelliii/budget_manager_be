package it.jac.project_work.budget_manager.service;

import it.jac.project_work.budget_manager.dto.AccountInDTO;
import it.jac.project_work.budget_manager.dto.AccountOutDTO;
import it.jac.project_work.budget_manager.dto.AuthInDTO;
import it.jac.project_work.budget_manager.dto.AuthResponseDTO;
import it.jac.project_work.budget_manager.entity.Account;
import it.jac.project_work.budget_manager.entity.Role;
import it.jac.project_work.budget_manager.repository.AccountRepository;
import it.jac.project_work.budget_manager.security.JwtUtil;
import org.hibernate.annotations.CurrentTimestamp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.security.Timestamp;
import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
public class AuthService {
    @Autowired
    public final AccountRepository accountRepository;

    PasswordEncoder passwordEncoder =
            PasswordEncoderFactories.createDelegatingPasswordEncoder();
    public AuthService(AccountRepository accountRepository){
        this.accountRepository = accountRepository;
    }

    public AuthResponseDTO signup(AccountInDTO dto){
        Account account = new Account();
        if(dto.getEmail() != null){
            Optional<Account> optionalAccount = this.accountRepository.findByEmail(dto.getEmail());
            if(optionalAccount.isPresent()){
                throw new ResponseStatusException(HttpStatus.CONFLICT, "A user with the same email already exists");
            }
            account.setEmail(dto.getEmail());
        }else{
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Missing required parameter: email");
        }
        if(dto == null){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Missing required parameters");
        }
        if(dto.getName() != null){
            account.setName(dto.getName());
        }
        if(dto.getSurname() != null){
            account.setSurname(dto.getSurname());
        }


        if(dto.getPassword() != null){
            account.setPassword(passwordEncoder.encode(dto.getPassword()));
        }else{
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Missing required parameter: password");
        }
        if(dto.getBirthdate() != null ){
            account.setBithdate(dto.getBirthdate());
        }
        if(dto.getImage() != null){
            account.setImage(dto.getImage());
        }else{
            account.setImage("default-image.png");
        }

        account.setMenuList("[{\"name\": \"Menu\", \"value\": \"/homepage\", \"icon\": \"fa-solid fa-house\"}, {\"name\": \"Profile\", \"value\": \"/profile\"}], \"value\": \"fa-solid fa-house\"");
        Set<Role> roles = new HashSet<>();
        if (dto.getParentId() != null) {
            Optional<Account> optionalParent = this.accountRepository.findById(dto.getParentId());
            if (optionalParent.isPresent()) {
                account.setParent(optionalParent.get());
                roles.add(Role.USER);
                account.setMenuList("[{\"name\": \"Menu\", \"value\": \"/homepage\", \"icon\": \"fa-solid fa-house\"}]");
            } else {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Parent Entity not found id: [" + dto.getParentId() + "]");
            }
        } else {
            roles.add(Role.USER);
            roles.add(Role.PARENT);
        }

        account.setRoles(roles);

        AuthResponseDTO response = new AuthResponseDTO();

        account =  this.accountRepository.save(account);
        response.setUser(AccountOutDTO.build(account));
        System.out.println(account.getRoles());
        response.setJwt(JwtUtil.generateToken(account.getEmail(), account.getRoles()));
        return response;
    }

    public AuthResponseDTO login(AuthInDTO dto){
        Account account = new Account();
        if(dto == null){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Missing required parameters");
        }
        if(dto.getEmail() == null){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Missing required parameter: email");
        }
        Optional<Account> optionalAccount = this.accountRepository.findByEmail(dto.getEmail());
        if(!optionalAccount.isPresent()){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Account entity not found");
        }
        account = optionalAccount.get();

        if(!passwordEncoder.matches(dto.getPassword(), account.getPassword())){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid email or password");
        }
        AuthResponseDTO response = new AuthResponseDTO();
        response.setUser(AccountOutDTO.build(account));
        response.setJwt(JwtUtil.generateToken(account.getEmail(), account.getRoles()));

       return response;
    }
}


