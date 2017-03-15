package com.hjz.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import com.fasterxml.jackson.annotation.JacksonAnnotationsInside;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.hjz.serialize.ReferSerializer;

/**
 * 组合注解 将id转换成json{id:xx,code:xx,name:xx}
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@JacksonAnnotationsInside
@JsonSerialize(using = ReferSerializer.class)
public @interface ReferSerialTransfer {
/**
 * 参照注册中的code
 * @return
 */
   public String referCode() default "";
}
