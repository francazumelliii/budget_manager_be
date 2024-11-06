package it.jac.project_work.budget_manager.dto;

import java.util.List;

public class MonthlyStatsPerWeekDTO {
    private String name;
    private List<WeekStatsDTO> series;

    public MonthlyStatsPerWeekDTO() {
    }

    public MonthlyStatsPerWeekDTO(String name, List<WeekStatsDTO> series) {
        this.name = name;
        this.series = series;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<WeekStatsDTO> getSeries() {
        return series;
    }

    public void setSeries(List<WeekStatsDTO> series) {
        this.series = series;
    }
}
