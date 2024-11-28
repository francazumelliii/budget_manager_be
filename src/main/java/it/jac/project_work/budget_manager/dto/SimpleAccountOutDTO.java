package it.jac.project_work.budget_manager.dto;

import it.jac.project_work.budget_manager.entity.Account;
import org.springframework.boot.autoconfigure.task.TaskExecutionProperties;

public class SimpleAccountOutDTO {
    private String name;
    private String surname;
    private String email;
    private String image;

    private Double splitAmount;

    private SimpleAccountOutDTO(){
    }

    public SimpleAccountOutDTO(String name, String surname, String email, String image, Double splitAmount) {
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.splitAmount = splitAmount;
        this.image = image;
    }



    public static SimpleAccountOutDTO build(Account entity){
        SimpleAccountOutDTO dto = new SimpleAccountOutDTO();
        dto.setName(entity.getName());
        dto.setSurname(entity.getSurname());
        dto.setEmail(entity.getEmail());
        dto.setImage(entity.getImage());
        return dto;
    }
    public static SimpleAccountOutDTO build(Account account, Double splitAmount) {
        SimpleAccountOutDTO dto = new SimpleAccountOutDTO();
        dto.setName(account.getName());
        dto.setEmail(account.getEmail());
        dto.setSplitAmount(splitAmount);

        return dto;
    }


    public Double getSplitAmount() {
        return splitAmount;
    }

    public void setSplitAmount(Double splitAmount) {
        this.splitAmount = splitAmount;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
