package it.jac.project_work.budget_manager.dto;

import it.jac.project_work.budget_manager.entity.Account;
import it.jac.project_work.budget_manager.entity.Role;

import java.util.ArrayList;
import java.util.List;

public class AccountOutDTO {
    private Long id;
    private String name;
    private String surname;
    private String email;
    private String birthdate;
    private String image;
    private String menuList;
    private String defaultCurrency;
    private List<AccountOutDTO> children;
    private AccountOutDTO parent;
    private Enum<Role> role;



    public AccountOutDTO(){

    }

    public AccountOutDTO(Long id, String name, String surname,String defaultCurrency, String email, String birthdate, String image, List<AccountOutDTO> children, AccountOutDTO parent, String menuList, Enum<Role> role) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.birthdate = birthdate;
        this.defaultCurrency = defaultCurrency;
        this.image = image;
        this.children = children;
        this.parent = parent;
        this.menuList = menuList;
        this.role = role;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDefaultCurrency() {
        return defaultCurrency;
    }

    public void setDefaultCurrency(String defaultCurrency) {
        this.defaultCurrency = defaultCurrency;
    }

    public String getName() {
        return name;
    }

    public Enum<Role> getRole() {
        return role;
    }

    public void setRole(Enum<Role> role) {
        this.role = role;
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

    public String getMenuList() {
        return menuList;
    }

    public void setMenuList(String menuList) {
        this.menuList = menuList;
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
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setSurname(entity.getSurname());
        dto.setEmail(entity.getEmail());
        dto.setDefaultCurrency(entity.getDefaultCurrency());
        dto.setBirthdate(entity.getBithdate() != null ? entity.getBithdate().toString() : null);
        dto.setImage(entity.getImage());
        dto.setParent(entity.getParent() != null ? buildSingleEntity(entity.getParent()) : null);
        dto.setMenuList(entity.getMenuList());
        dto.setRole(entity.getParent() == null ? Role.PARENT : Role.USER);

        List<AccountOutDTO> childrenList = new ArrayList<>();
        entity.getChildren().forEach(ch -> childrenList.add(AccountOutDTO.buildSingleEntity(ch)));
        dto.setChildren(childrenList);

        return dto;
    }
    private static AccountOutDTO buildSingleEntity(Account account){
        AccountOutDTO dto = new AccountOutDTO();
        dto.setId(account.getId());
        dto.setName(account.getName());
        dto.setSurname(account.getSurname());
        dto.setEmail(account.getEmail());
        dto.setDefaultCurrency(account.getDefaultCurrency());
        dto.setBirthdate(account.getBithdate().toString());
        dto.setImage(account.getImage());
        dto.setMenuList(account.getMenuList());
        dto.setRole(Role.USER);
        return dto;
    }

}
