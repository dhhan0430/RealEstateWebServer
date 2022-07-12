package dh.realestate.repository;

import dh.realestate.model.entity.RealEstateAndSubway;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RealEstateAndSubwayRepository extends JpaRepository<RealEstateAndSubway, Long> {

    RealEstateAndSubway findByRealEstateAndSubway(String realEstate, String subway);
}
