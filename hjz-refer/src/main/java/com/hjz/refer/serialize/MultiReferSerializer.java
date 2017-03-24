package com.hjz.refer.serialize;

import java.io.IOException;

import org.apache.commons.lang3.StringUtils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.hjz.cache.strategy.JedisCacheTool;
import com.hjz.refer.constants.ReferConstant;
import com.hjz.refer.context.ContextUtils;

/**
 * <p>参照多选id转id-code-name</p>
 * 
 * <note>将String{id,id}转换成JsonArray[{id:xx,code:xx,name:xx},{id:xx,code:xx,name:xx}]</note>
 * 
 */
@Deprecated
public class MultiReferSerializer extends JsonSerializer<String> {

	@Override
	public void serialize(String value, JsonGenerator jgen, SerializerProvider provider)
			throws IOException, JsonProcessingException {
		String[] ids =StringUtils.isBlank(value) ? null: value.split(",");
		JSONArray jsonArray = new JSONArray();
		if(ids != null && ids.length > 0){
			JedisCacheTool cacheTool = ContextUtils.getBean(JedisCacheTool.class);
			JSONObject jsonObj = null;
			for (String id : ids) {
				jsonObj = (JSONObject) cacheTool.get(id);
				if (jsonObj == null) {
					jsonObj= new JSONObject();
					jsonObj.put(ReferConstant.REFER_ID, id);
					jsonObj.put(ReferConstant.REFER_CODE, id);
					jsonObj.put(ReferConstant.REFER_NAME, id);
				}
				jsonArray.add(jsonObj);
			}
		}else{
			JSONObject jsonObj= new JSONObject();
			jsonObj.put(ReferConstant.REFER_ID, null);
			jsonObj.put(ReferConstant.REFER_CODE, null);
			jsonObj.put(ReferConstant.REFER_NAME, null);
			jsonArray.add(jsonObj);
		}
		jgen.writeObject(jsonArray);
	}
}
