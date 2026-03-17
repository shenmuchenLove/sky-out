package com.sky.controller.admin;

import com.sky.dto.CategoryDTO;
import com.sky.dto.CategoryPageQueryDTO;
import com.sky.entity.Category;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.CategoryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 分类管理
 */
@Api(tags = "分类相关接口")
@RequestMapping("/admin/category")
@Slf4j
@RestController
public class CategoryController {


    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    /**
     * 分类分页查询
     *
     * @param pageQueryDTO 参数
     */
    @ApiOperation("分页查询分类列表")
    @GetMapping("/page")
    public Result<PageResult> page(CategoryPageQueryDTO pageQueryDTO) {
        log.info("分页查询：{}", pageQueryDTO);
        PageResult result = categoryService.page(pageQueryDTO);
        return Result.success(result);
    }

    /**
     * 新增分类
     *
     * @param dto 参数
     */
    @ApiOperation("新增分类")
    @PostMapping
    public Result<String> save(@RequestBody CategoryDTO dto) {
        log.info("新增分类：{}", dto);
        categoryService.save(dto);
        return Result.success();
    }


    /**
     * 根据ID查询分类
     *
     * @param id 参数
     */
    @ApiOperation("根据ID查询分类")
    @GetMapping("/{id}")
    public Result<Category> getById(@PathVariable Long id) {
        log.info("根据ID查询分类：{}", id);
        Category category = categoryService.getById(id);
        return Result.success(category);
    }


    /**
     * 修改分类
     *
     * @param dto 参数
     */
    @ApiOperation("修改分类")
    @PutMapping
    public Result<String> update(@RequestBody CategoryDTO dto) {
        log.info("修改分类：{}", dto);
        categoryService.update(dto);
        return Result.success();
    }


    /**
     * 根据ID删除分类
     *
     * @param id 删除的ID
     */
    @ApiOperation("根据ID删除分类")
    @DeleteMapping
    public Result<String> delete(Long id) {
        log.info("删除分类：{}", id);
        categoryService.delete(id);
        return Result.success();
    }


    /**
     * 修改分类状态
     *
     * @param status 状态
     * @param id     分类ID
     */
    @ApiOperation("修改分类状态")
    @PostMapping("/status/{status}")
    public Result<String> setStatus(@PathVariable Integer status, Long id) {
        log.info("设置分类状态：{},{}", status, id);
        categoryService.setStatus(status, id);
        return Result.success();
    }


    /**
     * 根据类型查询分类
     *
     * @param type 分类类型
     */
    @ApiOperation("根据类型查询分类")
    @GetMapping("/list")
    public Result<List<Category>> getByType(Integer type) {
        log.info("查询分类：{}", type);
        List<Category> category = categoryService.getByType(type);
        return Result.success(category);
    }

}
