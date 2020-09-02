//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//
package com.kk.common.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import com.kk.common.exception.RRException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class RedisAspect {
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    @Value("${kk.redis.open: false}")
    private boolean open;

    public RedisAspect() {
    }

    @Around("execution(* com.kk.common.utils.RedisUtils.*(..))")
    public Object around(ProceedingJoinPoint point) throws Throwable {
        Object result = null;
        if (this.open) {
            try {
                result = point.proceed();
            } catch (Exception var4) {
                this.logger.error("redis error", var4);
                throw new RRException("Redis服务异常");
            }
        }

        return result;
    }
}
