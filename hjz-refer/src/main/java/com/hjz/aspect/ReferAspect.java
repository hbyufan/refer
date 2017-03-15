package com.hjz.aspect;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.hjz.cache.strategy.RedisCacheStrategy;

@Component
@Aspect
@Deprecated
public class ReferAspect {

	@Autowired
	private RedisCacheStrategy redisCacheStrategy;

	// Controller层切点
	@Pointcut("@annotation(org.springframework.web.bind.annotation.ResponseBody)")
	public void controllerAspect() {

	}

//	// 配置环绕通知,使用在方法aspect()上注册的切入点
//	@Around("controllerAspect()")
//	public void around(JoinPoint joinPoint) {
//		long start = System.currentTimeMillis();
//		try {
//			((ProceedingJoinPoint) joinPoint).proceed();
//			//Object result = ((ProceedingJoinPoint) joinPoint).proceed();
////			if (result instanceof JsonBackData) {
////				JsonBackData backData = (JsonBackData) result;
////				Object content = backData.getBackData();
////				ReferFiled referFiled = content.getClass().getAnnotation(ReferFiled.class);
////				if (referFiled != null) {
////					String[] fileds = referFiled.value();
////					for (String filed : fileds) {
////						Method method = content.getClass().getMethod("get" + changeFirstCharacterCase(true, filed),
////								String.class);
////						String filedId = (String) method.invoke(content, new Object[] {});
////						System.out.println("redisCacheStrategy.get(filedId)----" + redisCacheStrategy.get(filedId));
////					}
////				}
////			}
//			long end = System.currentTimeMillis();
//
//		} catch (Throwable e) {
//			throw new BusinessException(e);
//
//		}
//	}

	private String changeFirstCharacterCase(boolean capitalize, String str) {
		int strLen;
		if (str == null || (strLen = str.length()) == 0) {
			return str;
		}
		StringBuffer buf = new StringBuffer(strLen);
		if (capitalize) {
			buf.append(Character.toUpperCase(str.charAt(0)));
		} else {
			buf.append(Character.toLowerCase(str.charAt(0)));
		}
		buf.append(str.substring(1));
		return buf.toString();
	}

}
