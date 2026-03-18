package com.sky.controller.user;

import com.sky.result.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "C端-店铺相关接口")
@Slf4j
@RestController
@RequestMapping("/user/shop")
public class ShopController {


    private final RedisTemplate<String, Object> redisTemplate;

    public ShopController(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    /**
     * 获取店铺营业状态
     *
     * @return 状态
     */
    @ApiOperation("获取店铺营业状态")
    @RequestMapping("/status")
    public Result<Integer> getStatus() {
        Integer status = (Integer) redisTemplate.opsForValue().get("SHOP_STATUS");
        log.info("获取店铺营业状态：{}", status);
        return Result.success(status);
    }

}
