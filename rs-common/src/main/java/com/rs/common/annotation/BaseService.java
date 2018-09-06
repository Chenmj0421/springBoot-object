package com.rs.common.annotation;

import java.lang.annotation.*;

/**
 * 初始化继承BaseService的service
 * @author liegou
 * @date
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface BaseService {
}
