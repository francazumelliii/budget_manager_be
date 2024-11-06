package it.jac.project_work.budget_manager.dto;

public class WeekStatsDTO {
    private String name;
    private Double value;
    private Object extra;

    public WeekStatsDTO() {
    }

    public WeekStatsDTO(String name, Double value, Object extra) {
        this.name = name;
        this.value = value;
        this.extra = extra;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }

    public Object getExtra() {
        return extra;
    }

    public void setExtra(Object extra) {
        this.extra = extra;
    }
}
