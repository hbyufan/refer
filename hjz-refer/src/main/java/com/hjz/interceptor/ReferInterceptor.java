package com.hjz.interceptor;

import java.io.Serializable;

import org.hibernate.EmptyInterceptor;
import org.hibernate.type.Type;

import com.hjz.annotation.Refer;
import com.hjz.database.entity.SuperEntity;
import com.hjz.utils.ReferCacheTool;

public class ReferInterceptor extends EmptyInterceptor {

	private static final long serialVersionUID = -733891814099550065L;

	@Override
	public void onDelete(Object entity, Serializable id, Object[] state, String[] propertyNames, Type[] types) {

		ReferCacheTool.deleteReferCache(entity);
		super.onDelete(entity, id, state, propertyNames, types);
	}

	@Override
	public boolean onSave(Object entity, Serializable id, Object[] state, String[] propertyNames, Type[] types) {
		ReferCacheTool.putReferCache(entity);
		return super.onSave(entity, id, state, propertyNames, types);
	}

	@Override
	public boolean onFlushDirty(Object entity, Serializable id, Object[] currentState, Object[] previousState,
			String[] propertyNames, Type[] types) {
		if (entity instanceof SuperEntity) {

			// 删除
			if (((SuperEntity) entity).getDr()==SuperEntity.IS_DELETE_YES) {
				ReferCacheTool.deleteReferCache(entity);
			}
			// 修改
			else {
				Refer refer = entity.getClass().getAnnotation(Refer.class);
				if (refer != null) {
					int referidIndex = 0;
					int referidCode = 0;
					int referidName = 0;
					int referParentId = 0;
					for (int i = 0; i < propertyNames.length; i++) {
						if (propertyNames[i].equals(refer.id())) {
							referidIndex = i;
						} else if (propertyNames[i].equals(refer.code())) {
							referidCode = i;
						} else if (propertyNames[i].equals(refer.name())) {
							referidName = i;
						}
						else if (propertyNames[i].equals(refer.parentId())) {
							referParentId = i;
						}
					}
					// 修改保存不一定改了编码和名称，ID应该不会变，不排除ID不是主键所以还是取一遍
					boolean caheUpdate = false;
					if (!currentState[referidIndex].equals(previousState[referidIndex])) {
						caheUpdate = true;
					} else if (!currentState[referidCode].equals(previousState[referidCode])) {
						caheUpdate = true;
					} else if (!currentState[referidName].equals(previousState[referidName])) {
						caheUpdate = true;
					}
					else if (currentState[referParentId] != null && !currentState[referParentId].equals(previousState[referParentId])) {
						caheUpdate = true;
					}
					if (caheUpdate) {
						ReferCacheTool.putReferCache(entity);
					}
				}
			}
		}
		return super.onFlushDirty(entity, id, currentState, previousState, propertyNames, types);
	}

}
