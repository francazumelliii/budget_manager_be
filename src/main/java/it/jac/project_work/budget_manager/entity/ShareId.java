package it.jac.project_work.budget_manager.entity;

import jakarta.persistence.Embeddable;

import java.io.Serializable;

@Embeddable
public class ShareId implements Serializable {

    private Long accountId;
    private Long projectId;

    public ShareId() {}

    public ShareId(Long accountId, Long projectId) {
        this.accountId = accountId;
        this.projectId = projectId;
    }

    // Getters e Setters
    public Long getAccountId() {
        return accountId;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    @Override
    public int hashCode() {
        return accountId.hashCode() + projectId.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        ShareId that = (ShareId) obj;
        return accountId.equals(that.accountId) && projectId.equals(that.projectId);
    }
}
