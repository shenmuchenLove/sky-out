package com.sky.mapper;

import com.sky.annotation.AutoFill;
import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.Setmeal;
import com.sky.enumeration.OperationType;
import com.sky.vo.SetmealVO;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;

import java.util.ArrayList;
import java.util.List;

@Mapper
public interface SetmealMapper {

    /**
     * 根据分类id查询套餐的数量
     *
     * @param categoryId 分类ID
     */
    @Select("select count(*) from setmeal where category_id = #{categoryId} and status = 1")
    Integer countByCategoryId(Long categoryId);

    /**
     * 套餐分页查询
     *
     * @param dto 查询参数
     */
    List<Setmeal> page(SetmealPageQueryDTO dto);

    /**
     * 新增套餐
     *
     * @param setmeal 套餐参数
     */
    // 主键返回
    @Options(useGeneratedKeys = true, keyProperty = "id")
    @AutoFill(OperationType.INSERT)
    @Insert("insert into setmeal(category_id, name, price, status, description, image, create_time, update_time, create_user, update_user) " +
            "VALUE (#{categoryId},#{name},#{price},#{status},#{description},#{image},#{createTime},#{updateTime},#{createUser},#{updateUser})")
    void insert(Setmeal setmeal);

    /**
     * 修改套餐
     *
     * @param setmeal 套餐参数
     */
    @AutoFill(OperationType.UPDATE)
    void update(Setmeal setmeal);

    /**
     * 根据id查询套餐
     *
     * @param id 套餐id
     */

    SetmealVO getById(Long id);

    /**
     * 批量删除套餐
     *
     * @param ids 套餐id
     */
    void delete(ArrayList<Long> ids);
}
