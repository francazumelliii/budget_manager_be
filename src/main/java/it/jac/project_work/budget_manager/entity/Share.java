package it.jac.project_work.budget_manager.entity;


import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

import java.sql.Timestamp;

@Entity
public class Share {
    @Id
    @ManyToOne()
    @JoinColumn(name = "account_id")
    private Account account;

    @Id
    @ManyToOne()
    @JoinColumn(name = "project_id")
    private Project project;

    @JoinColumn(name = "joined_at")
    private Timestamp joinedAt;



    public Share(){}

    public Share(Account account, Project project, Timestamp joinedAt) {
        this.account = account;
        this.project = project;
        this.joinedAt = joinedAt;
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
