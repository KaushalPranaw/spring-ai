package com.spring.multimodel;

import com.spring.multimodel.tools.WeatherTool;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class SpringAiMultimodelApplicationTests {


    @Autowired
    private WeatherTool weatherTool;

    @Test
    void getWeatherTest(){
        var response=weatherTool.getWeather("Delhi India");
        System.out.println(response);
    }

}
