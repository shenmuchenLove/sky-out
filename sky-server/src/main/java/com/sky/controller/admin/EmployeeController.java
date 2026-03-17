package com.sky.controller.admin;

import com.sky.constant.JwtClaimsConstant;
import com.sky.dto.EmployeeDTO;
import com.sky.dto.EmployeeLoginDTO;
import com.sky.dto.PasswordEditDTO;
import com.sky.entity.Employee;
import com.sky.properties.JwtProperties;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.EmployeeService;
import com.sky.utils.JwtUtil;
import com.sky.vo.EmployeeLoginVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * 员工管理
 */

// @Api : 用在类上，例如Controller，表示对类的说明
@Api(tags = "员工相关接口")
@RestController
@RequestMapping("/admin/employee")
@Slf4j
public class EmployeeController {

    private final EmployeeService employeeService;
    private final JwtProperties jwtProperties;

    public EmployeeController(EmployeeService employeeService, JwtProperties jwtProperties) {
        this.employeeService = employeeService;
        this.jwtProperties = jwtProperties;
    }

    /**
     * 登录
     *
     * @param employeeLoginDTO 员工登录信息
     * @return 登录结果
     */
    @ApiOperation("员工登录")
    @PostMapping("/login")
    public Result<EmployeeLoginVO> login(@RequestBody EmployeeLoginDTO employeeLoginDTO) {
        log.info("员工登录：{}", employeeLoginDTO);

        Employee employee = employeeService.login(employeeLoginDTO);

        //登录成功后，生成jwt令牌
        Map<String, Object> claims = new HashMap<>();
        claims.put(JwtClaimsConstant.EMP_ID, employee.getId());
        String token = JwtUtil.createJWT(
                jwtProperties.getAdminSecretKey(),
                jwtProperties.getAdminTtl(),
                claims);

        EmployeeLoginVO employeeLoginVO = EmployeeLoginVO.builder()
                .id(employee.getId())
                .userName(employee.getUsername())
                .name(employee.getName())
                .token(token)
                .build();

        return Result.success(employeeLoginVO);
    }

    /**
     * 退出
     */
    @ApiOperation("员工退出")
    @PostMapping("/logout")
    public Result<String> logout() {
        return Result.success();
    }


    /**
     * 分页查询
     *
     * @param page     页码
     * @param pageSize 页大小
     * @param name     员工姓名
     * @return 分页结果
     */
    @ApiOperation("分页查询")
    @GetMapping("/page")
    public Result<PageResult> page(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer pageSize,
            String name
    ) {
        log.info("分页查询，参数：page={}, pageSize={}, name={}", page, pageSize, name);
        PageResult pageResult = employeeService.page(page, pageSize, name);
        return Result.success(pageResult);
    }


    /**
     * 新增员工
     *
     * @param dto 员工信息
     * @return 新增结果
     */
    @ApiOperation("新增用户")
    @PostMapping
    public Result<String> save(@RequestBody EmployeeDTO dto) {
        log.info("新增用户：{}", dto);
        employeeService.save(dto);
        return Result.success();
    }


    /**
     * 根据id查询员工
     *
     * @param id 员工id
     * @return 员工信息
     */
    @ApiOperation("根据ID查询员工")
    @GetMapping("/{id}")
    public Result<Employee> getById(@PathVariable Long id) {
        log.info("根据员工ID查询员工信息：{}", id);
        Employee employee = employeeService.getById(id);
        return Result.success(employee);
    }

    /**
     * 修改员工信息
     *
     * @param dto 员工信息
     * @return 修改结果
     */
    @ApiOperation("修改员工信息")
    @PutMapping
    public Result<String> update(@RequestBody EmployeeDTO dto) {
        log.info("修改员工信息：{}", dto);
        employeeService.update(dto);
        return Result.success();
    }


    /**
     * 启用禁用员工状态
     *
     * @param status 员工状态
     * @param id     员工ID
     */
    @ApiOperation("启用禁用员工状态")
    @PostMapping("/status/{status}")
    public Result<String> updateStatus(@PathVariable Integer status, Long id) {
        log.info("启用禁用员工状态：{}, {}", status, id);
        employeeService.updateStatus(status, id);
        return Result.success();
    }


    /**
     * 修改当前登录用户密码
     *
     * @param dto 密码dto
     */
    @ApiOperation("修改当前登录用户的密码")
    @PutMapping("/editPassword")
    public Result<String> editPassword(@RequestBody PasswordEditDTO dto) {
        log.info("修改用户密码：{}", dto);
        employeeService.updatePassword(dto);
        return Result.success();
    }

}
