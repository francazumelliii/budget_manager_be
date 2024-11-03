package it.jac.project_work.budget_manager.service;

import it.jac.project_work.budget_manager.dto.CategoryOutDTO;
import it.jac.project_work.budget_manager.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryService {

    @Autowired
    public final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository){
        this.categoryRepository = categoryRepository;
    }

    public List<CategoryOutDTO> getAllCategories(){
        return this.categoryRepository.findAll().stream().map(category -> CategoryOutDTO.build(category)).collect(Collectors.toList());
    }
}
