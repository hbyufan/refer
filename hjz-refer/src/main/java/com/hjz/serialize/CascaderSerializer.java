package com.hjz.serialize;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.hjz.constants.ReferConstant;
import com.hjz.utils.ReferObjectUtil;

/**
 * 级联自定义档案转换为前端需要的[id1,id2,id3]
 * 
 * @author hupeng 2016年12月30日
 *
 */
public class CascaderSerializer extends JsonSerializer<String> {
	/**
	 * 自定义档案实体参照code
	 */
	public final static String REFER_DEFDOC = "defdoc";

	@Override
	public void serialize(String value, JsonGenerator jgen, SerializerProvider provider)
			throws IOException, JsonProcessingException {

		List<String> idLst = new ArrayList<>();
		idLst.add(value);
		setParentId(idLst, value);
		// 第一层去掉
		idLst.remove(idLst.size() - 1);
		Collections.reverse(idLst);

		jgen.writeObject(idLst);
	}

	/**
	 * 递归搜索
	 * 
	 * @param idLst
	 * @param id
	 */
	private void setParentId(List<String> idLst, String id) {
		JSONArray jSONArray = ReferObjectUtil.getReferEntityValue(Arrays.asList(new String[] { id }), REFER_DEFDOC);
		JSONObject jsonobject = null;
		if (jSONArray != null)
			jsonobject = (JSONObject) jSONArray.get(0);
		if (jsonobject != null && !StringUtils.isEmpty(jsonobject.getString(ReferConstant.REFER_PARENTID))) {
			idLst.add(jsonobject.getString(ReferConstant.REFER_PARENTID));
			setParentId(idLst, jsonobject.getString(ReferConstant.REFER_PARENTID));
		}

	}
}
