package it.jac.project_work.budget_manager.dto;

public class AccountPatchDTO {
    private String name;
    private String surname;
    private String defaultCurrency;

    public AccountPatchDTO(){}

    public AccountPatchDTO(String name, String surname, String defaultCurrency) {
        this.name = name;
        this.surname = surname;
        this.defaultCurrency = defaultCurrency;
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

    public String getDefaultCurrency() {
        return defaultCurrency;
    }

    public void setDefaultCurrency(String defaultCurrency) {
        this.defaultCurrency = defaultCurrency;
    }
}
