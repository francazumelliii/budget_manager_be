package it.jac.project_work.budget_manager.dto;

import java.util.List;

public class ShareProjectInDTO {
    private Long projectId;
    private List<String> emails;

    public ShareProjectInDTO(){

    }

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    public List<String> getEmails() {
        return emails;
    }

    public void setEmails(List<String> emails) {
        this.emails = emails;
    }
}
