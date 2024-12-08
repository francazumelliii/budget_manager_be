package it.jac.project_work.budget_manager.repository;

import it.jac.project_work.budget_manager.entity.Account;
import it.jac.project_work.budget_manager.entity.Project;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.authentication.jaas.JaasPasswordCallbackHandler;

import java.sql.Timestamp;
import java.util.List;

public interface ProjectRepository extends JpaRepository<Project, Long> {

    @Query("SELECT DISTINCT p FROM Project p LEFT JOIN Share s ON s.project = p WHERE p.account = :account OR s.account = :account ORDER BY p.createdAt")
    public Page<Project> findAllByAccount(Account account, Pageable pageable);
    @Query("SELECT DISTINCT p FROM Project p LEFT JOIN Share s ON s.project = p WHERE p.account = :account OR s.account = :account ORDER BY p.createdAt")
    public List<Project> findAllByAccount(Account account);
    @Query("SELECT p FROM Project p INNER JOIN Share s ON s.project = p WHERE s.account.id = :id AND s.account.parent.id = :parentId ORDER BY p.createdAt")
    public List<Project> findAllChildProjects(@Param("id") Long id, @Param("parentId") Long parentId);



}
