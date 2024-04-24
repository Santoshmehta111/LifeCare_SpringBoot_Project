package com.spring.lifecare.entites;

import java.util.ArrayList;
import java.util.List;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;


@Entity
@Table(name = "cart_info")

public class Cart {
	public Cart() {
		super();
	}

	public Cart(int cartId, double totalPrice, double payablePrice, User user, List<CartItems> cartItems) {
		super();
		this.cartId = cartId;
		this.totalPrice = totalPrice;
		this.payablePrice = payablePrice;
		this.user = user;
		this.cartItems = cartItems;
	}

	public int getCartId() {
		return cartId;
	}

	public void setCartId(int cartId) {
		this.cartId = cartId;
	}

	public double getTotalPrice() {
		return totalPrice;
	}

	public void setTotalPrice(double totalPrice) {
		this.totalPrice = totalPrice;
	}

	public double getPayablePrice() {
		return payablePrice;
	}

	public void setPayablePrice(double payablePrice) {
		this.payablePrice = payablePrice;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public List<CartItems> getCartItems() {
		return cartItems;
	}

	public void setCartItems(List<CartItems> cartItems) {
		this.cartItems = cartItems;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int cartId;
	private double totalPrice;
	private double payablePrice;

	@OneToOne(targetEntity = User.class, cascade = { CascadeType.PERSIST, CascadeType.MERGE }, fetch = FetchType.EAGER)
	@JoinColumn(name = "userId")
	private User user;

	@OneToMany(targetEntity = CartItems.class, mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<CartItems> cartItems = new ArrayList<>();
}