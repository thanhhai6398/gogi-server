package vn.com.gigo.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import vn.com.gigo.entities.Voucher;

@Repository
public interface VoucherRepository extends JpaRepository<Voucher, Long>{
	Voucher findOneById(Long id);
	
	Voucher findOneByCode(String code);

	@Query(value="SELECT * FROM vouchers WHERE id NOT IN (SELECT voucher_id FROM vouchers_products WHERE account_id = ?1)", nativeQuery=true)
	List<Voucher> getVoucherByAccountId(Long accountId);
}