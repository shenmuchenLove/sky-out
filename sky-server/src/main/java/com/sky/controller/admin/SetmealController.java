package com.sky.controller.admin;

import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.SetmealService;
import com.sky.vo.SetmealVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@Api(tags = "套餐相关接口")
@Slf4j
@RestController
@RequestMapping("/admin/setmeal")
public class SetmealController {

    private final SetmealService setmealService;

    public SetmealController(SetmealService setmealService) {
        this.setmealService = setmealService;
    }

    /**
     * 套餐分页查询
     *
     * @param dto 查询参数
     */
    @ApiOperation("套餐分页查询")
    @GetMapping("/page")
    public Result<PageResult> page(SetmealPageQueryDTO dto) {
        log.info("分页查询：{}", dto);
        PageResult pageResult = setmealService.pageQuery(dto);
        return Result.success(pageResult);
    }


    /**
     * 新增套餐
     *
     * @param setmealDTO 套餐信息
     */
    @ApiOperation("新增套餐")
    @PostMapping
    public Result<String> save(@RequestBody SetmealDTO setmealDTO) {
        log.info("新增套餐：{}", setmealDTO);
        setmealService.save(setmealDTO);
        return Result.success();
    }


    /**
     * 套餐起售或停售
     *
     * @param status 状态
     * @param id     套餐id
     */
    @ApiOperation("套餐起售或停售")
    @PostMapping("/status/{status}")
    public Result<String> setStatus(@PathVariable Integer status, Long id) {
        log.info("套餐起售或停售：{}", id);
        setmealService.startOrStop(status, id);
        return Result.success();
    }


    /**
     * 查询套餐
     *
     * @param id 套餐id
     */
    @ApiOperation("查询套餐")
    @GetMapping("/{id}")
    public Result<SetmealVO> getById(@PathVariable Long id) {
        log.info("查询套餐：{}", id);
        SetmealVO setmealVO = setmealService.getByIdWithDish(id);
        return Result.success(setmealVO);
    }


    /**
     * 修改套餐
     *
     * @param setmealDTO 套餐信息
     */
    @ApiOperation("修改套餐")
    @PutMapping
    public Result<String> update(@RequestBody SetmealDTO setmealDTO) {
        log.info("修改套餐：{}", setmealDTO);
        setmealService.update(setmealDTO);
        return Result.success();
    }

    /**
     * 批量删除套餐
     *
     * @param ids 套餐id
     */
    @ApiOperation("批量删除套餐")
    @DeleteMapping
    public Result<String> delete(@RequestParam("ids") ArrayList<Long> ids) {
        log.info("批量删除套餐：{}", ids);
        setmealService.delete(ids);
        return Result.success();
    }

}
