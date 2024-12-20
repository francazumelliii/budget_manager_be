package it.jac.project_work.budget_manager.entity;


import jakarta.persistence.*;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Account {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "name")
    private String name;

    @Column(name = "surname")
    private String surname;

    @Column(name = "email")
    private String email;


    @Column(name = "password")
    private String password;

    @Column(name = "birthdate")
    private Date bithdate;

    @Column(name = "image")
    private String image;
    @Column(name = "disabled")
    private Integer disabled;

    @Column(name = "created_at")
    private Timestamp createdAt;
    @Column(name = "menu_list")
    private String menuList;
    @Column(name = "default_currency")
    private String defaultCurrency;

    @JoinColumn(name = "parent_id")
    @ManyToOne()
    private Account parent;

    @OneToMany(mappedBy = "parent")
    private Set<Account> children;

    @OneToMany(mappedBy = "account")
    private Set<Expense> expenses;

    @OneToMany(mappedBy = "account")
    private Set<Income> incomes;

    @OneToMany(mappedBy = "account")
    private Set<Share> shares;

    @OneToMany(mappedBy = "account")
    private Set<Project> projects;
    @OneToMany(mappedBy = "account")
    private Set<ExpenseSplit> expenseSplits;

    @ElementCollection(fetch = FetchType.EAGER)
    @Enumerated(EnumType.STRING)
    private Set<Role> roles;

    public Account(){
        this.expenseSplits = new HashSet<>();
        this.children = new HashSet<>();
        this.incomes = new HashSet<>();
        this.expenses = new HashSet<>();
        this.shares = new HashSet<>();
        this.roles = new HashSet<>();
        this.projects = new HashSet<>();
        roles.add(Role.USER);
        if(parent == null){
            roles.add(Role.PARENT);
        }

    }

    public Account(long id, String name, String surname, String defaultCurrency, String email, String password, Date bithdate, String image, Timestamp createdAt, Account parent, Set<Account> children, Set<Expense> expenses, Integer disabled, Set<Income> incomes, Set<Share> shares, String menuList, Set<Project> projects, Set<ExpenseSplit> expenseSplits) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.disabled = disabled;
        this.password = password;
        this.bithdate = bithdate;
        this.image = image;
        this.defaultCurrency = defaultCurrency;
        this.createdAt = createdAt;
        this.menuList = menuList;
        this.parent = parent;
        this.children = children;
        this.expenses = expenses;
        this.incomes = incomes;
        this.shares = shares;
        this.expenseSplits = expenseSplits;
        this.projects = projects;
        roles.add(Role.USER);
        if(parent == null){
            roles.add(Role.PARENT);
        }
    }

    public Set<ExpenseSplit> getExpenseSplits() {
        return expenseSplits;
    }

    public void setExpenseSplits(Set<ExpenseSplit> expenseSplits) {
        this.expenseSplits = expenseSplits;
    }

    @PrePersist
    protected void onCreate() {
        this.createdAt = new Timestamp(System.currentTimeMillis());
    }

    public Set<Project> getProjects() {
        return projects;
    }

    public String getDefaultCurrency() {
        return defaultCurrency;
    }

    public Integer getDisabled() {
        return disabled;
    }

    public void setDisabled(Integer disabled) {
        this.disabled = disabled;
    }

    public void setDefaultCurrency(String defaultCurrency) {
        this.defaultCurrency = defaultCurrency;
    }

    public void setProjects(Set<Project> projects) {
        this.projects = projects;
    }

    public long getId() {
        return id;
    }

    public String getMenuList() {
        return menuList;
    }

    public void setMenuList(String menuList) {
        this.menuList = menuList;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Date getBithdate() {
        return bithdate;
    }

    public void setBithdate(Date bithdate) {
        this.bithdate = bithdate;
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

    public Account getParent() {
        return parent;
    }

    public void setParent(Account parent) {
        this.parent = parent;
    }

    public Set<Account> getChildren() {
        return children;
    }

    public void setChildren(Set<Account> children) {
        this.children = children;
    }

    public Set<Expense> getExpenses() {
        return expenses;
    }

    public void setExpenses(Set<Expense> expenses) {
        this.expenses = expenses;
    }

    public Set<Income> getIncomes() {
        return incomes;
    }

    public void setIncomes(Set<Income> incomes) {
        this.incomes = incomes;
    }

    public Set<Share> getShares() {
        return shares;
    }

    public void setShares(Set<Share> shares) {
        this.shares = shares;
    }
}
