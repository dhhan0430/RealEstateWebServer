package dh.realestate.repository;

import dh.realestate.model.entity.RealEstateEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RealEstateRepository extends JpaRepository<RealEstateEntity, Long> {

    RealEstateEntity findByNameAndAddressAndTypeAndAreaForExclusiveUse(
            String name, String address, String type, double areaForExclusiveUse
    );

}
