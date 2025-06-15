package com.leosam.web;

import com.leosam.pojo.City;
import com.leosam.service.CityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * @author admin
 */
@Controller
public class IndexController {

    @Autowired
    private CityService cityService;

    @RequestMapping({"/city"})
    @ResponseBody
    public City getCityById(@RequestParam(required = false, defaultValue = "0") long id) {
        return cityService.getById(id);
    }

    @RequestMapping({"/city-all"})
    @ResponseBody
    public List<City> city() {
        return cityService.getCityList();
    }

}