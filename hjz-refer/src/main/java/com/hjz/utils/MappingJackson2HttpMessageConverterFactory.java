package com.hjz.utils;

import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableList;
import com.hjz.serialize.ReferBeanSerializerModifier;

/**
 * 
 * @author hupeng 2016年9月21日
 *
 */
@Deprecated
public class MappingJackson2HttpMessageConverterFactory {
	public MappingJackson2HttpMessageConverter init() {
		final MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
		ObjectMapper mapper = converter.getObjectMapper();
		mapper.setSerializerFactory(
				mapper.getSerializerFactory().withSerializerModifier(new ReferBeanSerializerModifier()));
		converter.setPrettyPrint(true);
		converter.setSupportedMediaTypes(ImmutableList.of(MediaType.APPLICATION_JSON_UTF8,
				new MediaType("application", "*+json", MappingJackson2HttpMessageConverter.DEFAULT_CHARSET)));
		return converter;
	}
}
