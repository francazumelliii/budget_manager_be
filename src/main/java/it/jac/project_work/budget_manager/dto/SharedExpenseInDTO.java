package it.jac.project_work.budget_manager.dto;

import java.util.List;

public class SharedExpenseInDTO {
    private List<SimpleAccountOutDTO> shared;
    private Long expenseId;
    private Long projectId;

    public SharedExpenseInDTO(){}

    public List<SimpleAccountOutDTO> getShared() {
        return shared;
    }

    public void setShared(List<SimpleAccountOutDTO> shared) {
        this.shared = shared;
    }

    public Long getExpenseId() {
        return expenseId;
    }

    public void setExpenseId(Long expenseId) {
        this.expenseId = expenseId;
    }

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }
}
