package com.spring.lifecare.controller;

import java.util.List;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.spring.lifecare.entites.MedicineDetail;
import com.spring.lifecare.entites.User;
import com.spring.lifecare.service.MedicineService;
import com.spring.lifecare.service.UserService;


@Controller
public class BuyerController {
	public static final String BUYER = "buyer";

	@Autowired
	private MedicineService medicineService;

	@Autowired
	private UserService userService;



	@GetMapping("/" + BUYER)
	public String buyerPage(Model model, @RequestParam(required = false) String searchTerm,
			@AuthenticationPrincipal UserDetails userDetails) {
		User user = userService.findUserByEmail(userDetails.getUsername());
		int userId = user.getUserId();
		List<MedicineDetail> medicines;

		if (Objects.nonNull(searchTerm) && !searchTerm.isEmpty()) {
			medicines = medicineService.searchingMedicineFromBuyer(searchTerm);
		} else {
			medicines = medicineService.getAllMedicineDetails();
		}
		model.addAttribute("userId", userId);
		model.addAttribute("searchTerm", searchTerm);
		model.addAttribute("medicines", medicines);
		model.addAttribute("userName", user.getUserName());
		model.addAttribute("userEmail", user.getEmail());

		return BUYER;
	}


}