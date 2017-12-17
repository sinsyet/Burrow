package com.example.jutil.eventbus.anno;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)             // 作用域
@Retention(RetentionPolicy.RUNTIME)     // 允注解保留到运行时
public @interface Subscribe {
    Class value();
}
