package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.constant.StatusConstant;
import com.sky.context.BaseContext;
import com.sky.dto.EmployeeDTO;
import com.sky.dto.EmployeeLoginDTO;
import com.sky.dto.PasswordEditDTO;
import com.sky.entity.Employee;
import com.sky.exception.AccountLockedException;
import com.sky.exception.AccountNotFoundException;
import com.sky.exception.PasswordErrorException;
import com.sky.mapper.EmployeeMapper;
import com.sky.result.PageResult;
import com.sky.service.EmployeeService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeMapper employeeMapper;

    public EmployeeServiceImpl(EmployeeMapper employeeMapper) {
        this.employeeMapper = employeeMapper;
    }

    /**
     * 员工登录
     *
     * @param employeeLoginDTO 登录信息
     */
    public Employee login(EmployeeLoginDTO employeeLoginDTO) {
        String username = employeeLoginDTO.getUsername();
        String password = employeeLoginDTO.getPassword();

        //1、根据用户名查询数据库中的数据
        Employee employee = employeeMapper.getByUsername(username);

        //2、处理各种异常情况（用户名不存在、密码不对、账号被锁定）
        if (employee == null) {
            //账号不存在
            throw new AccountNotFoundException(MessageConstant.ACCOUNT_NOT_FOUND);
        }

        //密码比对
        password = DigestUtils.md5DigestAsHex(password.getBytes());
        if (!password.equals(employee.getPassword())) {
            //密码错误
            throw new PasswordErrorException(MessageConstant.PASSWORD_ERROR);
        }

        if (Objects.equals(employee.getStatus(), StatusConstant.DISABLE)) {
            //账号被锁定
            throw new AccountLockedException(MessageConstant.ACCOUNT_LOCKED);
        }

        //3、返回实体对象
        return employee;
    }

    /**
     * 分页查询
     *
     * @param page     页码
     * @param pageSize 页大小
     * @param name     员工姓名
     * @return 分页结果
     */
    @Override
    public PageResult page(Integer page, Integer pageSize, String name) {

        // 1. 设置分页参数
        PageHelper.startPage(page, pageSize);

        // 2. 执行查询语句
        List<Employee> employees = employeeMapper.page(name);

        // 3. 封装对象并返回
        PageResult result = new PageResult();

        Page<Employee> employeePage = (Page<Employee>) employees;
        result.setTotal(employeePage.getTotal());
        result.setRecords(employeePage.getResult());

        return result;
    }

    /**
     * 新增员工
     *
     * @param dto 员工信息
     */
    @Override
    public void save(EmployeeDTO dto) {

        Employee employee = new Employee();

        // 对象属性拷贝
        BeanUtils.copyProperties(dto, employee);

        // 设置账号的状态：默认正常状态 1表示正常 0表示锁定
        employee.setStatus(StatusConstant.ENABLE);

        // 设置密码，对密码进行md5加密处理
        employee.setPassword(DigestUtils.md5DigestAsHex("123456".getBytes()));


        employeeMapper.insert(employee);

    }

    /**
     * 根据id查询员工信息
     *
     * @param id 员工id
     * @return 员工信息
     */
    @Override
    public Employee getById(Long id) {
        return employeeMapper.getById(id);
    }

    /**
     * 修改员工信息
     *
     * @param dto 员工信息
     */
    @Override
    public void update(EmployeeDTO dto) {
        Employee employee = new Employee();

        // 属性拷贝
        BeanUtils.copyProperties(dto, employee);

        // 补全基本信息
        employee.setUpdateTime(LocalDateTime.now());
        employee.setUpdateUser(BaseContext.getCurrentId());

        employeeMapper.update(employee);
    }


    /**
     * 启用禁用员工状态
     *
     * @param status 状态
     * @param id     员工ID
     */
    @Override
    public void updateStatus(Integer status, Long id) {
        Employee employee = new Employee();
        employee.setStatus(status);
        employee.setId(id);
        employee.setUpdateUser(BaseContext.getCurrentId());
        employee.setUpdateTime(LocalDateTime.now());

        employeeMapper.update(employee);
    }


    /**
     * 修改当前登录用户密码
     *
     * @param dto 密码dto
     */
    @Override
    public void updatePassword(PasswordEditDTO dto) {
        // 1. 获取当前登录用户ID
        Long empId = BaseContext.getCurrentId();

        // 2. 根据ID进行查询
        Employee employee = employeeMapper.getById(empId);
        if (employee == null) {
            throw new RuntimeException("当前登录用户不存在");
        }
        // 3. 比对密码
        if (DigestUtils.md5DigestAsHex(dto.getOldPassword().getBytes()).equals(employee.getPassword())) {
            // 需要对于新密码进行加密存储
            String password = DigestUtils.md5DigestAsHex(dto.getNewPassword().getBytes());
            // 封装对象
            Employee emp = new Employee();
            emp.setPassword(password);
            emp.setId(empId);
            emp.setUpdateTime(LocalDateTime.now());
            employeeMapper.update(emp);
        } else {
            throw new RuntimeException("原始密码不正确");
        }
    }
}
