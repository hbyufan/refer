package com.hjz.md.consumer;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.alibaba.fastjson.JSONArray;
import com.hjz.md.bo.MdReferBO;
import com.hjz.md.service.MdReferService;
import com.hjz.mq.common.MqMessage;
import com.hjz.mq.consumer.BaseConsumer;

@Component
public class MdReferBaseConsumer extends BaseConsumer {
	@Autowired
	private MdReferService mdReferService;

	@Override
	protected void doConsumeMsg(MqMessage mqMessage) {
		JSONArray jsonbeans = (JSONArray) mqMessage.getBody();
		List<MdReferBO> boLst = JSONArray.parseArray(jsonbeans.toJSONString(), MdReferBO.class);
		mdReferService.saveReferInfo(boLst);
	}

	@Override
	protected String[] getQueueNames() {
		return new String[] { "MdReferbase-update" };
	}

}
