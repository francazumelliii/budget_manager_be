package it.jac.project_work.budget_manager;

import it.jac.project_work.budget_manager.tasks.ScheduledTasks;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class BudgetManagerApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(BudgetManagerApplication.class, args);

        ScheduledTasks scheduledTasks = context.getBean(ScheduledTasks.class);

        scheduledTasks.runDailyDateUpdates();
    }
}
