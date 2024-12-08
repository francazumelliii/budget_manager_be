package it.jac.project_work.budget_manager.repository;

import it.jac.project_work.budget_manager.entity.Account;
import it.jac.project_work.budget_manager.entity.Expense;
import it.jac.project_work.budget_manager.entity.Project;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.List;

@Transactional
public interface ExpenseRepository extends JpaRepository<Expense,Long> {
    List<Expense> findByAccountAndCreatedAtGreaterThanEqualOrderByDateDesc(Account account, Timestamp date);


    @Query("SELECT e FROM Expense e WHERE e.account.id = :id AND e.date BETWEEN :startDate AND :endDate ORDER BY e.date")
    List<Expense> findAllBetweenDates(@Param("id") Long id, @Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);


    @Query("SELECT e FROM Expense e WHERE e.account.parent.id = :parentId AND e.account.id = :id ORDER BY e.date")
    List<Expense> findAllChildExpenses(@Param("id") Long id, @Param("parentId") Long parentId);

    @Query("SELECT e FROM Expense e WHERE e.account.id = :id ")
    Page<Expense> findAllWithPagination(@Param("id") Long id, Pageable pageable);

    @Query("SELECT e FROM Expense e WHERE e.account.id = :id ")
    Page<Expense> findAllChildWithPagination(@Param("id") Long id, Pageable pageable);

    List<Expense> findAllByFrequencyNot(char frequency);

    @Query("SELECT e FROM Expense e WHERE e.date BETWEEN :today AND :fiveDaysAfter AND e.account.id = :id AND e.frequency != 'S' ORDER BY e.date")
    List<Expense> findAllDueInNextFiveDays(@Param("today") LocalDate today, @Param("fiveDaysAfter") LocalDate fiveDaysAfter, @Param("id") Long id);


    @Modifying
    @Query("DELETE FROM Expense e WHERE e.project = :project")
    void deleteAllByProject(Project project);

}
