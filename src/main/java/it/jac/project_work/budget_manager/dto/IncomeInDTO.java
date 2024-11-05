package it.jac.project_work.budget_manager.dto;

import java.sql.Date;
import java.time.LocalDateTime;

public class IncomeInDTO {
        private String name;
        private String description;
        private String frequency;
        private Double amount;
        private String image;
        private Date date;

    private IncomeInDTO(){}

    public IncomeInDTO(String name, String description, String frequency, Double amount, String image, Date date) {
        this.name = name;
        this.description = description;
        this.frequency = frequency;
        this.amount = amount;
        this.image = image;
        this.date = date;
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

    public String getFrequency() {
        return frequency;
    }

    public void setFrequency(String frequency) {
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

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
