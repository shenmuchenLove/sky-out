package com.sky.service;

import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.result.PageResult;
import com.sky.vo.DishVO;

public interface DishService {

    /**
     * 新增菜品和对应的口味
     *
     * @param dto 菜单信息
     */
    void saveWithFlavor(DishDTO dto);

    /**
     * 菜品分页查询
     *
     * @param dto 查询参数
     */
    PageResult page(DishPageQueryDTO dto);

    /**
     * 批量删除菜品
     *
     * @param ids 菜品id
     */
    void delete(Long[] ids);

    /**
     * 菜品起售停售
     *
     * @param status 状态
     * @param id     菜品id
     */
    void setStatus(Integer status, Long id);

    /**
     * 根据id查询菜品和对应的口味
     *
     * @param id 菜品id
     */
    DishVO getById(Long id);

    /**
     * 修改菜品和口味
     *
     * @param dto 菜品信息
     */
    void update(DishDTO dto);
}
