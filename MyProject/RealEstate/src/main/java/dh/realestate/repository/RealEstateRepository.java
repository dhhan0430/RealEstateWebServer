package dh.realestate.repository;

import dh.realestate.model.entity.RealEstateEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RealEstateRepository extends JpaRepository<RealEstateEntity, Long> {

}
