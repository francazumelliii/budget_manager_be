package it.jac.project_work.budget_manager.dto;

import it.jac.project_work.budget_manager.entity.Account;
import it.jac.project_work.budget_manager.entity.Expense;
import it.jac.project_work.budget_manager.entity.Project;
import it.jac.project_work.budget_manager.entity.Share;
import org.springframework.boot.autoconfigure.task.TaskExecutionProperties;

import javax.sound.sampled.Port;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class ProjectOutDTO {
    public static final Timestamp timestamp = new Timestamp(System.currentTimeMillis());
    private Long id;
    private String name;
    private String description;
    private String image;
    private Double goalAmount;
    private LocalDateTime createdAt;
    private List<ExpenseOutDTO> expenses;
    private SimpleAccountOutDTO creator;
    private List<SimpleAccountOutDTO> accounts;

    private ProjectOutDTO(){}

    private ProjectOutDTO(Long id, String name, String description, String image, Double goalAmount, LocalDateTime createdAt, List<ExpenseOutDTO> expenses, List<SimpleAccountOutDTO> accounts, SimpleAccountOutDTO creator) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.image = image;
        this.goalAmount = goalAmount;
        this.createdAt = createdAt;
        this.expenses = expenses;
        this.accounts = accounts;
        this.creator = creator;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Double getGoalAmount() {
        return goalAmount;
    }

    public void setGoalAmount(Double goalAmount) {
        this.goalAmount = goalAmount;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public List<ExpenseOutDTO> getExpenses() {
        return expenses;
    }

    public void setExpenses(List<ExpenseOutDTO> expenses) {
        this.expenses = expenses;
    }

    public List<SimpleAccountOutDTO> getAccounts() {
        return accounts;
    }

    public void setAccounts(List<SimpleAccountOutDTO> accounts) {
        this.accounts = accounts;
    }

    public SimpleAccountOutDTO getCreator() {
        return creator;
    }

    public void setCreator(SimpleAccountOutDTO creator) {
        this.creator = creator;
    }

    public static ProjectOutDTO build(Project entity){
        ProjectOutDTO dto = new ProjectOutDTO();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setDescription(entity.getDescription());
        dto.setImage(entity.getImage());
        dto.setGoalAmount(entity.getGoalAmount());
        dto.setCreatedAt(entity.getCreatedAt().toLocalDateTime());
        dto.setExpenses(entity.getExpenses().stream().map(exp-> ExpenseOutDTO.build(exp)).collect(Collectors.toList()));
        dto.setCreator(SimpleAccountOutDTO.build(entity.getAccount()));
        List<SimpleAccountOutDTO> accounts = entity.getShares()
                .stream().map(Share::getAccount)
                .map(SimpleAccountOutDTO:: build)
                .collect(Collectors.toList());
        dto.setAccounts(accounts);
        return dto;
    }

}
