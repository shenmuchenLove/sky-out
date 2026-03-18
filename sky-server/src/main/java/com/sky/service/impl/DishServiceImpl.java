package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.constant.StatusConstant;
import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.entity.DishFlavor;
import com.sky.exception.DeletionNotAllowedException;
import com.sky.mapper.DishFlavorMapper;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetmealDishMapper;
import com.sky.result.PageResult;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
public class DishServiceImpl implements DishService {

    private final DishMapper dishMapper;

    private final DishFlavorMapper dishFlavorMapper;

    private final SetmealDishMapper setmealDishMapper;

    public DishServiceImpl(DishMapper dishMapper, DishFlavorMapper dishFlavorMapper, SetmealDishMapper setmealDishMapper) {
        this.dishMapper = dishMapper;
        this.dishFlavorMapper = dishFlavorMapper;

        this.setmealDishMapper = setmealDishMapper;
    }


    /**
     * 新增菜品，同时保存对应的口味数据
     *
     * @param dto 菜品信息
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void saveWithFlavor(DishDTO dto) {

        Dish dish = new Dish();
        BeanUtils.copyProperties(dto, dish);
        // 保存菜品数据到菜品表dish
        dishMapper.insert(dish);

        Long dishId = dish.getId();
        // 保存菜品口味信息到菜品口味表dish_flavor
        List<DishFlavor> flavors = dto.getFlavors();
        if (flavors != null && !flavors.isEmpty()) {
            flavors.forEach(flavor -> {
                flavor.setDishId(dishId);
            });
            dishFlavorMapper.insertBatch(flavors);
        }

    }

    /**
     * 菜品分页查询
     *
     * @param dto 查询参数
     */
    @Override
    public PageResult page(DishPageQueryDTO dto) {

        PageHelper.startPage(dto.getPage(), dto.getPageSize());
        List<Dish> list = dishMapper.pageQuery(dto);
        Page<Dish> page = (Page<Dish>) list;
        return new PageResult(page.getTotal(), page.getResult());
    }

    /**
     * 批量删除
     *
     * @param ids 删除的ID数组
     */
    @Override
    public void delete(Long[] ids) {
        // 删除的逻辑 只有状态不是启用的才可以删除
        for (Long id : ids) {
            Dish dish = dishMapper.getById(id);
            if (Objects.equals(dish.getStatus(), StatusConstant.ENABLE)) {
                throw new RuntimeException("当前菜品正在启售中，不能删除");
            }
        }
        //判断当前菜品是否能够删除---是否被套餐关联了？？
        List<Long> setmealIds = setmealDishMapper.getSetmealIdsByDishIds(ids);
        if (setmealIds != null && !setmealIds.isEmpty()) {
            //当前菜品被套餐关联了，不能删除
            throw new DeletionNotAllowedException(MessageConstant.DISH_BE_RELATED_BY_SETMEAL);
        }

        // 批量删除
        dishMapper.delete(ids);

    }

    /**
     * 修改菜品状态
     *
     * @param status 状态
     * @param id     菜品ID
     */
    @Override
    public void setStatus(Integer status, Long id) {
        Dish dish = Dish.builder()
                .status(status)
                .id(id)
                .build();

        dishMapper.update(dish);
    }

    /**
     * 根据ID查询菜品和对应的口味数据
     *
     * @param id 菜品ID
     * @return 菜品数据
     */
    @Override
    public DishVO getById(Long id) {

        // 1. 查询菜品
        Dish dish = dishMapper.getById(id);

        // 2. 查询菜品对应的口味
        List<DishFlavor> dishFlavors = dishMapper.getByDishId(id);

        // 3. 封装数据并返回
        DishVO dishVO = new DishVO();
        BeanUtils.copyProperties(dish, dishVO);
        dishVO.setFlavors(dishFlavors);

        return dishVO;
    }

    /**
     * 修改菜品
     *
     * @param dto 菜品数据
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void update(DishDTO dto) {

        // 修改菜品
        Dish dish = new Dish();
        BeanUtils.copyProperties(dto, dish);
        dishMapper.update(dish);

        // 修改口味 先清除在添加
        dishFlavorMapper.deleteByDishId(dto.getId());

        List<DishFlavor> flavors = dto.getFlavors();
        if (flavors != null && !flavors.isEmpty()) {
            flavors.forEach(flavor -> {
                flavor.setDishId(dto.getId());
            });

            dishFlavorMapper.insertBatch(flavors);
        }

    }

    /**
     * 获取菜品列表
     *
     * @param categoryId 分类ID
     * @return 菜品列表
     */
    @Override
    public  List<DishVO> list(Integer categoryId) {
        return dishMapper.getByCategoryId(categoryId);
    }
}
