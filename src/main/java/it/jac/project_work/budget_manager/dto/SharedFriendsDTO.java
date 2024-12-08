package it.jac.project_work.budget_manager.dto;

import it.jac.project_work.budget_manager.entity.Share;

import java.time.LocalDateTime;
import java.util.List;

public class SharedFriendsDTO {
    private String name;
    private String surname;
    private String email;
    private ProjectOutDTO project;
    private LocalDateTime addedAt;

    public SharedFriendsDTO(){}

    public static SharedFriendsDTO build(Share share){
        SharedFriendsDTO dto = new SharedFriendsDTO();
        dto.setName(share.getAccount().getName());
        dto.setSurname(share.getAccount().getSurname());
        dto.setEmail(share.getAccount().getEmail());
        dto.setAddedAt(share.getJoinedAt().toLocalDateTime());
        dto.setProject(ProjectOutDTO.build(share.getProject()));
        return dto;

    }

    public ProjectOutDTO getProject() {
        return project;
    }

    public void setProject(ProjectOutDTO project) {
        this.project = project;
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


    public LocalDateTime getAddedAt() {
        return addedAt;
    }

    public void setAddedAt(LocalDateTime addedAt) {
        this.addedAt = addedAt;
    }
}
