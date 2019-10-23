package com.jzue.note.boot.annotationdrive;

import org.springframework.context.annotation.ImportSelector;
import org.springframework.core.io.support.SpringFactoriesLoader;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * @author jzue
 * @date 2019/9/26 上午11:20
 **/
public class AutoConfigurationImortSelectorDemo implements ImportSelector {

    public String[] selectImports(AnnotationMetadata annotationMetadata) {
        //获取EnableAutoDemoConfig所在的META-INF/spring.fctories里面的EnableDemoAutoConfig.class类全名为key对应的值的Class全名
        List<String> configurations =SpringFactoriesLoader.loadFactoryNames(this.getSpringFactoriesLoaderFactoryClass(),null);
        Assert.notEmpty(configurations, "No auto configuratizon classes found in META-INF/spring.factories. If you are using a custom packaging, make sure that file is correct.");
        return StringUtils.toStringArray(configurations);
    }

    private Class<?> getSpringFactoriesLoaderFactoryClass(){
        return EnableUserAutoConfig.class;
    }
}
