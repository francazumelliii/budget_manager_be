package it.jac.project_work.budget_manager.dto;

import it.jac.project_work.budget_manager.entity.Category;
import it.jac.project_work.budget_manager.entity.Expense;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class ExpenseOutDTO {
    private Long id;
    private String name;
    private String description;
    private Double amount;
    private char frequency;
    private LocalDateTime createdAt;
    private String image;
    private CategoryOutDTO category;
    private Long projectId;
    private LocalDate date;
    private Long accountId;
    private List<SimpleAccountOutDTO> participants;

    private ExpenseOutDTO(){}

    private ExpenseOutDTO(Long id, String name, String description, Double amount, char frequency, LocalDateTime createdAt, String image, CategoryOutDTO category,Long accountId,  Long projectId, LocalDate date, List<SimpleAccountOutDTO> participants) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.amount = amount;
        this.frequency = frequency;
        this.createdAt = createdAt;
        this.image = image;
        this.category = category;
        this.projectId = projectId;
        this.accountId = accountId;
        this.participants = participants;
        this.date = date;
    }

    public static ExpenseOutDTO build(Expense entity) {
        ExpenseOutDTO dto = new ExpenseOutDTO();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setDescription(entity.getDescription());
        dto.setAmount(entity.getAmount());
        dto.setFrequency(entity.getFrequency());
        dto.setCreatedAt(entity.getCreatedAt().toLocalDateTime());
        dto.setImage(entity.getImage());
        dto.setDate(entity.getDate().toLocalDate());
        dto.setAccountId(entity.getAccount().getId());
        dto.setParticipants(List.of());

        if(entity.getExpenseSplits() != null){
            List<SimpleAccountOutDTO> splits = entity.getExpenseSplits()
                    .stream().map(split -> new SimpleAccountOutDTO(
                            split.getAccount().getName(),
                            split.getAccount().getSurname(),
                            split.getAccount().getEmail(),
                            split.getAccount().getImage(),
                            split.getAmount()
                    )).collect(Collectors.toList());
            dto.setParticipants(splits);
        }

        if(entity.getCategory()!= null) {
            dto.setCategory(CategoryOutDTO.build(entity.getCategory()));
        }
        if (entity.getProject() != null) {
            dto.setProjectId(entity.getProject().getId());
        } else {
            dto.setProjectId(null);
        }


        return dto;
    }

    public List<SimpleAccountOutDTO> getParticipants() {
        return participants;
    }

    public void setParticipants(List<SimpleAccountOutDTO> participants) {
        this.participants = participants;
    }

    public Long getAccountId() {
        return accountId;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
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

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public char getFrequency() {
        return frequency;
    }

    public void setFrequency(char frequency) {
        this.frequency = frequency;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public CategoryOutDTO getCategory() {
        return category;
    }

    public void setCategory(CategoryOutDTO category) {
        this.category = category;
    }

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }


}
