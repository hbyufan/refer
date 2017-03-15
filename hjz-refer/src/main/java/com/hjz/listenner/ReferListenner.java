package com.hjz.listenner;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ApplicationContextEvent;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hjz.annotation.Refer;
import com.hjz.constants.SupportUrlconstants;
import com.hjz.context.ContextUtils;
import com.hjz.database.entity.SuperEntity;
import com.hjz.database.repository.EntityNativeQuery;
import com.hjz.utils.ReferCacheTool;

@Component
public class ReferListenner implements ApplicationListener<ApplicationContextEvent> {
	@Autowired
	private EntityNativeQuery<? extends SuperEntity> query;

	/**
	 * Y的时候初始化
	 */
	@Value("${refer.init:N}")
	private String referInit;

	@Override
	public void onApplicationEvent(ApplicationContextEvent event) {
		if (event.getApplicationContext().getParent() != null) {
			Map<String, Object> beansWithAnnotationMap = ContextUtils.getApplicationContext()
					.getBeansWithAnnotation(Refer.class);
			JSONArray jsonbeans = new JSONArray();
			for (String key : beansWithAnnotationMap.keySet()) {
				System.out.println("beanName= " + key);
				Object bean = beansWithAnnotationMap.get(key);
				Refer refer = bean.getClass().getAnnotation(Refer.class);
				if (refer != null) {
					List<String> filednames = new ArrayList<>();
					filednames.add(refer.id());
					filednames.add(refer.code());
					filednames.add(refer.name());
					if (!StringUtils.isEmpty(refer.parentId())) {
						filednames.add(refer.parentId());
					}
					if(referInit.equals("Y")){
						List<SuperEntity> lst = null;
						try {
							lst = (List<SuperEntity>) query.query((Class<? extends SuperEntity>) bean.getClass(),
									filednames, null, null);
							ReferCacheTool.putBatchReferCache(lst, bean.getClass());
						} catch (Exception e) {
							throw new RuntimeException(e);
						}
					}
					
					JSONObject jsonParam = new JSONObject();
					jsonParam.put("idColumn", refer.id());
					jsonParam.put("codeColumn", refer.code());
					jsonParam.put("nameColumn", refer.name());
					jsonParam.put("beanName", bean.getClass().getName());
					jsonParam.put("referCode", refer.referCode());
					jsonbeans.add(jsonParam);
				
				}
			}
			if(jsonbeans.size() > 0){
				sendReferToSupport(jsonbeans);
			}
		}
	}

	/**
	 * 将refer注解参照发送到support
	 * 
	 * @param jsonbeans
	 */
	private void sendReferToSupport(JSONArray jsonbeans) {

		CloseableHttpClient client = null;
		SupportUrlconstants supportUrlconstants = ContextUtils.getBean(SupportUrlconstants.class);

		HttpPost htPost = new HttpPost(
				supportUrlconstants.getSupportBaseUrl() + supportUrlconstants.RERERBASE_SAVE_URL);

		StringEntity entity = new StringEntity(jsonbeans.toString(), "utf-8");
		entity.setContentEncoding("UTF-8");
		entity.setContentType(MediaType.APPLICATION_JSON.toString());
		htPost.setEntity(entity);
		try {
			client = HttpClients.createDefault();
			HttpResponse httpResponse = client.execute(htPost);
			 HttpEntity httpEntity = httpResponse.getEntity();
			 String backMsg = EntityUtils.toString(httpEntity);
			 System.out.println(backMsg);
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			try {
				client.close();
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}

	}

}
