package dh.realestate.repository;

import dh.realestate.model.entity.SubwayEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SubwayRepository extends JpaRepository<SubwayEntity, Long> {

    SubwayEntity findByPlaceNameOrAddressName(String placeName, String addressName);
}
