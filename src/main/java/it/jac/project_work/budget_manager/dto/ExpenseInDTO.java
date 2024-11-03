package it.jac.project_work.budget_manager.dto;

import java.sql.Date;

public class ExpenseInDTO {

    private String name;
    private Double amount;
    private String description;
    private String frequency;
    private Date date;
    private Integer categoryId;
    private String image;
    private Integer projectId;

    public ExpenseInDTO(){}

    public ExpenseInDTO(String name, Double amount, String description, String frequency, Date date, Integer categoryId, String image, Integer projectId) {
        this.name = name;
        this.amount = amount;
        this.description = description;
        this.frequency = frequency;
        this.date = date;
        this.categoryId = categoryId;
        this.image = image;
        this.projectId = projectId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
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

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Integer getProjectId() {
        return projectId;
    }

    public void setProjectId(Integer projectId) {
        this.projectId = projectId;
    }
}
