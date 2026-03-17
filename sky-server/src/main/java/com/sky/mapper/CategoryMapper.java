package com.sky.mapper;

import com.sky.dto.CategoryPageQueryDTO;
import com.sky.entity.Category;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface CategoryMapper {

    /**
     * 分类分页查询
     *
     * @param pageQueryDTO 分类分页查询参数
     * @return 分类列表
     */
    List<Category> page(CategoryPageQueryDTO pageQueryDTO);

    /**
     * 新增分类
     *
     * @param category 分类
     */
    @Insert("insert into category(type, name, sort, status, create_time, update_time, create_user, update_user) " +
            "VALUE (#{type}, #{name}, #{sort}, #{status}, #{createTime}, #{updateTime}, #{createUser}, #{updateUser})")
    void insert(Category category);

    @Select("select id, type, name, sort, status, create_time, update_time, create_user, update_user from category where id = #{id}")
    Category getById(Long id);

    /**
     * 修改分类
     *
     * @param category 分类
     */

    void update(Category category);

    /**
     * 删除分类
     *
     * @param id 分类id
     */
    @Select("delete from category where id = #{id}")
    void deleteById(Long id);

    /**
     * 根据类型查询分类
     *
     * @param type 分类类型
     * @return 分类列表
     */
    @Select("select id, type, name, sort, status, create_time, update_time, create_user, update_user from category where type = #{type}")
    List<Category> getByType(Integer type);
}
