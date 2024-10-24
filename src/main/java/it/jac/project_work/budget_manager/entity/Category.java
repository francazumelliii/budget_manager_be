package it.jac.project_work.budget_manager.entity;

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity
public class Category {
    @Id
    @JoinColumn(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @JoinColumn(name = "name")
    private String name;

    @JoinColumn(name = "description")
    private String description;

    @JoinColumn(name = "image")
    private String image;

    @OneToMany(mappedBy = "category")
    private Set<Expense> expenses;



    public Category(){
        this.expenses = new HashSet<>();
    }

    public Category(long id, String name, String description, String image, Set<Expense> expenses) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.image = image;
        this.expenses = expenses;
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

    public Set<Expense> getExpenses() {
        return expenses;
    }

    public void setExpenses(Set<Expense> expenses) {
        this.expenses = expenses;
    }
}
