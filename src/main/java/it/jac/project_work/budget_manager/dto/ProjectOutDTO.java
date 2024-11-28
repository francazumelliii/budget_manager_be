package it.jac.project_work.budget_manager.dto;

import it.jac.project_work.budget_manager.entity.*;
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
    //TODO: put amount to pay per project due to request caller;
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

    public static ProjectOutDTO build(Project entity) {
        ProjectOutDTO dto = new ProjectOutDTO();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setDescription(entity.getDescription());
        dto.setImage(entity.getImage());
        dto.setGoalAmount(entity.getGoalAmount());
        dto.setCreatedAt(entity.getCreatedAt().toLocalDateTime());

        // Set expenses
        dto.setExpenses(entity.getExpenses()
                .stream()
                .map(ExpenseOutDTO::build)
                .collect(Collectors.toList()));

        // Calculate and set the creator's split amount
        Account creatorAccount = entity.getAccount();
        Double creatorSplitAmount = entity.getExpenses()
                .stream()
                .flatMap(expense -> expense.getExpenseSplits().stream())
                .filter(expenseSplit -> expenseSplit.getAccount().equals(creatorAccount))
                .map(ExpenseSplit::getAmount)
                .reduce(0.0, Double::sum);

        dto.setCreator(SimpleAccountOutDTO.build(creatorAccount, creatorSplitAmount));

        // Calculate and set accounts' split amounts
        List<SimpleAccountOutDTO> accounts = entity.getShares()
                .stream()
                .map(share -> {
                    Account account = share.getAccount();
                    Double splitAmount = entity.getExpenses()
                            .stream()
                            .flatMap(expense -> expense.getExpenseSplits().stream())
                            .filter(expenseSplit -> expenseSplit.getAccount().equals(account))
                            .map(ExpenseSplit::getAmount)
                            .reduce(0.0, Double::sum);

                    return SimpleAccountOutDTO.build(account, splitAmount);
                })
                .collect(Collectors.toList());

        dto.setAccounts(accounts);

        return dto;
    }



}
