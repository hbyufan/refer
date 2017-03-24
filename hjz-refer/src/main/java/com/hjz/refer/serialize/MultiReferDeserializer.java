package com.hjz.refer.serialize;

import java.io.IOException;
import java.util.Iterator;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;

/**
 * 参照多选id-code-name转id
 * 
 * <note>将JsonArray[{id:xx,code:xx,name:xx},{id:xx,code:xx,name:xx}]转换成String{id,id}</note>
 * 
 * @author xg。
 * @date 2016-11-22
 */
@Deprecated
public class MultiReferDeserializer extends JsonDeserializer<String> {

	@Override
	public String deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
		JsonNode node = jp.getCodec().readTree(jp);
		if (node == null) {
			return jp.getText();
		} else if (node instanceof ArrayNode) {
			ArrayNode nodes = (ArrayNode) node;
			Iterator<JsonNode> it = nodes.iterator();
			StringBuffer sb = new StringBuffer();
			while (it.hasNext()) {
				JsonNode item = it.next();
				String id = item.get("id").asText();
				sb.append(id).append(",");
			}
			return sb.deleteCharAt(sb.lastIndexOf(",")).toString();
		} else {
			return node.get("id").asText();
		}
	}
}
