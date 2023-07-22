package com.javastack.spring.finance.models;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Entity
@Table(name="expenses")
public class Expense {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank(message="You must input the name of the item.")
    private String name;
    
    @NotBlank(message="You must input where you purchased this item.")
    private String seller;
    
    @NotNull()
    @Min(value=1, message="The quantity must be at least one.")
    private int quantity;
    
    @NotNull
    @Min(value=1, message="Price must be greater than $1")
    private double price;
    
    private double totalPrice;
    
    @NotBlank(message="Please enter a category.")
    private String category;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="user_id")
    private User user;
    
    
    @Column(updatable=false)
    private Date createdAt;
    private Date updatedAt;
    
	@PrePersist
	   protected void onCreate(){
	   this.createdAt = new Date();
	 }
    @PreUpdate
	   protected void onUpdate(){
       this.updatedAt = new Date();
    }
	public Expense() {
		super();
	}
	


	public Expense(@NotBlank(message = "You must input the name of the item.") String name,
			@NotBlank(message = "You must input where you purchased this item.") String seller,
			@NotNull @Min(value = 1, message = "The quantity must be at least one.") int quantity,
			@NotNull @Min(value = 1, message = "Price must be greater than $1") double price, double totalPrice,
			@NotBlank(message = "Please enter a category.") String category, User user, Date createdAt,
			Date updatedAt) {
		super();
		this.name = name;
		this.seller = seller;
		this.quantity = quantity;
		this.price = price;
		this.totalPrice = totalPrice;
		this.category = category;
		this.user = user;
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getSeller() {
		return seller;
	}
	public void setSeller(String seller) {
		this.seller = seller;
	}
	public int getQuantity() {
		return quantity;
	}
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	public double getPrice() {
		return price;
	}
	public void setPrice(double price) {
		this.price = price;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public Date getCreatedAt() {
		return createdAt;
	}
	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}
	public Date getUpdatedAt() {
		return updatedAt;
	}
	public void setUpdatedAt(Date updatedAt) {
		this.updatedAt = updatedAt;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public double getTotalPrice() {
		return totalPrice;
	}
	public void setTotalPrice(double totalPrice) {
		this.totalPrice = totalPrice;
	}

    
    
}
