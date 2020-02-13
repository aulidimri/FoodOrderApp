package com.upgrad.FoodOrderingApp.api.controller;

import com.upgrad.FoodOrderingApp.api.model.*;
import com.upgrad.FoodOrderingApp.service.businness.AddressService;
import com.upgrad.FoodOrderingApp.service.businness.CategoryService;
import com.upgrad.FoodOrderingApp.service.businness.RestaurantService;
import com.upgrad.FoodOrderingApp.service.entity.*;
import com.upgrad.FoodOrderingApp.service.exception.AuthorizationFailedException;
import com.upgrad.FoodOrderingApp.service.exception.CategoryNotFoundException;
import com.upgrad.FoodOrderingApp.service.exception.InvalidRatingException;
import com.upgrad.FoodOrderingApp.service.exception.RestaurantNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@CrossOrigin
@RestController
@RequestMapping("/")
public class RestaurantController {
    @Autowired
    private RestaurantService restaurantService;

    @Autowired
    private AddressService addressService;

    @Autowired
    private CategoryService categoryService;

    @RequestMapping(method = RequestMethod.GET, path = "/restaurant")
    public ResponseEntity<RestaurantListResponse> getRestaurants() {
        List<Restaurant> restaurantList = restaurantService.getRestaurants();
        List<RestaurantList> restaurantLists = new ArrayList<>();
        for (Restaurant restaurant : restaurantList) {
            Address address = addressService.getAddressById(restaurant.getAddress().getId());
            State state = addressService.getStateById(address.getState().getId());
            RestaurantDetailsResponseAddressState restaurantDetailsResponseAddressState = new RestaurantDetailsResponseAddressState().stateName(state.getStateName()).
                    id(UUID.fromString(state.getUuid()));
            RestaurantDetailsResponseAddress restaurantDetailsResponseAddress = new RestaurantDetailsResponseAddress().city(address.getCity())
                    .flatBuildingName(address.getFlatBuildingNumber()).pincode(address.getPincode()).locality(address.getLocality())
                    .state(restaurantDetailsResponseAddressState).id(UUID.fromString(address.getUuid()));
            List<RestaurantCategory> categories = restaurantService.getCategoriesByRestaurant(restaurant);
            String category = "";
            for(RestaurantCategory c : categories){
                category += c.getCategory().getCategoryName() + ",";
            }
            RestaurantList restaurantList1 = new RestaurantList().restaurantName(restaurant.getRestaurantName()).averagePrice(restaurant.getAveragePriceForTwo()
            ).customerRating(restaurant.getCustomerRating()).id(UUID.fromString(restaurant.getUuid())).photoURL(restaurant.getPhotoUrl()).numberCustomersRated(restaurant.getNumberOfCustomersRated())
                    .address(restaurantDetailsResponseAddress).categories(category);
            restaurantLists.add(restaurantList1);
        }
        RestaurantListResponse restaurantListResponse = new RestaurantListResponse().restaurants(restaurantLists);
        return new ResponseEntity<RestaurantListResponse>(restaurantListResponse, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.GET, path = "/restaurant/name/{reastaurant_name}")
    public ResponseEntity<RestaurantListResponse> getRestaurantsByName(@PathVariable String reastaurant_name) throws RestaurantNotFoundException {
        List<Restaurant> restaurantList = restaurantService.getRestaurantsByName(reastaurant_name);
        List<RestaurantList> restaurantLists = new ArrayList<>();
        for (Restaurant restaurant : restaurantList) {
            Address address = addressService.getAddressById(restaurant.getAddress().getId());
            State state = addressService.getStateById(address.getState().getId());
            RestaurantDetailsResponseAddressState restaurantDetailsResponseAddressState = new RestaurantDetailsResponseAddressState().stateName(state.getStateName()).
                    id(UUID.fromString(state.getUuid()));
            RestaurantDetailsResponseAddress restaurantDetailsResponseAddress = new RestaurantDetailsResponseAddress().city(address.getCity())
                    .flatBuildingName(address.getFlatBuildingNumber()).pincode(address.getPincode()).locality(address.getLocality())
                    .state(restaurantDetailsResponseAddressState).id(UUID.fromString(address.getUuid()));
            List<RestaurantCategory> categories = restaurantService.getCategoriesByRestaurant(restaurant);
            String category = "";
            for(RestaurantCategory c : categories){
                category += c.getCategory().getCategoryName() + ",";
            }
            RestaurantList restaurantList1 = new RestaurantList().restaurantName(restaurant.getRestaurantName()).averagePrice(restaurant.getAveragePriceForTwo()
            ).customerRating(restaurant.getCustomerRating()).id(UUID.fromString(restaurant.getUuid())).photoURL(restaurant.getPhotoUrl()).numberCustomersRated(restaurant.getNumberOfCustomersRated())
                    .address(restaurantDetailsResponseAddress).categories(category);
            restaurantLists.add(restaurantList1);
        }
        RestaurantListResponse restaurantListResponse = new RestaurantListResponse().restaurants(restaurantLists);
        return new ResponseEntity<RestaurantListResponse>(restaurantListResponse, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.GET, path = "/restaurant/category/{category_id}")
    public ResponseEntity<RestaurantListResponse> getRestaurantsByCategory(@PathVariable String category_id) throws CategoryNotFoundException {
        List<Restaurant> restaurantList = restaurantService.getRestaurantsByCategory(category_id);
        List<RestaurantList> restaurantLists = new ArrayList<>();
        for (Restaurant restaurant : restaurantList) {
            Address address = addressService.getAddressById(restaurant.getAddress().getId());
            State state = addressService.getStateById(address.getState().getId());
            RestaurantDetailsResponseAddressState restaurantDetailsResponseAddressState = new RestaurantDetailsResponseAddressState().stateName(state.getStateName()).
                    id(UUID.fromString(state.getUuid()));
            RestaurantDetailsResponseAddress restaurantDetailsResponseAddress = new RestaurantDetailsResponseAddress().city(address.getCity())
                    .flatBuildingName(address.getFlatBuildingNumber()).pincode(address.getPincode()).locality(address.getLocality())
                    .state(restaurantDetailsResponseAddressState).id(UUID.fromString(address.getUuid()));
            List<RestaurantCategory> categories = restaurantService.getCategoriesByRestaurant(restaurant);
            String category = "";
            for(RestaurantCategory c : categories){
                category += c.getCategory().getCategoryName() + ",";
            }
            RestaurantList restaurantList1 = new RestaurantList().restaurantName(restaurant.getRestaurantName()).averagePrice(restaurant.getAveragePriceForTwo()
            ).customerRating(restaurant.getCustomerRating()).id(UUID.fromString(restaurant.getUuid())).photoURL(restaurant.getPhotoUrl()).numberCustomersRated(restaurant.getNumberOfCustomersRated())
                    .address(restaurantDetailsResponseAddress).categories(category);
            restaurantLists.add(restaurantList1);
        }
        RestaurantListResponse restaurantListResponse = new RestaurantListResponse().restaurants(restaurantLists);
        return new ResponseEntity<RestaurantListResponse>(restaurantListResponse, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.GET, path = "/api/restaurant/{restaurant_id}")
    public ResponseEntity<RestaurantDetailsResponse> getRestaurantsByRestaurantId(@PathVariable String restaurant_id) throws RestaurantNotFoundException {
        Restaurant restaurant = restaurantService.getRestaurantsByRestaurantId(restaurant_id);
        Address address = addressService.getAddressById(restaurant.getAddress().getId());
        State state = addressService.getStateById(address.getState().getId());
        RestaurantDetailsResponseAddressState restaurantDetailsResponseAddressState = new RestaurantDetailsResponseAddressState().stateName(state.getStateName()).
                id(UUID.fromString(state.getUuid()));
        RestaurantDetailsResponseAddress restaurantDetailsResponseAddress = new RestaurantDetailsResponseAddress().city(address.getCity())
                .flatBuildingName(address.getFlatBuildingNumber()).pincode(address.getPincode()).locality(address.getLocality())
                .state(restaurantDetailsResponseAddressState).id(UUID.fromString(address.getUuid()));
        List<RestaurantCategory> categories = restaurantService.getCategoriesByRestaurant(restaurant);
        List<CategoryList> categoryLists = new ArrayList<>();
        for(RestaurantCategory rc : categories){
            List<CategoryItem> categoryItems = categoryService.getCategoryItemsById(rc.getCategory());
            List<ItemList> items = new ArrayList<>();
            for(CategoryItem categoryItem : categoryItems){
                Item item = categoryService.getItemsById(categoryItem.getItem().getId());
                ItemList.ItemTypeEnum itemTypeEnum = ItemList.ItemTypeEnum.NON_VEG;
                if(Integer.parseInt(item.getType()) == 0){
                    itemTypeEnum = ItemList.ItemTypeEnum.VEG;
                }
                ItemList itemList = new ItemList().itemName(item.getItemName()).price(item.getPrice())
                        .id(UUID.fromString(item.getUuid())).itemType(itemTypeEnum);
                items.add(itemList);
                CategoryList categoryList = new CategoryList().itemList(items).categoryName(rc.getCategory().getCategoryName())
                        .id(UUID.fromString(rc.getCategory().getUuid()));
                categoryLists.add(categoryList);
            }
        }
        RestaurantDetailsResponse restaurantDetailsResponse = new RestaurantDetailsResponse().categories(categoryLists).restaurantName(restaurant.getRestaurantName())
                .address(restaurantDetailsResponseAddress).averagePrice(restaurant.getAveragePriceForTwo()).customerRating(restaurant.getCustomerRating())
                .photoURL(restaurant.getPhotoUrl()).numberCustomersRated(restaurant.getNumberOfCustomersRated()).id(UUID.fromString(restaurant.getUuid()));
        return new ResponseEntity<RestaurantDetailsResponse>(restaurantDetailsResponse, HttpStatus.OK);
    }


    @RequestMapping(method = RequestMethod.PUT, path = "/api/restaurant/{restaurant_id}")
    public ResponseEntity<RestaurantUpdatedResponse> updateRestaurantDetails(String accessToken, @RequestParam double customerRating, @PathVariable String restaurant_id) throws RestaurantNotFoundException, InvalidRatingException, AuthorizationFailedException {
        Restaurant restaurant = restaurantService.updateRestaurantDetails(accessToken, customerRating, restaurant_id);
        RestaurantUpdatedResponse restaurantUpdatedResponse = new RestaurantUpdatedResponse().id(UUID.fromString(restaurant.getUuid()))
                .status("RESTAURANT RATING UPDATED SUCCESSFULLY");
        return new ResponseEntity<RestaurantUpdatedResponse>(restaurantUpdatedResponse, HttpStatus.OK);
    }
}
