package it.jac.project_work.budget_manager.dto;

import java.sql.Date;

public class AccountInDTO {
    private String name;
    private String surname;
    private String email;
    private String password;
    private Date birthdate;
    private String image;
    private Long parentId;


    public AccountInDTO(){}

    public AccountInDTO(String name, String surname, String email, String password, Date birthdate, String image, Long parentId) {
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.password = password;
        this.birthdate = birthdate;
        this.image = image;
        this.parentId = parentId;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public Date getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(Date birthdate) {
        this.birthdate = birthdate;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
