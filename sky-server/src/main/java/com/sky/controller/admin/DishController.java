package com.sky.controller.admin;

import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@Api(tags = "菜品管理接口")
@Slf4j
@RestController
@RequestMapping("/admin/dish")
public class DishController {

    private final DishService dishService;

    public DishController(DishService dishService) {
        this.dishService = dishService;
    }

    /**
     * 新增菜品
     *
     * @param dto 菜品信息
     */
    @ApiOperation("保存菜品信息")
    @PostMapping
    public Result<String> save(@RequestBody DishDTO dto) {
        log.info("新增菜品：{}", dto);
        dishService.saveWithFlavor(dto);
        return Result.success();
    }


    /**
     * 菜品分页查询
     *
     * @param dto 查询参数
     */
    @ApiOperation("菜品分页查询")
    @GetMapping("/page")
    public Result<PageResult> page(DishPageQueryDTO dto) {
        log.info("分页查询：{}", dto);
        PageResult pageResult = dishService.page(dto);
        return Result.success(pageResult);
    }


    /**
     * 批量删除菜品
     *
     * @param ids 菜品id数组
     */
    @ApiOperation("批量删除菜品")
    @DeleteMapping
    public Result<String> delete(Long[] ids) {
        log.info("批量删除菜品：{}", Arrays.toString(ids));
        dishService.delete(ids);
        return Result.success();

    }

    /**
     * 菜品起售停售
     *
     * @param status 状态
     * @param id     菜品id
     */
    @ApiOperation("菜品起售停售")
    @PostMapping("/status/{status}")
    public Result<String> setStatus(@PathVariable Integer status, Long id) {
        log.info("菜品起售停售：{},{}", status, id);
        dishService.setStatus(status, id);
        return Result.success();
    }

    /**
     * 根据id查询菜品
     *
     * @param id 菜品id
     */
    @ApiOperation("根据ID查询菜品")
    @GetMapping("/{id}")
    public Result<DishVO> getById(@PathVariable Long id) {
        log.info("查询菜品：{}", id);
        DishVO dishVO = dishService.getById(id);
        return Result.success(dishVO);
    }

    /**
     * 修改菜品
     *
     * @param dto 菜品信息
     */
    @ApiOperation("修改菜品")
    @PutMapping
    public Result<String> update(@RequestBody DishDTO dto) {
        log.info("修改菜品：{}", dto);
        dishService.update(dto);
        return Result.success();
    }


    /**
     * 根据分类id查询菜品
     *
     * @param categoryId 分类id
     */
    @ApiOperation("根据分类id查询菜品")
    @GetMapping("/list")
    public Result< List<DishVO>> list(Integer categoryId) {
        log.info("根据分类id查询菜品：{}", categoryId);
        List<DishVO> dishVO = dishService.list(categoryId);
        return Result.success(dishVO);
    }
}
