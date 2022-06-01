package com.example.restaurantnaverapi.wishlist.repository;

import com.example.restaurantnaverapi.db.MemoryDbRepositoryAbstract;
import com.example.restaurantnaverapi.wishlist.entity.WishListEntity;
import org.springframework.stereotype.Repository;

@Repository
public class WishListRepository extends MemoryDbRepositoryAbstract<WishListEntity> {


}
