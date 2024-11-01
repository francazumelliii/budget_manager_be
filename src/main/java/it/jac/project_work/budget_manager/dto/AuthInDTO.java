package it.jac.project_work.budget_manager.dto;

public class AuthInDTO {

    private String email;
    private String password;

    public AuthInDTO(){}
    public AuthInDTO(String email, String password){
        this.email = email;
        this.password = password;
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
}