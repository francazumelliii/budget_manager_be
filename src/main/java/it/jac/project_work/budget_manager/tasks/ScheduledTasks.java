package it.jac.project_work.budget_manager.tasks;

import it.jac.project_work.budget_manager.service.ExpenseService;
import it.jac.project_work.budget_manager.service.IncomeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class ScheduledTasks {

    @Autowired
    private ExpenseService expenseService;
    @Autowired
    private IncomeService incomeService;

    @Scheduled(cron = "0 0 0 * * ?")
    public void runDailyDateUpdates() {
        incomeService.updateIncomeDates();
        expenseService.updateExpenseDates();
    }
}
