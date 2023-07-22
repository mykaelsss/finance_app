package com.javastack.spring.finance.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.javastack.spring.finance.models.Expense;
import com.javastack.spring.finance.models.User;

@Repository
public interface ExpenseRepo extends CrudRepository<Expense, Long>{
	List<Expense> findAll();
	List<Expense> findAllByUser(User user);
	List<Expense> findAllByCategoryContainingAndUser(String category, User user);
	List<Expense> findAllByNameContainingAndUser(String name, User user);
}
