package elcom.com.its.notify.recevice.service.service;

import elcom.com.its.notify.recevice.service.model.dto.Category;

import java.util.List;

public interface CategoryService {


    public List<Category> findByCatType(String catType);
}
