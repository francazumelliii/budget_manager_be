package it.jac.project_work.budget_manager.dto;

import it.jac.project_work.budget_manager.service.ProjectService;

public class ProjectInDTO {
    private String name;
    private String description;
    private String image;
    private Double goalAmount;

    private ProjectInDTO(){

    }

    public ProjectInDTO(String name, String description, String image, Double goalAmount) {
        this.name = name;
        this.description = description;
        this.image = image;
        this.goalAmount = goalAmount;
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
}
