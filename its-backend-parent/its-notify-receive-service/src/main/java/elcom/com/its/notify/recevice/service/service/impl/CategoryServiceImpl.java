package elcom.com.its.notify.recevice.service.service.impl;

import elcom.com.its.notify.recevice.service.config.ApplicationConfig;
import elcom.com.its.notify.recevice.service.model.dto.Category;
import elcom.com.its.notify.recevice.service.model.dto.CategoryDTO;
import elcom.com.its.notify.recevice.service.service.CategoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {
    private static final Logger LOGGER = LoggerFactory.getLogger(CategoryServiceImpl.class);

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public List<Category> findByCatType(String catType) {
        List<Category> categoryList = null;
        //Lay category list
        String urlRequest = ApplicationConfig.ITSCORE_ROOT_URL + "v1.0/its/management/category/filter?catType=" + catType;
        CategoryDTO categoryDTO = restTemplate.getForObject(urlRequest, CategoryDTO.class);
        LOGGER.info("find Category by catType from DBM: {}", categoryDTO);
        if (categoryDTO != null && categoryDTO.getStatus() == HttpStatus.OK.value()) {
            categoryList = categoryDTO.getData();
        }
        return categoryList;
    }
}
