package com.leosam.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.leosam.mapper.CityMapper;
import com.leosam.pojo.City;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class CityService extends ServiceImpl<CityMapper, City> {

    private static final Cache<Long, City> CITY_CACHE = Caffeine.newBuilder()
            .recordStats()
            .expireAfterWrite(30, TimeUnit.SECONDS)
            .build();

    public List<City> getCityList() {
        return getBaseMapper().selectList(null);
    }

    public City getById(Long id) {
        if (id <= 0) {
            return null;
        }
        return CITY_CACHE.get(id, (i) -> getBaseMapper().selectById(i));
    }
}
