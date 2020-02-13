package com.upgrad.FoodOrderingApp.service.businness;

import com.upgrad.FoodOrderingApp.service.dao.CategoryDao;
import com.upgrad.FoodOrderingApp.service.dao.CategoryItemDao;
import com.upgrad.FoodOrderingApp.service.dao.ItemDao;
import com.upgrad.FoodOrderingApp.service.entity.Category;
import com.upgrad.FoodOrderingApp.service.entity.CategoryItem;
import com.upgrad.FoodOrderingApp.service.entity.Item;
import com.upgrad.FoodOrderingApp.service.exception.CategoryNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {

    @Autowired
    private CategoryDao categoryDao;

    @Autowired
    private CategoryItemDao categoryItemDao;

    @Autowired
    private ItemDao itemDao;

    public List<Category> getCategories() {
        return categoryDao.getAllCategories();
    }

    public Category getCategoryById(String category_id) throws CategoryNotFoundException {
        if(category_id.isEmpty()){
            throw new CategoryNotFoundException("CNF-001", "Category id field should not be empty");
        }
        Category category = categoryDao.findByCategoryId(category_id);
        if(category == null){
            throw new CategoryNotFoundException("CNF-002", "No category by this id");
        }
        return category;

    }

    public List<CategoryItem> getCategoryItemsById(Category category){
        return categoryItemDao.findCategoryItemById(category);
    }


    public Item getItemsById(long id) {
        return itemDao.getItemById(id);
    }
}
