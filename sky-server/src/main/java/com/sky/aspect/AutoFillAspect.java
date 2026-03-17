package com.sky.aspect;

import com.sky.annotation.AutoFill;
import com.sky.constant.AutoFillConstant;
import com.sky.context.BaseContext;
import com.sky.enumeration.OperationType;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.time.LocalDateTime;

@Slf4j
@Aspect
@Component
public class AutoFillAspect {


    // 切入点  -- > 对哪些方法进行增强
    @Pointcut("execution(* com.sky.mapper.*.*(..)) && @annotation(com.sky.annotation.AutoFill)")
    public void autoFillPointCut() {
    }


    // 前置通知
    @Before("autoFillPointCut()")
    public void before(JoinPoint joinPoint) throws Exception {

        log.info("开始进行公共字段的自动填充");

        // 1. 获取被拦截到的方法上的注解类型
        // 1.1 方法签名对象
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        // 1.2 获取注解对象
        AutoFill autoFill = signature.getMethod().getAnnotation(AutoFill.class);
        // 1.3 获取操作类型
        OperationType value = autoFill.value();

        // 2. 获取到被拦截的方法的参数 - 实体参数
        Object[] args = joinPoint.getArgs();
        if (args == null || args.length == 0) {
            return;
        }
        Object object = args[0];

        // 3. 准备赋值的数据
        // 当前时间
        LocalDateTime dateTime = LocalDateTime.now();
        // 用户
        Long currentId = BaseContext.getCurrentId();

        // 4. 根据不同的类型，为对应的字段赋值 - 反射

        if (value == OperationType.INSERT) {
            // 插入操作 -- 创建时间、修改时间、创建人、修改人
            Method setUpdateTime = object.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_TIME, LocalDateTime.class);
            Method setCreateTime = object.getClass().getDeclaredMethod(AutoFillConstant.SET_CREATE_TIME, LocalDateTime.class);
            Method setUpdateUser = object.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_USER, Long.class);
            Method setCreateUser = object.getClass().getDeclaredMethod(AutoFillConstant.SET_CREATE_USER, Long.class);

            // 赋值
            setUpdateTime.invoke(object, dateTime);
            setCreateTime.invoke(object, dateTime);
            setUpdateUser.invoke(object, currentId);
            setCreateUser.invoke(object, currentId);

        } else if (value == OperationType.UPDATE) {
            // 更新操作 -- 更新人、更新时间
            Method setUpdateTime = object.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_TIME, LocalDateTime.class);
            Method setUpdateUser = object.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_USER, Long.class);
            // 赋值
            setUpdateTime.invoke(object, dateTime);
            setUpdateUser.invoke(object, currentId);
        }

    }


}
