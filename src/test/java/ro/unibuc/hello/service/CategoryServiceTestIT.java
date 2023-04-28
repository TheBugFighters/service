package ro.unibuc.hello.service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ro.unibuc.hello.dto.CategoryDTO;
import ro.unibuc.hello.entity.CategoryEntity;
import ro.unibuc.hello.repository.CategoryRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
@Tag("IT")
public class CategoryServiceTestIT {

    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    CategoryService categoryService;

    @Test
    void test_addCategory(){
        CategoryDTO categoryDTO= CategoryDTO.builder().categoryName("testCategory2").build();
        categoryService.addCategory(categoryDTO);
        Optional<CategoryEntity> categoryOptional = categoryRepository.findByNameEquals(categoryDTO.getCategoryName());
        assertEquals("testCategory2",categoryOptional.get().getName());
        categoryRepository.deleteById(categoryOptional.get().getId());
    }
    @Test
    void test_getCategories(){
        List<CategoryEntity> categoriesToBeAddedToTheDb = new ArrayList<>();

        categoriesToBeAddedToTheDb.add(new CategoryEntity("Telefoane"));
        categoriesToBeAddedToTheDb.add(new CategoryEntity("Televizoare"));
        categoriesToBeAddedToTheDb.add(new CategoryEntity("Laptopuri"));

        categoryRepository.saveAll(categoriesToBeAddedToTheDb);

        List<CategoryDTO> expectedResult = new ArrayList<>();
        expectedResult.add(getCategoryDTOFromEntity(categoriesToBeAddedToTheDb.get(0)));
        expectedResult.add(getCategoryDTOFromEntity(categoriesToBeAddedToTheDb.get(1)));
        expectedResult.add(getCategoryDTOFromEntity(categoriesToBeAddedToTheDb.get(2)));

        List<CategoryDTO> actualResult= categoryService.getCategories();

        Assertions.assertThat(actualResult).usingRecursiveComparison().isEqualTo(expectedResult);
        categoryRepository.deleteAll(categoriesToBeAddedToTheDb);
    }

    private CategoryDTO getCategoryDTOFromEntity(CategoryEntity category) {
        return CategoryDTO.builder().categoryName(category.getName()).build();
    }

    private CategoryEntity getCategoryEntityFromDTO(CategoryDTO categoryDTO) {
        CategoryEntity categoryEntity = new CategoryEntity();
        categoryEntity.setName(categoryDTO.getCategoryName());
        return categoryEntity;
    }
}

