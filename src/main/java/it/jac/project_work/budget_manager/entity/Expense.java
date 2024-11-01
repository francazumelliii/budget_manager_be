package it.jac.project_work.budget_manager.entity;


import jakarta.persistence.*;

import java.sql.Timestamp;

@Entity
public class Expense {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "name")
    private String name;

    @Column(name = "amount")
    private Double amount;

    @Column(name = "frequency")
    private char frequency;

    @Column(name = "description")
    private String description;

    @Column(name = "image")
    private String image;

    @Column(name = "created_at")
    private Timestamp createdAt;

    @JoinColumn(name = "category_id")
    @ManyToOne
    private Category category;

    @JoinColumn(name = "project_id")
    @ManyToOne
    private Project project;

    @JoinColumn(name = "account_id")
    @ManyToOne
    private Account account;

    public Expense(){}

    public Expense(long id, String name, Double amount, char frequency, String description, String image, Timestamp createdAt, Category category, Project project, Account account) {
        this.id = id;
        this.name = name;
        this.amount = amount;
        this.frequency = frequency;
        this.description = description;
        this.image = image;
        this.createdAt = createdAt;
        this.category = category;
        this.project = project;
        this.account = account;
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


    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public char getFrequency() {
        return frequency;
    }

    public void setFrequency(char frequency) {
        this.frequency = frequency;
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

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }
}
