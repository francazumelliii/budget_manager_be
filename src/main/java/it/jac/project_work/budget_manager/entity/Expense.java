package it.jac.project_work.budget_manager.entity;


import jakarta.persistence.*;

import java.sql.Timestamp;

@Entity
public class Expense {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @JoinColumn(name = "name")
    private String name;

    @JoinColumn(name = "surname")
    private String surname;

    @JoinColumn(name = "amount")
    private Double amount;

    @JoinColumn(name = "frequency")
    private char frequency;

    @JoinColumn(name = "description")
    private String description;

    @JoinColumn(name = "image")
    private String image;

    @JoinColumn(name = "created_at")
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

    public Expense(long id, String name, String surname, Double amount, char frequency, String description, String image, Timestamp createdAt, Category category, Project project, Account account) {
        this.id = id;
        this.name = name;
        this.surname = surname;
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

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
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
