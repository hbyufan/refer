package com.hjz.serialize;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
/**
 * 级联自定义档案只需要存末级id
 * @author hupeng 2016年12月30日
 *
 */
public class CascaderDeserializer extends JsonDeserializer<String> {
	@Override
	public String deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {

		String[] ids = jp.getCodec().readValue(jp, String[].class);
		if (ids != null) {
			String id = ids[ids.length-1];
			// String code = node.get("code").asText();
			// String name = node.get("name").asText();
			return id;
		} else {
			return jp.getText();
		}

	}
}
