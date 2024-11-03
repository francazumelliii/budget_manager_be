package it.jac.project_work.budget_manager.controller;

import it.jac.project_work.budget_manager.dto.CategoryOutDTO;
import it.jac.project_work.budget_manager.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/categories")
public class CategoryController {

    @Autowired
    public CategoryService categoryService;

    public CategoryController(CategoryService categoryService){
        this.categoryService = categoryService;
    }

    @GetMapping("/all")
    public List<CategoryOutDTO> allCategories(){
        return this.categoryService.getAllCategories();
    }

}
