package com.hjz.serialize;

import java.io.IOException;
import java.util.Arrays;
import java.util.Objects;

import org.apache.commons.lang3.StringUtils;

import com.alibaba.fastjson.JSONArray;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.hjz.annotation.ReferSerialTransfer;
import com.hjz.utils.ReferObjectUtil;

/**
 * 将String转换成json{id:xx,code:xx,name:xx}
 * <note>将String{id,id}转换成JsonArray[{id:xx,code:xx,name:xx},{id:xx,code:xx,name:
 * xx}]</note>
 */

public class ReferSerializer extends JsonSerializer<String> implements ContextualSerializer {
	/**
	 * 参照注册对应code
	 */
	private String referCode;

	public ReferSerializer(String referCode) {
		super();
		this.referCode = referCode;
	}

	// 必须要保留无参构造方法
	public ReferSerializer() {
		super();
	}

	@Override
	public void serialize(String value, JsonGenerator jgen, SerializerProvider provider)
			throws IOException, JsonProcessingException {
		String[] ids = StringUtils.isEmpty(value) ? null : value.split(",");
		if (ids != null && ids.length > 0) {
			JSONArray jsonArray = ReferObjectUtil.getReferEntityValue(Arrays.asList(ids), referCode);
			if (jsonArray != null) {
				// 单选
				if (jsonArray.size() == 1) {
					jgen.writeObject(jsonArray.get(0));
				}
				// 多选
				else {
					jgen.writeObject(jsonArray);
				}
			}
		}else{
			jgen.writeObject(null);
		}

	}

	@Override
	public JsonSerializer<?> createContextual(SerializerProvider serializerProvider, BeanProperty beanProperty)
			throws JsonMappingException {
		if (beanProperty != null) { // 为空直接跳过
			if (Objects.equals(beanProperty.getType().getRawClass(), String.class)) { // 非String类直接跳过
				AnnotatedMethod method = (AnnotatedMethod) beanProperty.getMember();
				ReferSerialTransfer referSerialTransfer = method.getAnnotated()
						.getDeclaredAnnotation(ReferSerialTransfer.class);
				if (referSerialTransfer != null) { // 如果能得到注解，就将注解的value传入ReferSerializer
					return new ReferSerializer(referSerialTransfer.referCode());
				}
			}
			return serializerProvider.findValueSerializer(beanProperty.getType(), beanProperty);
		}
		return serializerProvider.findNullValueSerializer(beanProperty);
	}

}
