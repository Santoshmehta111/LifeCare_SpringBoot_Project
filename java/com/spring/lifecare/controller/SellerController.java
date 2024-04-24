package com.spring.lifecare.controller;

import java.util.List;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.spring.lifecare.entites.MedicineDetail;
import com.spring.lifecare.entites.User;
import com.spring.lifecare.exception.ServiceException;
import com.spring.lifecare.service.MedicineService;
import com.spring.lifecare.service.UserService;


@Controller
public class SellerController {
	private static final String SELLER = "seller";
	private static final String REDIRECT = "redirect:/";
	private static final String ERRORMESSAGE = "errorMessage";

	@Autowired
	private MedicineService medicineService;
	@Autowired
	private UserService userService;

	@GetMapping("/" + SELLER)
	public String sellerPage(Model model, @RequestParam(required = false) String searchTerm,
			@AuthenticationPrincipal UserDetails userDetails) {
		List<MedicineDetail> medicines;
		User user = userService.findUserByEmail(userDetails.getUsername());
		int sellerId = user.getUserId();

		if (Objects.nonNull(searchTerm) && !searchTerm.isEmpty()) {
			medicines = medicineService.searchMedicines(user, searchTerm);
		} else {
			medicines = medicineService.listAllBySeller(sellerId);
		}

		model.addAttribute("userName", user.getUserName());
		model.addAttribute("searchTerm", searchTerm);
		model.addAttribute("medicine", medicines);
		model.addAttribute("sellerId", sellerId);
	

		return SELLER;
	}

	@PostMapping("/" + SELLER + "/addnew")
	public String handleNewMedicineDetails(@ModelAttribute("medicine") MedicineDetail medicineDetails, Model model,
			@AuthenticationPrincipal UserDetails userDetails) {
		try {
			User user = userService.findUserByEmail(userDetails.getUsername());
			medicineDetails.setUser(user);

			MedicineDetail existingMedicine = medicineService.checkMedicineDetailAlreadyExist(medicineDetails, user);
			if (Objects.isNull(existingMedicine)) {
				medicineService.createMedicineDetail(medicineDetails);

				return REDIRECT + SELLER;
			} else {
				model.addAttribute("message", "Medicine Details Already Exist!");

				return "redirect:/seller?message=Medicine+Details+Already+Exist";
			}
		} catch (ServiceException e) {
			model.addAttribute(ERRORMESSAGE, "Error handling new medicine details" + e.getMessage());
			return REDIRECT + "error";
		}
	}

	@GetMapping("/" + SELLER + "/delete")
	public String delete(@RequestParam("id") Integer id, @AuthenticationPrincipal UserDetails userDetails,
			RedirectAttributes redirectAttributes) {
		try {
			User user = userService.findUserByEmail(userDetails.getUsername());
			medicineService.deleteMedicine(user, id);
		} catch (ServiceException e) {
			redirectAttributes.addFlashAttribute(ERRORMESSAGE, "Error deleting medicine:" + e.getMessage());
		}

		return REDIRECT + SELLER;
	}

	@PostMapping("/" + SELLER + "/updateCount")
	public String changeMedicineCount(@RequestParam("medicineId") int medicineId, @RequestParam("count") int count,
			RedirectAttributes redirectAttributes) {
		try {
			medicineService.updateMedicineCount(medicineId, count);

		} catch (ServiceException e) {
			redirectAttributes.addFlashAttribute(ERRORMESSAGE, "Error Updating Medicine Count");
		}
		return REDIRECT + SELLER;
	}

	@PostMapping("/" + SELLER + "/checkMedicineName")
	@ResponseBody
	public boolean checkMedicineName(@RequestParam String medicineName,
			@AuthenticationPrincipal UserDetails userDetails) {
		User user = userService.findUserByEmail(userDetails.getUsername());
		return !medicineService.checkMedicineExistsForUser(medicineName, user);
	}
}