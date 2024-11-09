package it.jac.project_work.budget_manager.dto;

public class PageInDTO {
    private String orderBy;
    private Integer page;
    private Integer size;

    public PageInDTO() {
    }

    public PageInDTO(String orderBy, Integer page, Integer size) {
        this.orderBy = orderBy;
        this.page = page;
        this.size = size;
    }

    public String getOrderBy() {
        return orderBy;
    }

    public void setOrderBy(String orderBy) {
        this.orderBy = orderBy;
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }
}
