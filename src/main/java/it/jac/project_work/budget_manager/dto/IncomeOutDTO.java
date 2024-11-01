package it.jac.project_work.budget_manager.dto;

import it.jac.project_work.budget_manager.entity.Income;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class IncomeOutDTO {
        private Long id;
        private String name;
        private String description;
        private char frequency;
        private Double amount;
        private String image;
        private LocalDateTime createdAt;
        private LocalDate date;

        public IncomeOutDTO(){}

    public IncomeOutDTO(Long id, String name, String description, char frequency, Double amount, String image, LocalDateTime createdAt, LocalDate date) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.frequency = frequency;
        this.amount = amount;
        this.image = image;
        this.createdAt = createdAt;
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public char getFrequency() {
        return frequency;
    }

    public void setFrequency(char frequency) {
        this.frequency = frequency;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public static IncomeOutDTO build(Income entity){
            IncomeOutDTO dto = new IncomeOutDTO();
            dto.setId(entity.getId());
            dto.setName(entity.getName());
            dto.setDescription(entity.getDescription());
            dto.setAmount(entity.getAmount());
            dto.setFrequency(entity.getFrequency());
            dto.setDate(entity.getDate().toLocalDate());
            dto.setCreatedAt(entity.getCreatedAt().toLocalDateTime());
            return dto;
    }
}
