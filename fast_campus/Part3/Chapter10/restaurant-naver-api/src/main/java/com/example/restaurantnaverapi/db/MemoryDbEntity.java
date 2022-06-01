package com.example.restaurantnaverapi.db;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MemoryDbEntity {

    // int 로 하면 default가 0 이 들어가게 된다.
    protected Integer index;
}
