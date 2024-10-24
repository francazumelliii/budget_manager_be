package it.jac.project_work.budget_manager.entity;


import jakarta.persistence.*;

import java.sql.Timestamp;

@Entity
public class Income {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JoinColumn(name = "id")
    private long id;

    @JoinColumn(name = "name")
    private String name;

    @JoinColumn(name = "description")
    private String description;

    @JoinColumn(name = "frequency")
    private char frequency;

    @JoinColumn(name = "amount")
    private Double amount;

    @JoinColumn(name = "image")
    private String image;

    @JoinColumn(name = "created_at")
    private Timestamp createdAt;

    @JoinColumn(name = "account_id")
    @ManyToOne()
    private Account account;



    public Income(){}


    public Income(long id, String name, String description, char frequency, Double amount, String image, Timestamp createdAt, Account account) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.frequency = frequency;
        this.amount = amount;
        this.image = image;
        this.createdAt = createdAt;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public char getFrequency() {
        return frequency;
    }

    public void setFrequency(char frequency) {
        this.frequency = frequency;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
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

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }
}
