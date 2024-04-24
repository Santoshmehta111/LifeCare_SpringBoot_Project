package com.spring.lifecare.service;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import com.spring.lifecare.entites.MedicineDetail;
import com.spring.lifecare.entites.User;
import com.spring.lifecare.exception.ServiceException;
import com.spring.lifecare.exception.UnauthorizedAccessException;
import com.spring.lifecare.repository.MedicineRepository;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;

@Service
@Transactional
public class MedicineServiceImpl implements MedicineService {

	@Autowired
	private MedicineRepository medicineRepository;

	@Override
	public MedicineDetail createMedicineDetail(@NonNull MedicineDetail medicineDetail) throws ServiceException {
		try {
			MedicineDetail savedMedicineDetail = medicineRepository.save(medicineDetail);

			return savedMedicineDetail;
		} catch (DataIntegrityViolationException e) {

			throw new ServiceException("Failed to create medicine Details. Medicine details already exist.", e);
		} catch (Exception e) {

			throw new ServiceException("Failed to create medicine Details", e);
		}
	}

	@Override
	public MedicineDetail checkMedicineDetailAlreadyExist(MedicineDetail medicineDetail, User user) {
		try {
			String medicineName = medicineDetail.getMedicineName();
			MedicineDetail details = medicineRepository.findByUserAndMedicineName(user, medicineName);

			return details;
		} catch (Exception e) {

			throw new ServiceException("Failed to retrieve medicine Details which already exists", e);
		}
	}

	@Override
	public List<MedicineDetail> getAllMedicineDetails() {
		try {

			return medicineRepository.findAll();
		} catch (Exception e) {

			throw new ServiceException("Failed to get all medicine Details", e);
		}
	}

	@Override
	public List<MedicineDetail> searchMedicine(User user, String searchTerm) {
		try {

			return medicineRepository.findByUserAndMedicineNameContaining(user, searchTerm);
		} catch (Exception e) {

			throw new ServiceException("Failed to search medicine Details", e);
		}
	}

	@Override
	public MedicineDetail getMedicineById(int id) {
		try {

			return medicineRepository.findById(id).orElse(null);
		} catch (Exception e) {

			throw new ServiceException("Failed to get medicine Details by id", e);
		}
	}

	@Override
	public List<MedicineDetail> getAllMedicineDetailsByUserId(int userId) {

		return medicineRepository.findByUser(userId);
	}

	@Override
	public List<MedicineDetail> searchingMedicineFromBuyer(String searchTerm) {
		try {

			return medicineRepository.findByMedicineNameContaining(searchTerm);
		} catch (Exception e) {

			throw new ServiceException("Failed to search medicine Details from buyer", e);
		}
	}

	@Override
	public void deleteMedicine(User user, int medicineId) throws ServiceException {
		Optional<MedicineDetail> medicineOptional = medicineRepository.findById(medicineId);

		if (medicineOptional.isPresent()) {
			MedicineDetail medicine = medicineOptional.get();

			if (medicine.getUser().getUserId() == user.getUserId()) {
				medicineRepository.deleteByMedicineId(medicineId);
			} else {
				throw new UnauthorizedAccessException("Unauthorised access to delete medicine");
			}
		} else {
			throw new ServiceException("Failed to delete medicine Details");
		}
	}

	@Override
	public List<MedicineDetail> listAll() {
		List<MedicineDetail> result = medicineRepository.findAll();
		return result;
	}

	@Override
	public List<MedicineDetail> listAllBySeller(int sellerId) {
		List<MedicineDetail> result = medicineRepository.findAllBySellerId(sellerId);
		return result;
	}



	@Override
	public List<MedicineDetail> searchMedicine(String searchTerm) {
		List<MedicineDetail> result = medicineRepository.searchMedicinesBuyer(searchTerm);
		return result;
	}

	@Override
	public void updateMedicineCount(int medicineId, int count) {
		MedicineDetail medicine = medicineRepository.findById(medicineId)
				.orElseThrow(() -> new EntityNotFoundException("Medicine not found with id " + medicineId));
		medicine.setCount(count);
		medicineRepository.save(medicine);
	}

	

	@Override
	public boolean checkMedicineExistsForUser(String medicineName, User user) {
		return medicineRepository.existsByUserAndMedicineName(user, medicineName);
	}

	@Override
	public List<MedicineDetail> searchMedicines(User user, String searchTerm) {
		List<MedicineDetail> result = medicineRepository.searchMedicines(searchTerm, user.getUserId());
		return result;
	}


}