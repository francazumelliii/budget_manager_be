package it.jac.project_work.budget_manager.repository;

import it.jac.project_work.budget_manager.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    public List<Category> findAll();

}
