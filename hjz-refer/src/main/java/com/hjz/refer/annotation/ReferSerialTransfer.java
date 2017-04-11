package com.hjz.refer.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import com.fasterxml.jackson.annotation.JacksonAnnotationsInside;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.hjz.refer.serialize.ReferSerializer;

/**
 * 组合注解 将id转换成json{id:xx,code:xx,name:xx}
 *@JacksonAnnotation: marker annotation added to all Jackson-defined annotations (which includes all other annotations contained in this package)
  @JacksonAnnotationsInside: marked annotation used to indicate that a custom annotation contains Jackson annotations; used to allow "annotation bundles", custom annotations that are annotated with Jackson annotations (why? to allow adding just a single annotation to represent set of multiple Jackson annotations)
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
