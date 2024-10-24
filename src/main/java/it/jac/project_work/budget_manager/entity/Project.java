package it.jac.project_work.budget_manager.entity;

import jakarta.persistence.*;

import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Project {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @JoinColumn(name = "name")
    private String name;

    @JoinColumn(name = "description")
    private String description;

    @JoinColumn(name = "image")
    private String image;

    @JoinColumn(name = "goal_amount")
    private Double goalAmount;

    @JoinColumn(name = "created_at")
    private Timestamp createdAt;

    @OneToMany(mappedBy = "project")
    Set<Expense> expenses;

    @OneToMany(mappedBy = "project")
    private Set<Share> shares;


    public Project(){
        this.shares = new HashSet<>();
        this.expenses = new HashSet<>();

    }

    public Project(long id, String name, String description, String image, Double goalAmount, Timestamp createdAt, Set<Expense> expenses, Set<Share> shares) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.image = image;
        this.goalAmount = goalAmount;
        this.createdAt = createdAt;
        this.expenses = expenses;
        this.shares = shares;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Double getGoalAmount() {
        return goalAmount;
    }

    public void setGoalAmount(Double goalAmount) {
        this.goalAmount = goalAmount;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public Set<Expense> getExpenses() {
        return expenses;
    }

    public void setExpenses(Set<Expense> expenses) {
        this.expenses = expenses;
    }

    public Set<Share> getShares() {
        return shares;
    }

    public void setShares(Set<Share> shares) {
        this.shares = shares;
    }
}
