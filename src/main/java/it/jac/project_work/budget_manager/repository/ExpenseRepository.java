package it.jac.project_work.budget_manager.repository;

import it.jac.project_work.budget_manager.entity.Account;
import it.jac.project_work.budget_manager.entity.Expense;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.List;

public interface ExpenseRepository extends JpaRepository<Expense,Long> {
    List<Expense> findByAccountAndCreatedAtGreaterThanEqualOrderByDateDesc(Account account, Timestamp date);


    @Query("SELECT e FROM Expense e WHERE e.account.id = :id AND e.date BETWEEN :startDate AND :endDate ORDER BY e.date")
    List<Expense> findAllBetweenDates(@Param("id") Long id, @Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);


    @Query("SELECT e FROM Expense e WHERE e.account.parent.id = :parentId AND e.account.id = :id")
    List<Expense> findAllChildExpenses(@Param("id") Long id, @Param("parentId") Long parentId);

    @Query("SELECT e FROM Expense e WHERE e.account.id = :id")
    Page<Expense> findAllWithPagination(@Param("id") Long id, Pageable pageable);


}
