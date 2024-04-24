package com.spring.lifecare.model;

import java.util.List;

import com.spring.lifecare.entites.Cart;
import com.spring.lifecare.entites.MedicineDetail;
import com.spring.lifecare.entites.User;


public class UserModel {
	private int userId;
	private String userName;
	public UserModel() {
		super();
	}

	public UserModel(int userId, String userName, String email, String password, String confirmPassword,
			String phoneNumber, String role, List<MedicineDetail> medicineDetails, Cart cart) {
		super();
		this.userId = userId;
		this.userName = userName;
		this.email = email;
		this.password = password;
		this.confirmPassword = confirmPassword;
		this.phoneNumber = phoneNumber;
		this.role = role;
		this.medicineDetails = medicineDetails;
		this.cart = cart;
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

	public String getConfirmPassword() {
		return confirmPassword;
	}

	public void setConfirmPassword(String confirmPassword) {
		this.confirmPassword = confirmPassword;
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

	private String email;
	private String password;
	private String confirmPassword;
	private String phoneNumber;
	private String role;
	private List<MedicineDetail> medicineDetails;
	private Cart cart;

	public User getUsersDetail() {
		return new User(userId, userName, email, password, phoneNumber, role, medicineDetails, cart);
	}
}