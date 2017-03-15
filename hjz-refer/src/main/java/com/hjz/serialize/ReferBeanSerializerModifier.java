package com.hjz.serialize;

import java.util.List;

import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.BeanPropertyWriter;
import com.fasterxml.jackson.databind.ser.BeanSerializerModifier;
import com.fasterxml.jackson.databind.ser.std.NullSerializer;

/**
 * 
 * @author hupeng 2016年9月21日
 *
 */
@Deprecated
public class ReferBeanSerializerModifier extends BeanSerializerModifier {
	private JsonSerializer<Object> _nullArrayJsonSerializer = new ReferNullJsonSerializer();

	@Override
	public List<BeanPropertyWriter> changeProperties(SerializationConfig config, BeanDescription beanDesc,
			List<BeanPropertyWriter> beanProperties) {
		// 循环所有的beanPropertyWriter
		for (int i = 0; i < beanProperties.size(); i++) {
			BeanPropertyWriter writer = beanProperties.get(i);
			JsonSerialize jsonSerialize = writer.getAnnotation(JsonSerialize.class);
			
			if (jsonSerialize != null) {
				if (jsonSerialize.using().isAssignableFrom(ReferObjectSerializer.class)) {
					// 给writer注册一个自己的nullSerializer
					writer.assignNullSerializer(NullSerializer.instance);
					// writer.assignNullSerializer(this.defaultNullArrayJsonSerializer());
				} else if (jsonSerialize.using().isAssignableFrom(ReferSerializer.class)) {
					writer.assignNullSerializer(this.defaultNullArrayJsonSerializer());
//					AnnotatedMethod method=	(AnnotatedMethod)	writer.getMember();
//					ReferSerialTransfer referSerialTransfer =	method.getAnnotated().getDeclaredAnnotation(ReferSerialTransfer.class);
//					if (referSerialTransfer != null) { // 如果能得到注解，就将注解的value传入ReferSerializer
//						writer.assignSerializer(new ReferSerializer(referSerialTransfer.referCode()));
//					}
				}
			}
		}
		return beanProperties;
	}

	protected JsonSerializer<Object> defaultNullArrayJsonSerializer() {
		return _nullArrayJsonSerializer;
	}
}
