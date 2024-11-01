package it.jac.project_work.budget_manager.repository;

import it.jac.project_work.budget_manager.dto.IncomeOutDTO;
import it.jac.project_work.budget_manager.entity.Account;
import it.jac.project_work.budget_manager.entity.Income;
import org.springframework.data.jpa.repository.JpaRepository;

import java.sql.Date;
import java.util.List;

public interface IncomeRepository extends JpaRepository<Income, Long> {
    List<Income> findByAccountAndDateAfter(Account account, Date date);
}
