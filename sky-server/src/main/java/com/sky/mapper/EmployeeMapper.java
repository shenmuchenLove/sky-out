package com.sky.mapper;

import com.sky.entity.Employee;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface EmployeeMapper {

    /**
     * 根据用户名查询员工
     *
     * @param username 用户名
     * @return 员工对象
     */
    @Select("select * from employee where username = #{username}")
    Employee getByUsername(String username);

    /**
     * 员工分页查询
     *
     * @param name 员工姓名
     * @return 员工列表
     */
    List<Employee> page(String name);

    /**
     * 插入员工数据
     *
     * @param employee 员工数据
     */
    void insert(Employee employee);

    /**
     * 根据id查询员工
     *
     * @param id 员工id
     * @return 员工对象
     */
    @Select("select id, name, username, password, phone, sex, id_number, status, create_time, update_time, create_user, update_user from employee where id = #{id}")
    Employee getById(Long id);

    /**
     * 更新员工数据
     *
     * @param employee 员工数据
     */
    void update(Employee employee);
}
