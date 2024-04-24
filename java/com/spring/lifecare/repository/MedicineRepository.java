package com.spring.lifecare.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.spring.lifecare.entites.MedicineDetail;
import com.spring.lifecare.entites.User;



@Repository
public interface MedicineRepository extends JpaRepository<MedicineDetail, Integer> {
	List<MedicineDetail> findByUserAndMedicineNameContaining(User user, String searchTerm);

	MedicineDetail findByMedicineName(String medicineName);

	MedicineDetail getMedicineByMedicineId(int medicineId);

	MedicineDetail findByUserAndMedicineName(User user, String medicineName);

	@Query(value = "SELECT m FROM MedicineDetail m WHERE m.user.userId=?1")
	List<MedicineDetail> findByUser(int userId);

	List<MedicineDetail> findByMedicineNameContaining(String searchTerm);

	@Modifying
	@Query(value = "DELETE FROM MedicineDetail m WHERE m.medicineId = :medicineId")
	void deleteByMedicineId(int medicineId);

	@Query(value = "SELECT m FROM MedicineDetail m WHERE LOWER(m.medicineName) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
	List<MedicineDetail> searchMedicines(String searchTerm);

	boolean existsByUserAndMedicineName(User user, String medicineName);

	@Query(value = "SELECT m FROM MedicineDetail m WHERE m.user.userId = :sellerId")
	List<MedicineDetail> findAllBySellerId(@Param("sellerId") int sellerId);
	
	@Query(value = "SELECT m FROM MedicineDetail m WHERE m.user.userId = :userId AND LOWER(m.medicineName) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
	List<MedicineDetail> searchMedicines(String searchTerm, int userId);
 
	@Query(value = "SELECT m FROM MedicineDetail m WHERE LOWER(m.medicineName) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
	List<MedicineDetail> searchMedicinesBuyer(String searchTerm);
}
