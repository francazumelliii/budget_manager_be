package it.jac.project_work.budget_manager.repository;

import it.jac.project_work.budget_manager.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.swing.text.html.Option;
import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Long> {
    Optional<Account> findByEmail(String email);
}
