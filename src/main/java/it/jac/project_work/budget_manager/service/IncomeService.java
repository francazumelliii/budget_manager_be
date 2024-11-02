package it.jac.project_work.budget_manager.service;

import it.jac.project_work.budget_manager.dto.IncomeOutDTO;
import it.jac.project_work.budget_manager.entity.Account;
import it.jac.project_work.budget_manager.entity.Income;
import it.jac.project_work.budget_manager.repository.IncomeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class IncomeService {

    @Autowired
    public final IncomeRepository incomeRepository;

    public IncomeService(IncomeRepository incomeRepository){
        this.incomeRepository = incomeRepository;
    }

    public List<IncomeOutDTO> getLastMonthIncomes(Account account, Integer limit){
        List<Income> list = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH,1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        java.util.Date utilDate = calendar.getTime();

        Date sqlDate = new Date(utilDate.getTime());
        System.out.println(sqlDate);
        list = this.incomeRepository.findByAccountAndDateGreaterThanEqual(account,sqlDate);
        if(limit == null || limit > list.size()){
            return list.stream().map(income -> IncomeOutDTO.build(income)).collect(Collectors.toList());
        }
        else if(limit <= list.size()){
            return list.stream().map(income -> IncomeOutDTO.build(income)).collect(Collectors.toList()).subList(0,limit);

        }
        // TODO controller and testing
        return List.of();

    }


}
