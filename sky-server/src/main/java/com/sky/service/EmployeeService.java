package com.sky.service;

import com.sky.dto.EmployeeDTO;
import com.sky.dto.EmployeeLoginDTO;
import com.sky.dto.PasswordEditDTO;
import com.sky.entity.Employee;
import com.sky.result.PageResult;

public interface EmployeeService {

    /**
     * 员工登录
     *
     * @param employeeLoginDTO 登录信息
     */
    Employee login(EmployeeLoginDTO employeeLoginDTO);

    /**
     * 分页查询
     *
     * @param page     页码
     * @param pageSize 页大小
     * @param name     员工姓名
     * @return 分页结果
     */
    PageResult page(Integer page, Integer pageSize, String name);

    /**
     * 新增员工
     *
     * @param dto 新增员工信息
     */
    void save(EmployeeDTO dto);

    /**
     * 根据id查询员工信息
     *
     * @param id 员工id
     * @return 员工信息
     */
    Employee getById(Long id);

    /**
     * 修改员工信息
     *
     * @param dto 修改员工信息
     */
    void update(EmployeeDTO dto);

    /**
     * 启用禁用员工状态
     *
     * @param status 状态
     * @param id 员工ID
     */
    void updateStatus(Integer status, Long id);

    /**
     * 修改当前登录用户密码
     *
     * @param dto 密码dto
     */
    void updatePassword(PasswordEditDTO dto);
}
