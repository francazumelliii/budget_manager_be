package it.jac.project_work.budget_manager.repository;

import it.jac.project_work.budget_manager.entity.Share;
import it.jac.project_work.budget_manager.entity.ShareId;
import org.springframework.data.repository.CrudRepository;

public interface ShareRepository extends CrudRepository<Share, ShareId> {
    void deleteById(ShareId id);
}
