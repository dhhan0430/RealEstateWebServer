package com.example.springcalculator.component;

import com.example.springcalculator.component.ICalculator;
import com.example.springcalculator.component.MarketApi;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DollarCalculator implements ICalculator {

    private int price = 1;
    private final MarketApi marketApi;

    @Override
    public void init() {

        this.price = marketApi.connect();
    }

    @Override
    public int sum(int x, int y) {

        x *= price;
        y *= price;

        return x + y;
    }

    @Override
    public int minus(int x, int y) {

        x *= price;
        y *= price;

        return x - y;
    }
}
