package it.jac.project_work.budget_manager.repository;

import it.jac.project_work.budget_manager.dto.IncomeOutDTO;
import it.jac.project_work.budget_manager.entity.Account;
import it.jac.project_work.budget_manager.entity.Income;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import org.springframework.data.domain.Pageable;
import java.sql.Date;
import java.util.List;

public interface IncomeRepository extends JpaRepository<Income, Long> {
    List<Income> findByAccountAndDateGreaterThanEqualOrderByDateDesc(Account account, Date date);
    List<Income> findByAccount(Account account);

    @Query("SELECT i FROM Income i WHERE i.account.id = :id")
    Page<Income> findAllWithPagination(@Param("id") Long id, Pageable pageable);

    List<Income> findAllByFrequencyNot(char frequency);

    @Query("SELECT i FROM Income i WHERE i.account.id = :id AND i.account.parent.id = :parentId")
    List<Income> findAllChildIncomes(@Param("id") Long id, @Param("parentId") Long parentId);
}
