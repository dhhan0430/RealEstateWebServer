package dh.realestate.model.entity.listener;

import dh.realestate.model.entity.RealEstateAndSubway;

import javax.persistence.PostRemove;
import javax.persistence.PreRemove;

public class LinkEntityListener {
    @PostRemove
    public void postRemove(Object obj) {
        if (obj instanceof RealEstateAndSubway) {

            var realEstateAndSubway = (RealEstateAndSubway)obj;
            var subwayEntityId = realEstateAndSubway.getSubwayEntity().getId();


        }
    }
}
