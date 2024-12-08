package it.jac.project_work.budget_manager.repository;

import it.jac.project_work.budget_manager.entity.Account;
import it.jac.project_work.budget_manager.entity.Share;
import it.jac.project_work.budget_manager.entity.ShareId;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ShareRepository extends CrudRepository<Share, ShareId> {
    void deleteById(ShareId id);

    List<Share> findByProjectId(Integer projectId);
    @Query("SELECT s From Share s INNER JOIN Project p ON s.project = p WHERE p.account = :account")
    List<Share> findAllByAccount(Account account);


}
