package com.leosam.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.leosam.pojo.City;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CityMapper extends BaseMapper<City> {
    // MyBatis-Plus provides all basic CRUD operations through BaseMapper
    // You can add custom methods here if needed
}