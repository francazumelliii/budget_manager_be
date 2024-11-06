package it.jac.project_work.budget_manager.dto;

public class MonthlyStatsDTO {
    private Double totalExpense;
    private Double totalIncome;

    public MonthlyStatsDTO() {
    }

    public MonthlyStatsDTO(Double totalExpense, Double totalIncome) {
        this.totalExpense = totalExpense;
        this.totalIncome = totalIncome;
    }

    public Double getTotalExpense() {
        return totalExpense;
    }

    public void setTotalExpense(Double totalExpense) {
        this.totalExpense = totalExpense;
    }

    public Double getTotalIncome() {
        return totalIncome;
    }

    public void setTotalIncome(Double totalIncome) {
        this.totalIncome = totalIncome;
    }
}
