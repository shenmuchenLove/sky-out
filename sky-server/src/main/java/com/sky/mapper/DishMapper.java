package com.sky.mapper;

import com.sky.annotation.AutoFill;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.entity.DishFlavor;
import com.sky.enumeration.OperationType;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface DishMapper {

    /**
     * 根据分类id查询菜品数量
     *
     * @param categoryId 分类id
     */
    @Select("select count(*) from dish where category_id = #{categoryId}")
    Integer countByCategoryId(Long categoryId);

    /**
     * 新增菜品数据
     *
     * @param dish 菜品数据
     */
    // 主键返回 useGeneratedKeys = true：表示使用数据库自动生成的主键
    // keyProperty = "id"：指定将生成的主键值映射到 Dish 对象的 id 属性中
    @Options(useGeneratedKeys = true, keyProperty = "id")
    @AutoFill(OperationType.INSERT)
    @Insert("insert into dish(name, category_id, price, image, description, create_time, update_time, create_user, update_user) " +
            "VALUE(#{name}, #{categoryId}, #{price}, #{image}, #{description}, #{createTime}, #{updateTime}, #{createUser}, #{updateUser})")
    void insert(Dish dish);

    /**
     * 菜品分页查询
     *
     * @param dto 查询参数
     */
    List<Dish> pageQuery(DishPageQueryDTO dto);

    /**
     * 根据id查询菜品数据
     *
     * @param id 菜品id
     */
    @Select("select * from dish where id = #{id}")
    Dish getById(Long id);

    /**
     * 根据id删除菜品数据
     *
     * @param ids 菜品id
     */
    void delete(Long[] ids);

    /**
     * 修改菜品数据
     *
     * @param dish 菜品数据
     */
    @AutoFill(OperationType.UPDATE)
    void update(Dish dish);

    /**
     * 根据菜品id查询菜品和口味数据
     *
     * @param id 菜品id
     */
    @Select("select * from dish_flavor where dish_id = #{id}")
    List<DishFlavor> getByDishId(Long id);
}
