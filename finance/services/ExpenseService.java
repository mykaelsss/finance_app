package com.javastack.spring.finance.services;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.javastack.spring.finance.models.Expense;
import com.javastack.spring.finance.models.User;
import com.javastack.spring.finance.repositories.ExpenseRepo;

@Service
public class ExpenseService {

	private final ExpenseRepo expenseRepo;
	
	public ExpenseService(ExpenseRepo expenseRepo) {
		this.expenseRepo = expenseRepo;
	}
	//Find All
		public List<Expense> allExpenses(){
			return expenseRepo.findAll();
		}
		
		//Find One 
		public Expense oneExpense(Long id) {
			Optional<Expense> optionalExpense = expenseRepo.findById(id);
			if(optionalExpense.isPresent()) {
				return optionalExpense.get();
			}
			else {
				return optionalExpense.orElse(null);
			}
		}
		//Create
		public Expense createExpense(Expense newExpense) {
			return expenseRepo.save(newExpense);
		}
		
		public Expense updateExpense(Expense newExpense) {
			return expenseRepo.save(newExpense);
		}
		
		//Delete
		public void deleteExpense(Long id) {
			Optional<Expense> optionalExpense = expenseRepo.findById(id);
			if(optionalExpense.isPresent()) {
				expenseRepo.deleteById(id);
			}
		}
		//Find by Id
		public Expense findExpense(Long id) {
			Optional<Expense> optionalExpense = expenseRepo.findById(id);
			if(optionalExpense.isPresent()) {
				return optionalExpense.get();
			}else {
				return null;
			}
		}
		
		public List<Expense> userExpenses(User user){
			return expenseRepo.findAllByUser(user);
		}
		
		public double totalAmount(User user, List<Expense> expenses) {
			List<Expense> userExpenses = expenseRepo.findAllByUser(user);
			double fullPrice = 0;
			for( Expense expense : userExpenses) {
				fullPrice += expense.getTotalPrice();
				System.out.println(fullPrice);
			}
			return fullPrice;
		}
		
		public List<Expense> findByCategory(String category, User user) {
			return expenseRepo.findAllByCategoryContainingAndUser(category, user);
		}
		 
		public List<Expense> findByName(String name, User user){
			return expenseRepo.findAllByNameContainingAndUser(name, user);
		}
		
}
