package com.spring.lifecare.entites;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(name = "user_info", uniqueConstraints = @UniqueConstraint(columnNames = "email"))

public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int userId;
	private String userName;
	private String email;
	private String password;
	private String phoneNumber;
	public User() {
		super();
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public List<MedicineDetail> getMedicineDetails() {
		return medicineDetails;
	}

	public void setMedicineDetails(List<MedicineDetail> medicineDetails) {
		this.medicineDetails = medicineDetails;
	}

	public Cart getCart() {
		return cart;
	}

	public void setCart(Cart cart) {
		this.cart = cart;
	}

	public User(int userId, String userName, String email, String password, String phoneNumber, String role,
			List<MedicineDetail> medicineDetails, Cart cart) {
		super();
		this.userId = userId;
		this.userName = userName;
		this.email = email;
		this.password = password;
		this.phoneNumber = phoneNumber;
		this.role = role;
		this.medicineDetails = medicineDetails;
		this.cart = cart;
	}

	private String role;

	@OneToMany(targetEntity = MedicineDetail.class, cascade = CascadeType.ALL, mappedBy = "user")
	private List<MedicineDetail> medicineDetails;

	@OneToOne(targetEntity = Cart.class, cascade = CascadeType.ALL, mappedBy = "user")
	private Cart cart;
}