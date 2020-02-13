package com.upgrad.FoodOrderingApp.api.controller;

import com.upgrad.FoodOrderingApp.api.model.*;
import com.upgrad.FoodOrderingApp.service.businness.CategoryService;
import com.upgrad.FoodOrderingApp.service.entity.Category;
import com.upgrad.FoodOrderingApp.service.entity.CategoryItem;
import com.upgrad.FoodOrderingApp.service.entity.Item;
import com.upgrad.FoodOrderingApp.service.entity.Payment;
import com.upgrad.FoodOrderingApp.service.exception.CategoryNotFoundException;
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
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @RequestMapping(method = RequestMethod.GET, path = "/category")
    public ResponseEntity<CategoriesListResponse> getCategories() {
        List<Category> categories = categoryService.getCategories();
        List<CategoryListResponse> categoryListResponses = new ArrayList<>();
        for (Category category : categories) {
            CategoryListResponse categoryListResponse = new CategoryListResponse().categoryName(category.getCategoryName()).id(UUID.fromString(category.getUuid()));
            categoryListResponses.add(categoryListResponse);
        }
        CategoriesListResponse categoriesListResponse = new CategoriesListResponse().categories(categoryListResponses);
        return new ResponseEntity<CategoriesListResponse>(categoriesListResponse, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.GET, path = "/category/{category_id}")
    public ResponseEntity<CategoryDetailsResponse> getCategoryById(@PathVariable String category_id) throws CategoryNotFoundException {
        Category category = categoryService.getCategoryById(category_id);
        List<CategoryItem> categoryItems = categoryService.getCategoryItemsById(category);
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
        }
        CategoryDetailsResponse categoryDetailsResponse = new CategoryDetailsResponse().categoryName(category.getCategoryName())
                .id(UUID.fromString(category.getUuid())).itemList(items);
        return new ResponseEntity<CategoryDetailsResponse>(categoryDetailsResponse, HttpStatus.OK);



    }
}
