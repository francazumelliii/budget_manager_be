package it.jac.project_work.budget_manager.service;

import it.jac.project_work.budget_manager.dto.ExpenseOutDTO;
import it.jac.project_work.budget_manager.entity.Account;
import it.jac.project_work.budget_manager.entity.Expense;
import it.jac.project_work.budget_manager.repository.ExpenseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ExpenseService {

    @Autowired
    public final ExpenseRepository expenseRepository;

    public ExpenseService(ExpenseRepository expenseRepository){
        this.expenseRepository = expenseRepository;
    }

    public List<ExpenseOutDTO> getLastMonthExpenses(Account account, Integer limit){

        // get first day of current month
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH,1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        Timestamp firstDayOfMonth = new Timestamp(calendar.getTimeInMillis());
        System.out.println(firstDayOfMonth.toLocalDateTime());
        List<ExpenseOutDTO> list = new ArrayList<>();
        list = this.expenseRepository.findByAccountAndCreatedAtGreaterThanEqual(account, firstDayOfMonth)
                .stream().map(exp -> ExpenseOutDTO.build(exp)).collect(Collectors.toList());

        if(limit == null || limit > list.size()){
            return list;
        }else if(limit <= list.size()){
            return list.subList(0,limit);
        }
        return List.of();


    }

}
