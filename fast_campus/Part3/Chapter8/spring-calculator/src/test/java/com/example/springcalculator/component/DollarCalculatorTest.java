package com.example.springcalculator.component;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;

@SpringBootTest
// 굳이 Import 안해도 SpringBootTest annotation 붙이는 순간 모든 Bean 이 불러와짐.
@Import({MarketApi.class, DollarCalculator.class})
public class DollarCalculatorTest {

    // 스프링에서는 Bean으로 관리하기 때문에 Mock이 아니라 MockBean
    @MockBean
    private MarketApi marketApi;

    // Autowired 를 통해 스프링이 관리하고 있는 Bean을 받는다.
    @Autowired
    //private DollarCalculator dollarCalculator;
    private Calculator calculator;

    @Test
    public void dollarCalculatorTest() {

        Mockito.when(marketApi.connect()).thenReturn(3000);
        //dollarCalculator.init();

        //int sum = dollarCalculator.sum(10, 10);
        int sum = calculator.sum(10, 10);
        //int minus = dollarCalculator.minus(10, 10);
        int minus = calculator.minus(10, 10);

        Assertions.assertEquals(60000, sum);
        Assertions.assertEquals(0, minus);
    }
}
