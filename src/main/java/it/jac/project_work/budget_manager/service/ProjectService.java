package it.jac.project_work.budget_manager.service;

import it.jac.project_work.budget_manager.dto.ProjectOutDTO;
import it.jac.project_work.budget_manager.entity.Account;
import it.jac.project_work.budget_manager.repository.AccountRepository;
import it.jac.project_work.budget_manager.repository.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProjectService {

    @Autowired
    public final ProjectRepository projectRepository;
    @Autowired
    public final AccountRepository accountRepository;

    public ProjectService(ProjectRepository projectRepository, AccountRepository accountRepository){
        this.projectRepository = projectRepository;
        this.accountRepository = accountRepository;
    }

    public List<ProjectOutDTO> getProjectListByAccount(String userMail, Integer expensesLimit){
        Optional<Account> account = this.accountRepository.findByEmail(userMail);
        if(!account.isPresent()){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Entity not found");
        }
        return this.projectRepository.findAllByAccount(account.get())
                .stream().map(project -> ProjectOutDTO.build(project)).collect(Collectors.toList());
    }

}
