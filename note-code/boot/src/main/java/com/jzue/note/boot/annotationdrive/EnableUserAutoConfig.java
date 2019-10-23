package com.jzue.note.boot.annotationdrive;

import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * @author jzue
 * @date 2019/9/26 上午11:19
 **/
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import({AutoConfigurationImortSelectorDemo.class})
public @interface EnableUserAutoConfig {

}
