package com.javastack.spring.finance.controllers;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.javastack.spring.finance.models.Expense;
import com.javastack.spring.finance.models.User;
import com.javastack.spring.finance.services.ExpenseService;
import com.javastack.spring.finance.services.UserService;

@Controller
public class MainController {

	private final UserService userServ;
	private final ExpenseService expenseServ;
	
	public MainController(UserService userServ, ExpenseService expenseServ) {
		this.expenseServ = expenseServ;
		this.userServ = userServ;
	}
	
	@GetMapping("/")
	public String redirect() {
		return "/main/redirect.jsp";
	}
	
	@GetMapping("/dashboard")
	public String dashboard(Model model, HttpSession session) {
		if(session.getAttribute("userId") == null ) {
			return "redirect:/";
		}
		User user = userServ.getUser((Long) session.getAttribute("userId"));
		DecimalFormat df = new DecimalFormat("#,###.##");
		String formattedTotal = df.format(expenseServ.totalAmount(user, user.getExpenses()));
		String total = (formattedTotal);
		model.addAttribute("user", user);
		model.addAttribute("expense", new Expense());
		model.addAttribute("userExpenses", expenseServ.userExpenses(user));
		model.addAttribute("total", total);
		return "/main/dashboard.jsp";
	}
	
	@PostMapping("/dashboard")
	public String addExpense(@Valid @ModelAttribute("expense") Expense expense, BindingResult result, Model model, HttpSession session, HttpServletRequest request) {
		User user = userServ.getUser((Long) session.getAttribute("userId"));
		DecimalFormat decf = new DecimalFormat("#,###.##");
		String totalSpendingFormatted = decf.format(expenseServ.totalAmount(user, user.getExpenses()));
		String total = (totalSpendingFormatted);
		String referer = request.getHeader("Referer");
		if(result.hasErrors()) {
			model.addAttribute("user", user);
			model.addAttribute("userExpenses", expenseServ.userExpenses(user));
			model.addAttribute("total", total);
			return "/main/dashboard.jsp";
		}
		DecimalFormat df = new DecimalFormat("###.##");
		String formattedTotal = df.format(expense.getPrice() * expense.getQuantity());
		double totalPrice = Double.parseDouble(formattedTotal);
		expense.setTotalPrice(totalPrice);
		expenseServ.createExpense(expense);
		return "redirect:"+referer;
	}
	
	@GetMapping("/expense/{id}")
	public String oneExpense(@PathVariable("id") Long id, Model model) {
		Expense expense = expenseServ.findExpense(id);
		SimpleDateFormat sdf = new SimpleDateFormat("dd/M/yyyy");
		String date = sdf.format(expense.getCreatedAt());
		model.addAttribute("date", date);
		model.addAttribute(expense);
		return "/expense/expense.jsp";
	}
	@PostMapping("/dashboard/filter/name")
	public String dashboardFilterName(@RequestParam("name") String name, Model model) {
		model.addAttribute("name", name);
		return "redirect:/dashboard/name/"+name;
	}
	@PostMapping("/dashboard/filter/category")
	public String dashboardFilterCategory(@RequestParam("category") String category, Model model) {
		model.addAttribute("category", category);
		return "redirect:/dashboard/"+category;
	}
	@GetMapping("/dashboard/{category}")
	public String filterCategory(@PathVariable("category") String category, Model model,HttpSession session) {
		if(session.getAttribute("userId") == null ) {
			return "redirect:/";
		}
		User user = userServ.getUser((Long) session.getAttribute("userId"));
		List<Expense> filteredExpenses = expenseServ.findByCategory(category,user);
		double filteredTotal = 0.0;
		for( Expense expense : filteredExpenses) {
			filteredTotal += expense.getTotalPrice();
		}
		model.addAttribute("filteredExpenses", filteredExpenses);
		model.addAttribute("category", category);
		DecimalFormat df = new DecimalFormat("#,###.##");
		String formattedTotal = df.format(filteredTotal);
		String total = (formattedTotal);
		model.addAttribute("user", user);
		model.addAttribute("expense", new Expense());
		model.addAttribute("total", total);
		return "/main/filteredDashboard.jsp";
	}
	@GetMapping("/dashboard/name/{name}")
	public String filterName(@PathVariable("name") String name, Model model,HttpSession session) {
		if(session.getAttribute("userId") == null ) {
			return "redirect:/";
		}
		User user = userServ.getUser((Long) session.getAttribute("userId"));
		List<Expense> filteredExpenses = expenseServ.findByName(name,user);
		double filteredTotal = 0.0;
		for( Expense expense : filteredExpenses) {
			filteredTotal += expense.getTotalPrice();
		}
		model.addAttribute("filteredExpenses", filteredExpenses);
		model.addAttribute("name", name);
		DecimalFormat df = new DecimalFormat("#,###.##");
		String formattedTotal = df.format(filteredTotal);
		String total = (formattedTotal);
		model.addAttribute("user", user);
		model.addAttribute("expense", new Expense());
		model.addAttribute("total", total);
		return "/main/filteredNameDashboard.jsp";
	}
	
	@GetMapping("/remove/filter")
	public String removeFilter() {
		return "redirect:/dashboard";
	}
	
	@DeleteMapping("/expense/{id}/delete")
	public String delete(@PathVariable("id") Long id) {
		expenseServ.deleteExpense(id);
		return "redirect:/dashboard";
	}
	
	@PutMapping("/expense/{id}/update")
	public String update(@PathVariable("id") Long id,HttpServletRequest request, @Valid @ModelAttribute("expense") Expense expense, BindingResult result ) {
		if(result.hasErrors()) {
			return "/expense/expense.jsp/";
		}
		double price = expense.getPrice();
		int quantity = expense.getQuantity();
		DecimalFormat df = new DecimalFormat("###.##");
		String formattedTotal = df.format(price * quantity);
		double totalPrice = Double.parseDouble(formattedTotal);
		expense.setPrice(price);
		expense.setQuantity(quantity);
		expense.setTotalPrice(totalPrice);
		System.out.println("price" + price);
		System.out.println("quantity"+quantity);
		System.out.println("total" + totalPrice);
		expenseServ.updateExpense(expense);
		String referer = request.getHeader("Referer");
		return "redirect:" + referer;
	}
	
}
