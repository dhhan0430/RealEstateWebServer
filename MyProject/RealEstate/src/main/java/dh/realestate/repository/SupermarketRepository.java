package dh.realestate.repository;

import dh.realestate.model.entity.SupermarketEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SupermarketRepository extends JpaRepository<SupermarketEntity, Long> {

    SupermarketEntity findByPlaceNameOrAddressName(String placeName, String addressName);
}
