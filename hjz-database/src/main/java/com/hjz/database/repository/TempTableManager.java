package com.hjz.database.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import com.hjz.database.entity.SuperEntity;

@Component
public final class TempTableManager<T extends SuperEntity> extends AbstractEntityOperator<T> {

	private static final String TEMP_TABLE_PREFIX = "TEMP_"; // 临时表名称前缀

	@Autowired
	private JdbcTemplate jdbcTemplate;

	/**
	 * 执行创建数据库临时表
	 * 
	 * @param clazz
	 * @return 临时表名称
	 */
	public synchronized String createTempTable(Class<? extends SuperEntity> clazz) {
		String tableName = generateTableName();
		initOperator(clazz, tableName);
		String sql = generateDdlSql(tableName);
		jdbcTemplate.execute(sql);
		return tableName;
	}

	/**
	 * 执行删除临时表(考虑并发的情况,必须指定临时表名称)
	 */
	public synchronized void dropTempTable(String tableName) {
		String sql = " drop table if exists " + tableName;
		jdbcTemplate.execute(sql);
	}

	/**
	 * 生成临时表名
	 * 
	 * @return
	 */
	private String generateTableName() {
		String tableName = TEMP_TABLE_PREFIX + UUID.randomUUID().toString().replace("-", "");
		return tableName;
	}

	/**
	 * 生成建表sql(简单处理,临时表字段全部为varchar(100)类型,不设置主键和索引,有实际需求时再完善)
	 * 
	 * @return
	 */
	private String generateDdlSql(String tableName) {
		StringBuffer sb = new StringBuffer();
		sb.append("create table if not exists ").append(tableName).append(" ( "); // create
																					// temporary
																					// table
		List<String> fieldList = getDbFiledNames();
		for (String field : fieldList) {
			sb.append(field).append(" varchar(100),");
		}
		sb.deleteCharAt(sb.lastIndexOf(",")).append(" )");
		return sb.toString();
	}

}
