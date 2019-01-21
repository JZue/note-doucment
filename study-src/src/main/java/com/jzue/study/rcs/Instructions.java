package com.jzue.study.rcs;

import org.springframework.context.annotation.Import;

import java.lang.annotation.Documented;
import java.lang.annotation.*;

/**
 * @Author: junzexue
 * @Date: 2018/12/24 下午3:45
 * @Description:
 **/
@Target({ ElementType.TYPE, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(InstructionsConfigrationSelector.class)
public @interface Instructions {
    String value() default "";
}
