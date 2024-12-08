package it.jac.project_work.budget_manager.repository;

import it.jac.project_work.budget_manager.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.config.annotation.web.oauth2.client.OAuth2ClientSecurityMarker;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Long> {
    @Query("SELECT a FROM Account a WHERE a.email = :email AND a.disabled = 0")
    Optional<Account> findByEmail(String email);

    @Query("SELECT a FROM Account a WHERE a.id = :id")
    Optional<Account> findById(Long id);

    @Query("SELECT SUM(i.amount) FROM Income i WHERE i.account.id = :id AND i.date BETWEEN :startDate AND :endDate")
    Double findMonthlyIncome(@Param("id") Long id, @Param("startDate") LocalDate startDate, @Param("endDate")LocalDate endDate);

    @Query("SELECT SUM(e.amount) FROM Expense e WHERE e.account.id = :id AND e.date BETWEEN :startDate AND :endDate")
    Double findLastMonthExpense(@Param("id") Long id, @Param("startDate") LocalDate startDate, @Param("endDate")LocalDate endDate);


}
