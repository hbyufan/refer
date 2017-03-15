package com.hjz.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.stereotype.Component;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Component
public @interface Refer {

	String value() default "";

	/**
	 * 参照注册节点对应的参照编码
	 * 
	 * @return
	 */
	public String referCode() default "";

	/**
	 * 档案ID
	 * 
	 * @return
	 */
	public String id() default "id";

	/**
	 * 档案CODE
	 * 
	 * @return
	 */
	public String code() default "code";

	/**
	 * 档案名称
	 * 
	 * @return
	 */
	public String name() default "name";
	
	
	/**
	 * 父级档案ID
	 * @return
	 */
	public String parentId() default  "";
	

}
