package com.sky.service;

import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.result.PageResult;
import com.sky.vo.SetmealVO;

import java.util.ArrayList;

public interface SetmealService {
    /**
     * 分页查询
     *
     * @param dto 查询参数
     */
    PageResult pageQuery(SetmealPageQueryDTO dto);

    /**
     * 新增套餐
     *
     * @param setmealDTO 套餐数据
     */
    void save(SetmealDTO setmealDTO);

    /**
     * 套餐起售、停售
     *
     * @param status 状态
     * @param id     套餐id
     */
    void startOrStop(Integer status, Long id);

    /**
     * 根据id查询套餐数据
     *
     * @param id 套餐id
     * @return 套餐数据
     */
    SetmealVO getByIdWithDish(Long id);

    /**
     * 修改套餐
     *
     * @param setmealDTO 套餐数据
     */
    void update(SetmealDTO setmealDTO);

    /**
     * 批量删除套餐
     *
     * @param ids 套餐id
     */
    void delete(ArrayList<Long> ids);
}
