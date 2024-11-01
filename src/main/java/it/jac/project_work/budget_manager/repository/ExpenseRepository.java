package it.jac.project_work.budget_manager.repository;

import it.jac.project_work.budget_manager.entity.Account;
import it.jac.project_work.budget_manager.entity.Expense;
import org.springframework.data.jpa.repository.JpaRepository;

import java.sql.Timestamp;
import java.util.List;

public interface ExpenseRepository extends JpaRepository<Expense,Long> {
    List<Expense> findByAccountAndCreatedAtAfter(Account account, Timestamp date);
}
