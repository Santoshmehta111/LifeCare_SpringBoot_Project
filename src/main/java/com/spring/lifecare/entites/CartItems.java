package com.spring.lifecare.entites;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;


@Entity
@Table(name = "cartItems_info")

public class CartItems {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int itemId;
	private int quantity;

	@ManyToOne(targetEntity = MedicineDetail.class)
	@OnDelete(action = OnDeleteAction.CASCADE)
	@JoinColumn(name = "medicineId")
	private MedicineDetail medicineDetail;

	public CartItems(int itemId, int quantity, MedicineDetail medicineDetail, Cart cart) {
		super();
		this.itemId = itemId;
		this.quantity = quantity;
		this.medicineDetail = medicineDetail;
		this.cart = cart;
	}

	public CartItems() {
		super();
	}

	public int getItemId() {
		return itemId;
	}

	public void setItemId(int itemId) {
		this.itemId = itemId;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public MedicineDetail getMedicineDetail() {
		return medicineDetail;
	}

	public void setMedicineDetail(MedicineDetail medicineDetail) {
		this.medicineDetail = medicineDetail;
	}

	public Cart getCart() {
		return cart;
	}

	public void setCart(Cart cart) {
		this.cart = cart;
	}

	@ManyToOne(targetEntity = Cart.class, fetch = FetchType.EAGER)
	@JoinColumn(name = "cartId")
	private Cart cart;
}