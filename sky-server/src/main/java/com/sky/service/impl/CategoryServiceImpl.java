package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.context.BaseContext;
import com.sky.dto.CategoryDTO;
import com.sky.dto.CategoryPageQueryDTO;
import com.sky.entity.Category;
import com.sky.mapper.CategoryMapper;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetmealMapper;
import com.sky.result.PageResult;
import com.sky.service.CategoryService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryMapper categoryMapper;

    private final DishMapper dishMapper;

    private final SetmealMapper setmealMapper;

    public CategoryServiceImpl(CategoryMapper categoryMapper, DishMapper dishMapper, SetmealMapper setmealMapper) {
        this.categoryMapper = categoryMapper;
        this.dishMapper = dishMapper;
        this.setmealMapper = setmealMapper;
    }

    /**
     * 分类分页查询
     *
     * @param pageQueryDTO 分类分页查询参数
     * @return 分类分页查询结果
     */
    @Override
    public PageResult page(CategoryPageQueryDTO pageQueryDTO) {


        // 设置分页参数
        PageHelper.startPage(pageQueryDTO.getPage(), pageQueryDTO.getPageSize());

        // 执行查询
        List<Category> list = categoryMapper.page(pageQueryDTO);

        // 封装返回
        Page<Category> page = (Page<Category>) list;
        return new PageResult(page.getTotal(), page.getResult());
    }


    /**
     * 新增分类
     *
     * @param dto 分类参数
     */
    @Override
    public void save(CategoryDTO dto) {

        Category category = new Category();

        // 属性拷贝
        BeanUtils.copyProperties(dto, category);

        category.setCreateTime(LocalDateTime.now());
        category.setUpdateTime(LocalDateTime.now());
        category.setCreateUser(BaseContext.getCurrentId());
        category.setUpdateUser(BaseContext.getCurrentId());
        category.setStatus(1);

        categoryMapper.insert(category);

    }

    /**
     * 根据id查询分类
     *
     * @param id 分类id
     * @return 分类信息
     */
    @Override
    public Category getById(Long id) {
        return categoryMapper.getById(id);
    }

    /**
     * 修改分类
     *
     * @param dto 分类参数
     */
    @Override
    public void update(CategoryDTO dto) {

        Category category = new Category();
        BeanUtils.copyProperties(dto, category);

        category.setUpdateTime(LocalDateTime.now());
        category.setUpdateUser(BaseContext.getCurrentId());
        categoryMapper.update(category);

    }

    /**
     * 删除分类
     *
     * @param id 分类id
     */
    @Override
    public void delete(Long id) {

        // TODO 删除逻辑
        // 1. 查询当前分类是否关联了菜品，如果关联了就不能删除，抛出异常
        Integer count = dishMapper.countByCategoryId(id);
        if (count > 0) {
            // 当前分类下有菜品关联，不能删除
            throw new RuntimeException(MessageConstant.CATEGORY_BE_RELATED_BY_DISH);
        }

        // 2. 查询当前分类是否关联了套餐，如果关联了就抛出异常
        count = setmealMapper.countByCategoryId(id);
        if (count > 0) {
            // 当前分类下有套餐关联，不能删除
            throw new RuntimeException(MessageConstant.CATEGORY_BE_RELATED_BY_SETMEAL);
        }

        // 3. 删除分类
        categoryMapper.deleteById(id);
    }


    /**
     * 修改分类状态
     *
     * @param status 分类状态
     * @param id     分类id
     */
    @Override
    public void setStatus(Integer status, Long id) {
        Category category = Category.builder()
                .status(status)
                .id(id)
                .updateTime(LocalDateTime.now())
                .updateUser(BaseContext.getCurrentId())
                .build();

        categoryMapper.update(category);
    }

    /**
     * 根据类型查询分类
     *
     * @param type 分类类型
     * @return 分类列表
     */
    @Override
    public List<Category> getByType(Integer type) {
        return categoryMapper.getByType(type);
    }
}
