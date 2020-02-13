package com.upgrad.FoodOrderingApp.service.businness;

import com.upgrad.FoodOrderingApp.service.dao.CategoryDao;
import com.upgrad.FoodOrderingApp.service.dao.CustomerAuthDao;
import com.upgrad.FoodOrderingApp.service.dao.RestaurantCategoryDao;
import com.upgrad.FoodOrderingApp.service.dao.RestaurantDao;
import com.upgrad.FoodOrderingApp.service.entity.Category;
import com.upgrad.FoodOrderingApp.service.entity.CustomerAuth;
import com.upgrad.FoodOrderingApp.service.entity.Restaurant;
import com.upgrad.FoodOrderingApp.service.entity.RestaurantCategory;
import com.upgrad.FoodOrderingApp.service.exception.AuthorizationFailedException;
import com.upgrad.FoodOrderingApp.service.exception.CategoryNotFoundException;
import com.upgrad.FoodOrderingApp.service.exception.InvalidRatingException;
import com.upgrad.FoodOrderingApp.service.exception.RestaurantNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class RestaurantService {

    @Autowired
    private RestaurantDao restaurantDao;

    @Autowired
    private RestaurantCategoryDao restaurantCategoryDao;

    @Autowired
    private CategoryDao categoryDao;

    @Autowired
    private CustomerAuthDao customerAuthDao;

    public List<Restaurant> getRestaurants() {
        return restaurantDao.getRestaurants();
    }


    public List<Restaurant> getRestaurantsByName(String reastaurant_name) throws RestaurantNotFoundException {
        if(reastaurant_name.isEmpty()){
            throw new RestaurantNotFoundException("RNF-003", "Restaurant name field should not be empty");
        }
        return restaurantDao.getRestaurantsByName(reastaurant_name);
    }

    public List<RestaurantCategory> getCategoriesByRestaurant(Restaurant restaurant){
        return restaurantCategoryDao.findCategoriesByRestaurant(restaurant);
    }

    public List<Restaurant> getRestaurantsByCategory(String categoryId) throws CategoryNotFoundException {
        if(categoryId.isEmpty()){
            throw new CategoryNotFoundException("CNF-001", "Category id field should not be empty");
        }
        Category category = categoryDao.findByCategoryId(categoryId);
        if(category == null){
            throw new CategoryNotFoundException("CNF-002", "No category by this id");
        }
        List<RestaurantCategory> restaurantCategories =  categoryDao.findRestaurantsByCategoryId(category);
        List<Restaurant> restaurants = new ArrayList<>();
        for(RestaurantCategory restaurantCategory : restaurantCategories){
            Restaurant restaurant = restaurantDao.getRestaurantById(restaurantCategory.getRestaurant().getId());
            restaurants.add(restaurant);
        }
        return restaurants;
    }

    public Restaurant getRestaurantsByRestaurantId(String restaurant_id) throws RestaurantNotFoundException {
        if(restaurant_id.isEmpty()){
            throw new RestaurantNotFoundException("RNF-002", "Restaurant id field should not be empty");
        }
        Restaurant restaurant = restaurantDao.getRestaurantByUuid(restaurant_id);
        if(restaurant == null){
            throw new RestaurantNotFoundException("RNF-001", "No restaurant by this id");
        }
        return restaurantDao.getRestaurantByUuid(restaurant_id);
    }

    public Restaurant getRestaurantByUuid(String uuid){
        return restaurantDao.getRestaurantByUuid(uuid);
    }

    @Transactional
    public Restaurant updateRestaurantDetails(String accessToken, double customerRating, String restaurant_id) throws RestaurantNotFoundException, InvalidRatingException, AuthorizationFailedException {
        CustomerAuth customerAuth = customerAuthDao.findByAccessToken(accessToken);
        if(customerAuth == null){
            throw new AuthorizationFailedException("ATHR-001", "Customer is not Logged in");
        }
        if(customerAuth.getLogoutAt() != null){
            throw new AuthorizationFailedException("ATHR-002", "Customer is logged out. Log in again to access this endpoint.");
        }
        ZonedDateTime zdt = ZonedDateTime.now();
        if(customerAuth.getExpiresAt().isBefore(zdt)){
            throw new AuthorizationFailedException("ATHR-003", "Your session is expired. Log in again to access this endpoint.");
        }

        if(restaurant_id.isEmpty()){
            throw new RestaurantNotFoundException("RNF-002", "Restaurant id field should not be empty");
        }
        Restaurant restaurant = restaurantDao.getRestaurantByUuid(restaurant_id);
        if(restaurant == null){
            throw new RestaurantNotFoundException("RNF-001","No restaurant by this id");
        }
        if(!(customerRating >= 1 && customerRating <=5)){
            throw new InvalidRatingException("IRE-001","Restaurant should be in the range of 1 to 5");
        }
        int noOfCustomersRated =  restaurant.getNumberOfCustomersRated();
        int average = restaurant.getCustomerRating().intValue();
        double newAverageRating = (( noOfCustomersRated * average ) + customerRating) / (noOfCustomersRated+1) ;
        restaurant.setCustomerRating(BigDecimal.valueOf(newAverageRating));
        restaurant.setNumberOfCustomersRated(restaurant.getNumberOfCustomersRated()+ 1);
        restaurantDao.updateRestaurant(restaurant);
        return restaurant;
    }
}
