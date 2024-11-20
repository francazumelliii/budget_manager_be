package it.jac.project_work.budget_manager.entity;

import jakarta.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;

@Entity
public class Share {

    @EmbeddedId
    private ShareId id;

    @ManyToOne
    @MapsId("accountId")
    @JoinColumn(name = "account_id")
    private Account account;

    @ManyToOne
    @MapsId("projectId")
    @JoinColumn(name = "project_id")
    private Project project;

    @Column(name = "joined_at")
    private Timestamp joinedAt;

    public Share() {}

    public Share(Account account, Project project, Timestamp joinedAt) {
        this.id = new ShareId(account.getId(), project.getId());
        this.account = account;
        this.project = project;
        this.joinedAt = joinedAt;
    }

    public ShareId getId() {
        return id;
    }

    public void setId(ShareId id) {
        this.id = id;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public Timestamp getJoinedAt() {
        return joinedAt;
    }

    public void setJoinedAt(Timestamp joinedAt) {
        this.joinedAt = joinedAt;
    }
}

