package com.jzue.study.rcs;

import org.springframework.stereotype.Service;

import java.lang.annotation.*;


@Documented
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ByteWrite {
    String value() default "";
}
