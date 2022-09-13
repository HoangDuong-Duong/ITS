package com.elcom.its.config.service;

import com.elcom.its.config.model.ListId;
import com.elcom.its.config.model.dto.Category;
import com.elcom.its.config.model.dto.Response;

public interface CategoryService {

    Response getAllCategory();

    Response getAllCategory(Integer page, Integer size, String search);

    Response getAllCategoryByType(String catType);

    Response getCategoryById(String id);

    Response saveCategory(Category category);

    Response updateCategory(Category category);

    Response deleteCategory(String category);

    Response deleteMultiCategory(ListId category);
}
