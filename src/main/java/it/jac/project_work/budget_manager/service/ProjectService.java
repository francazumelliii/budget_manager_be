package it.jac.project_work.budget_manager.service;

import it.jac.project_work.budget_manager.dto.*;
import it.jac.project_work.budget_manager.entity.Account;
import it.jac.project_work.budget_manager.entity.Project;
import it.jac.project_work.budget_manager.entity.Share;
import it.jac.project_work.budget_manager.entity.ShareId;
import it.jac.project_work.budget_manager.repository.AccountRepository;
import it.jac.project_work.budget_manager.repository.ExpenseRepository;
import it.jac.project_work.budget_manager.repository.ProjectRepository;
import it.jac.project_work.budget_manager.repository.ShareRepository;
import org.apache.tomcat.util.http.parser.HttpParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProjectService {

    @Autowired
    public final ProjectRepository projectRepository;
    @Autowired
    public final AccountRepository accountRepository;

    @Autowired
    public final ShareRepository shareRepository;
    @Autowired
    public final ExpenseRepository expenseRepository;


    public ProjectService(ProjectRepository projectRepository, AccountRepository accountRepository, ShareRepository shareRepository, ExpenseRepository expenseRepository){
        this.projectRepository = projectRepository;
        this.accountRepository = accountRepository;
        this.shareRepository = shareRepository;
        this.expenseRepository = expenseRepository;
    }

    public PaginationDTO<ProjectOutDTO> getProjectListByAccount(String userMail, PageInDTO dto) {
        Account account = this.accountRepository.findByEmail(userMail)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Account entity not found"));
        Sort.Direction direction = dto.getOrderDirection().equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
        Sort sort = Sort.by(direction, dto.getOrderBy());
        PageRequest pageRequest = PageRequest.of(
                Math.max(dto.getPage(), 0),
                dto.getSize() > 0 ? dto.getSize() : 15,
                sort
        );
        Page<Project> projectPage = this.projectRepository.findAllByAccount(account, pageRequest);
        PaginationDTO<ProjectOutDTO> result = new PaginationDTO<>();
        result.setPage(projectPage.getNumber());
        result.setSize(projectPage.getSize());
        result.setOrderBy(projectPage.getSort().toString());
        result.setTotalRecords((int) projectPage.getTotalElements());
        result.setRecords(projectPage.getContent().stream().map(p -> ProjectOutDTO.build(p)).collect(Collectors.toList()));
        return result;
    }


    public ProjectOutDTO saveProject(String userEmail, ProjectInDTO dto) {
        Optional<Account> account = this.accountRepository.findByEmail(userEmail);
        Project project = new Project();
        if (!account.isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Account entity not found");
        }
        if (dto.getName() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Missing required parameter: name");
        }
        project.setName(dto.getName());
        project.setAccount(account.get());
        project.setDescription(dto.getDescription());
        project.setImage(dto.getImage() == null ? "default.png" : dto.getImage());
        project.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        project.setGoalAmount(dto.getGoalAmount());
        return ProjectOutDTO.build(this.projectRepository.save(project));
    }

    public List<ProjectOutDTO> findAllChildProjects(String userEmail, Long id){
        Account parent = this.accountRepository.findByEmail(userEmail)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Account entity not found"));
        Optional<Account> child = this.accountRepository.findById(id);
        if(!child.isPresent()){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"Account entity not found");
        }
        child = parent.getChildren().stream().filter(c -> c.getId() == id).findAny();
        if(!child.isPresent()){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Cannot see other user's children data");
        }
        return this.projectRepository.findAllChildProjects(child.get().getId(), parent.getId())
                .stream().map(project -> ProjectOutDTO.build(project)).collect(Collectors.toList());

    }

    public ProjectOutDTO addAccountToProject(ShareProjectInDTO dto, String userEmail) {
        Account account = this.accountRepository.findByEmail(userEmail)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Account entity not found"));

        if (dto.getProjectId() == null)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Missing required parameter: projectId");
        if (dto.getEmails() == null || dto.getEmails().isEmpty())
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Missing required parameter / incorrect data format: emails");

        Project project = this.projectRepository.findById(dto.getProjectId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Project entity not found"));

        boolean isOwner = account.getProjects().stream()
                .anyMatch(p -> p.getId().equals(dto.getProjectId()));

        if (!isOwner)
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Cannot update other user's project");

        dto.getEmails().forEach(email -> {
            Optional<Account> person = this.accountRepository.findByEmail(email);
            person.ifPresent(p -> {
                ShareId shareId = new ShareId(p.getId(), project.getId());
                if (!this.shareRepository.existsById(shareId)) {
                    Share share = new Share(p, project, new Timestamp(System.currentTimeMillis()));
                    this.shareRepository.save(share);
                    System.out.println("Shared with: " + email);
                } else {
                    System.out.println("Share already exists for: " + email);
                }
            });
        });

        return ProjectOutDTO.build(project);
    }

    public ProjectOutDTO removeAccountFromProject(String userEmail, String email, Long projectId, String option) {
        Account account = this.accountRepository.findByEmail(userEmail)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Account entity not found"));

        if (email.isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Missing required parameter: email");
        }
        if (projectId == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Missing required parameter: projectId");
        }
        if(option.isBlank()){
            option = "keep";
        }

        Project project = this.projectRepository.findById(projectId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Project entity not found"));

        Account toRemove = this.accountRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Account entity not found, email: " + email));

        boolean isOwner = account.getProjects().stream()
                .anyMatch(p -> p.getId().equals(projectId));
        if (!isOwner) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Cannot remove accounts from another user's project");
        }

        Share share = toRemove.getShares().stream()
                .filter(s -> s.getProject().getId().equals(projectId))
                .findFirst()
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.CONFLICT, "User hasn't joined the project"));

        this.shareRepository.deleteById(share.getId());

        Project updatedProject = this.projectRepository.findById(projectId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Project entity not found after deletion"));

        if(option.equalsIgnoreCase("remove")){
            this.expenseRepository.deleteAllByProject(project);
        }
        return ProjectOutDTO.build(updatedProject);
    }




}
