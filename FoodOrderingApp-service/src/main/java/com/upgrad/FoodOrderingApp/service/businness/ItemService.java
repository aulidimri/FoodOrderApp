package com.upgrad.FoodOrderingApp.service.businness;

import com.upgrad.FoodOrderingApp.service.dao.RestaurantDao;
import com.upgrad.FoodOrderingApp.service.dao.RestaurantItemDao;
import com.upgrad.FoodOrderingApp.service.entity.Item;
import com.upgrad.FoodOrderingApp.service.entity.Restaurant;
import com.upgrad.FoodOrderingApp.service.entity.RestaurantItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ItemService {

    @Autowired
    private RestaurantDao restaurantDao;

    @Autowired
    private RestaurantItemDao restaurantItemDao;

    public List<Item> getPopularItems(String restaurant_id) {
        Restaurant restaurant = restaurantDao.getRestaurantByUuid(restaurant_id);
        List<RestaurantItem> restaurantItems = restaurantItemDao.getItemsByRestaurant(restaurant.getId());
        return null;
    }
}
