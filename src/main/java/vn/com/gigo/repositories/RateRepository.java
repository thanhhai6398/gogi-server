package vn.com.gigo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import vn.com.gigo.entities.Rate;

@Repository
public interface RateRepository extends JpaRepository<Rate, Long>{

}
