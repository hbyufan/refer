package com.hjz.refer.serialize;

import java.io.IOException;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.hjz.refer.constants.ReferConstant;
@Deprecated
public class ReferNullJsonSerializer extends JsonSerializer<Object> {

	@Override
	public void serialize(Object value, JsonGenerator jgen, SerializerProvider provider)
			throws IOException, JsonProcessingException {
		JSONObject jsonobject = new JSONObject();
		jsonobject.put(ReferConstant.REFER_ID, "");
		jsonobject.put(ReferConstant.REFER_CODE, "");
		jsonobject.put(ReferConstant.REFER_NAME, "");
		jgen.writeObject(jsonobject);
	}

}
