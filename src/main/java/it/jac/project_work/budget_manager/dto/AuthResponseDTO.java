package it.jac.project_work.budget_manager.dto;

public class AuthResponseDTO {
    private AccountOutDTO user;
    private String jwt;


    public AuthResponseDTO(){}

    public AuthResponseDTO(AccountOutDTO user, String jwt) {
        this.user = user;
        this.jwt = jwt;
    }

    public AccountOutDTO getUser() {
        return user;
    }

    public void setUser(AccountOutDTO user) {
        this.user = user;
    }

    public String getJwt() {
        return jwt;
    }

    public void setJwt(String jwt) {
        this.jwt = jwt;
    }
}
