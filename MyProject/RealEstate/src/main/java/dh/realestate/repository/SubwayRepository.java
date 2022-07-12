package dh.realestate.repository;

import dh.realestate.model.entity.SubwayEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SubwayRepository extends JpaRepository<SubwayEntity, Long> {

    SubwayEntity findByPlaceNameAndAddressName(String placeName, String addressName);
}
