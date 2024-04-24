package com.spring.lifecare.service;

import java.util.List;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.spring.lifecare.entites.Cart;
import com.spring.lifecare.entites.CartItems;
import com.spring.lifecare.entites.MedicineDetail;
import com.spring.lifecare.entites.User;
import com.spring.lifecare.exception.ServiceException;
import com.spring.lifecare.repository.CartItemsRepository;
import com.spring.lifecare.repository.CartRepository;
import com.spring.lifecare.repository.MedicineRepository;
import com.spring.lifecare.repository.UserRepository;


@Service
public class CartServiceImpl implements CartService {

	@Autowired
	private CartRepository cartRepository;

	@Autowired
	private MedicineRepository medicineRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private CartItemsRepository cartItemsRepository;

	@Override
	@Transactional(rollbackFor = Exception.class)
	public Cart addMedicineToCart(int userId, int medicineId, int quantity) {
		try {
			User users = userRepository.getUserByuserId(userId);
			MedicineDetail medicine = medicineRepository.getMedicineByMedicineId(medicineId);

			if (Objects.nonNull(users) && Objects.nonNull(medicine)) {
				Cart cart = users.getCart();
				if (Objects.isNull(cart)) {
					cart = new Cart();
					cart.setUser(users);
					users.setCart(cart);
				}
				CartItems cartItem = findCartItemByMedicineAndCart(cart, medicine);

				if (Objects.nonNull(cartItem)) {
					cartItem.setQuantity(cartItem.getQuantity() + quantity);
				} else {
					cartItem = new CartItems();
					cartItem.setMedicineDetail(medicine);
					cartItem.setQuantity(quantity);
					cartItem.setCart(cart);
					cart.getCartItems().add(cartItem);
				}
				cartRepository.save(cart);
				updateTotalPrice(cart);
				cartRepository.save(cart);

				return cart;
			}
			return null;
		} catch (ServiceException e) {
			throw new ServiceException("Failed to add medicine to cart");
		}
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public Cart deleteMedicineFromCart(int userId, int medicineId) {
		try {
			User users = userRepository.getUserByuserId(userId);
			MedicineDetail medicine = medicineRepository.getMedicineByMedicineId(medicineId);

			if (Objects.nonNull(users) && Objects.nonNull(medicine) && Objects.nonNull(users.getCart())) {
				Cart cart = users.getCart();

				if (Objects.nonNull(cart)) {
					CartItems cartItemToRemove = findCartItemByMedicineAndCart(cart, medicine);

					if (Objects.nonNull(cartItemToRemove)) {
						cart.getCartItems().remove(cartItemToRemove);
						cartRepository.save(cart);
						updateTotalPrice(cart);

						return cart;
					}
				}
			}
			return null;
		} catch (Exception e) {
			throw new ServiceException("Failed to delete medicine from the cart");
		}
	}

	@Override
	public void incrementCartItemQuantity(int itemId, int userId) {
		CartItems cartItem = cartItemsRepository.findById(itemId);

		if (Objects.nonNull(cartItem)) {
			int currentQuantity = cartItem.getQuantity();
			cartItem.setQuantity(currentQuantity + 1);
			cartItemsRepository.save(cartItem);
			updateTotalPrice(cartItem.getCart());
		}
	}

	@Override
	public void decrementCartItemQuantity(int itemId, int userId) {
		CartItems cartItem = cartItemsRepository.findById(itemId);

		if (Objects.nonNull(cartItem)) {
			int currentQuantity = cartItem.getQuantity();

			if (currentQuantity > 1) {
				cartItem.setQuantity(currentQuantity - 1);
				cartItemsRepository.save(cartItem);
				updateTotalPrice(cartItem.getCart());
			}
		}
	}

	@Override
	public double calculateTotalPrice(int userId) {
		User users = userRepository.getUserByuserId(userId);
		if (Objects.nonNull(users) && Objects.nonNull(users.getCart())) {
			return users.getCart().getTotalPrice();
		}
		return 0;
	}

	@Override
	public CartItems findCartItemByMedicineAndCart(Cart cart, MedicineDetail medicine) {
		for (CartItems item : cart.getCartItems()) {
			if (item.getMedicineDetail().equals(medicine)) {
				return item;
			}
		}
		return null;
	}

	@Override
	public void updateTotalPrice(Cart cart) {
		double totalPrice = 0;
		List<CartItems> cartItems = cart.getCartItems();
		for (CartItems eachItem : cartItems) {
			MedicineDetail medicine = eachItem.getMedicineDetail();
			int quantity = eachItem.getQuantity();
			totalPrice += medicine.getPrice() * quantity;
		}
		cart.setTotalPrice(totalPrice);
		cart.setPayablePrice(totalPrice);
		cartRepository.save(cart);
	}

	@Override
	public List<CartItems> getCartItemsByUserId(int userId) {
		return cartItemsRepository.findByCart_User_userId(userId);

	}

	@Override
	public Cart addToCart(int userId, List<Integer> selectedMedicineIds) {
		User user = userRepository.getUserByuserId(userId);

		if (Objects.nonNull(user)) {
			Cart cart = user.getCart();
			if (Objects.isNull(cart)) {
				cart = new Cart();
				cart.setUser(user);
				user.setCart(cart);
			}

			for (Integer medicineId : selectedMedicineIds) {
				MedicineDetail medicine = medicineRepository.getMedicineByMedicineId(medicineId);
				if (Objects.nonNull(medicine)) {
					CartItems cartItem = findCartItemByMedicineAndCart(cart, medicine);

					if (Objects.nonNull(cartItem)) {
						cartItem.setQuantity(cartItem.getQuantity() + 1);
					} else {
						cartItem = new CartItems();
						cartItem.setMedicineDetail(medicine);
						cartItem.setQuantity(1);
						cartItem.setCart(cart);
						cart.getCartItems().add(cartItem);
					}
				} else {
				}
			}
			cartRepository.save(cart);
			updateTotalPrice(cart);
			userRepository.save(user);

			return cart;
		}

		return null;
	}

	
	@Override
	public void clearCartItemsForUser(int userId) {
		User user = userRepository.getUserByuserId(userId);

		if (Objects.nonNull(user) && Objects.nonNull(user.getCart())) {
			Cart cart = user.getCart();
			cart.getCartItems().clear();
			cartRepository.save(cart);
		}
	}

	@Override
	public double calculatePayablePriceForCardHolder(int userId) {
		User user = userRepository.getUserByuserId(userId);
		if (Objects.isNull(user) || Objects.isNull(user.getCart())) {
			return 0;
		}

		double totalMedicinePrice = calculateTotalPriceInCart(userId);

	

		return totalMedicinePrice;
	}



	@Override
	public double calculateTotalPriceInCart(int userId) {
		User user = userRepository.getUserByuserId(userId);
		List<CartItems> cartItems = user.getCart().getCartItems();

		double totalMedicinePrice = 0.0;

		for (CartItems cartItem : cartItems) {
			MedicineDetail medicine = cartItem.getMedicineDetail();

			if (medicine != null ) {
				totalMedicinePrice += medicine.getPrice() * cartItem.getQuantity();
			}
		}

		return totalMedicinePrice;
	}

}