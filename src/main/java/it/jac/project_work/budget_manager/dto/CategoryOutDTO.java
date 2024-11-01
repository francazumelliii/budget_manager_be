package it.jac.project_work.budget_manager.dto;

import it.jac.project_work.budget_manager.entity.Category;

public class CategoryOutDTO {
    private String name;
    private String description;
    private String image;

    public CategoryOutDTO(){}

    public CategoryOutDTO(String name, String description, String image) {
        this.name = name;
        this.description = description;
        this.image = image;
    }
    public static CategoryOutDTO build(Category entity){
        CategoryOutDTO dto = new CategoryOutDTO();
        dto.setName(entity.getName());
        dto.setDescription(entity.getDescription());
        dto.setImage(entity.getImage());
        return dto;
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
}
