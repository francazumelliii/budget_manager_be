package it.jac.project_work.budget_manager.dto;

import it.jac.project_work.budget_manager.entity.Account;

import java.util.ArrayList;
import java.util.List;

public class AccountOutDTO {

    private String name;
    private String surname;
    private String email;
    private String birthdate;
    private String image;
    private List<AccountOutDTO> children;
    private AccountOutDTO parent;



    public AccountOutDTO(){

    }

    public AccountOutDTO(String name, String surname, String email, String birthdate, String image, List<AccountOutDTO> children, AccountOutDTO parent) {
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.birthdate = birthdate;
        this.image = image;
        this.children = children;
        this.parent = parent;
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

    public String getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(String birthdate) {
        this.birthdate = birthdate;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public List<AccountOutDTO> getChildren() {
        return children;
    }

    public void setChildren(List<AccountOutDTO> children) {
        this.children = children;
    }

    public AccountOutDTO getParent() {
        return parent;
    }

    public void setParent(AccountOutDTO parent) {
        this.parent = parent;
    }

    public static AccountOutDTO build(Account entity) {
        AccountOutDTO dto = new AccountOutDTO();
        dto.setName(entity.getName());
        dto.setSurname(entity.getSurname());
        dto.setEmail(entity.getEmail());
        dto.setBirthdate(entity.getBithdate() != null ? entity.getBithdate().toString() : null);
        dto.setImage(entity.getImage());
        dto.setParent(entity.getParent() != null ? buildSingleEntity(entity.getParent()) : null);

        List<AccountOutDTO> childrenList = new ArrayList<>();
        entity.getChildren().forEach(ch -> childrenList.add(AccountOutDTO.buildSingleEntity(ch)));
        dto.setChildren(childrenList);

        return dto;
    }
    private static AccountOutDTO buildSingleEntity(Account account){
        AccountOutDTO dto = new AccountOutDTO();
        dto.setName(account.getName());
        dto.setSurname(account.getSurname());
        dto.setEmail(account.getEmail());
        dto.setBirthdate(account.getBithdate().toString());
        dto.setImage(account.getImage());
        return dto;
    }

}
