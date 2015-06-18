package com.eastcom.cacher;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;



import org.springframework.jdbc.core.JdbcTemplate;

import com.locatino.server.Constant;
import com.locatino.server.DbCacheServer;
import com.locatino.util.Logger;

/**
 * cacheDB，and provide api for access cache and db
 * @author locatino
 *
 */
public class CacheDatabase {

	private static final Logger logger = Logger.getLogger(CacheDatabase.class);
	
	@SuppressWarnings("unused")
	private static DataSource dataSource;
	private static DataSource cacheDatasource;
	private static JdbcTemplate cacheJdbcTemplate;
	private static JdbcTemplate jdbcTemplate;

	public CacheDatabase(DataSource oracleDatasource, DataSource cacheDataSource, String initSql) throws Exception {
		long startTime = System.currentTimeMillis();
		logger.info("init cache db...");
		CacheDatabase.jdbcTemplate = new JdbcTemplate(oracleDatasource);
		CacheDatabase.cacheDatasource = cacheDataSource;
		CacheDatabase.cacheJdbcTemplate = new JdbcTemplate(cacheDatasource);
		CacheDatabase.cacheJdbcTemplate.update(initSql);
		reloadAll();
		logger.info("cache db init succ，use time："+ (System.currentTimeMillis() - startTime)+" ms");
	}

	public void setDatasource(DataSource oracleDatasource) {
		CacheDatabase.cacheDatasource = oracleDatasource;	
	}
	
	public static void reloadAll() throws Exception{
		reloadHainanHisAlarm();
	}
	
	public static void reloadHainanHisAlarm() throws Exception {
		List<Map<String, Object>> reList;
		logger.info("reload （V_FM_RTRECORD_HAINAN_HIS）...");
		long start_V_FM_RTRECORD_HAINAN_HIS = System.currentTimeMillis();
		reList = jdbcTemplate.queryForList("select * from V_FM_RTRECORD_RES_ALL where (IMPACT_PRODUCT in (select distinct PRODUCT_NAME from FM_CHUNJIE_PRODUCT_SENDTICK ) ) order by OCCUR_TIME DESC");
		StringBuilder sb = new StringBuilder();
		sb.append("insert into V_FM_RTRECORD_HAINAN_HIS(")
		.append("RECORD_ID, EVENT_ID, CHILD_EVENT_ID, RECORD_TYPE, OCCUR_TIME, COLLECT_TIME, START_TIME, LAST_TIME, COUNTS, ")
		.append("SEVERITY, FACILITY, TITLE , ALARM_DESCR, CONTEXT, KEY_VALUE, PARENT_ID, IS_INHERIT, HAS_CHILDREN, INHERIT_TYPE,")
		.append("ACK_TAG, ACK_USER, ACK_TIME, CLEAR_TAG, CLEAR_USER, CLEAR_TIME, DISPATCH_TAG, DISPATCH_USER, ")
		.append("DISPATCH_TIME, ISOLATE_TAG, ISOLATE_TIME, ISOLATE_USER, HANGUP_TAG, HANGUP_TIME, HANGUP_USER,")
		.append("PREDEAL_TAG, PREDEAL_USER, PREDEAL_TIME, ORDER_ID, DISPATCH_POLICY_ID, ORDER_STATUS,")
		.append("PREDEAL_POLICY_ID, PREDEAL_POLICY_NAME, PREDEAL_RESULT,  REMARK,")
		.append("DEVICE_ID, DEVICE_NAME, DEVICE_ALIAS, IPADDRESS, DEVICE_DESCR , VENDOR, MODEL, SERVICE,")
		.append("STATUS, LIFE_CYCLE, PROVINCE, CITY, SUB_ID, SUB_NAME, SUB_ALIAS, RESOURCE_TYPE, RESOURCE_NMS_STATUS, SOURCE_INFO, NE_TYPE, ")
		.append("IMPACT_PRODUCT, IMPACT_CUSTOMER ")
		.append(")")
		.append(" values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
		
		jdbcTemplate.execute("delete * from V_FM_RTRECORD_HAINAN_HIS");
		for(Map<String, Object> map : reList){
			cacheJdbcTemplate.update(sb.toString(), map.get("RECORD_ID"),map.get("EVENT_ID"),map.get("CHILD_EVENT_ID"),map.get("RECORD_TYPE"),map.get("OCCUR_TIME")
					,map.get("COLLECT_TIME"),map.get("START_TIME"),map.get("LAST_TIME"),map.get("COUNTS"),map.get("SEVERITY"),
					map.get("FACILITY"),map.get("TITLE"),map.get("ALARM_DESCR"),map.get("CONTEXT"),map.get("KEY_VALUE")
					,map.get("PARENT_ID"),map.get("IS_INHERIT"),map.get("HAS_CHILDREN"),map.get("INHERIT_TYPE"),map.get("ACK_TAG")
					,map.get("ACK_USER"),map.get("ACK_TIME"),map.get("CLEAR_TAG"),map.get("CLEAR_USER"),map.get("CLEAR_TIME")
					,map.get("DISPATCH_TAG"),map.get("DISPATCH_USER"),map.get("DISPATCH_TIME"),map.get("ISOLATE_TAG"),map.get("ISOLATE_TIME")
					,map.get("ISOLATE_USER"),map.get("HANGUP_TAG"),map.get("HANGUP_TIME"),map.get("HANGUP_USER"),map.get("PREDEAL_TAG")
					,map.get("PREDEAL_USER"),map.get("PREDEAL_TIME"),map.get("ORDER_ID"),map.get("DISPATCH_POLICY_ID"),map.get("ORDER_STATUS")
					,map.get("PREDEAL_POLICY_ID"),map.get("PREDEAL_POLICY_NAME"),map.get("PREDEAL_RESULT"),map.get("REMARK"),map.get("DEVICE_ID")
					,map.get("DEVICE_NAME"),map.get("DEVICE_ALIAS"),map.get("IPADDRESS"),map.get("DEVICE_DESCR"),map.get("VENDOR")
					,map.get("MODEL"),map.get("SERVICE"),map.get("STATUS"),map.get("LIFE_CYCLE"),map.get("PROVINCE")
					,map.get("CITY"),map.get("SUB_ID"),map.get("SUB_NAME"),map.get("SUB_ALIAS"),map.get("RESOURCE_TYPE")
					,map.get("RESOURCE_NMS_STATUS"),map.get("SOURCE_INFO"),map.get("NE_TYPE"),map.get("IMPACT_PRODUCT"),map.get("IMPACT_CUSTOMER"));
		}
		logger.info("reload（V_FM_RTRECORD_HAINAN_HIS）succ，use time:" + ( System.currentTimeMillis() - start_V_FM_RTRECORD_HAINAN_HIS) + "ms");
		logger.info( "jvm mem info : " + DbCacheServer.toMemoryInfo ());
	}
	
	public static List<Map<String, Object>> execSql(int op, String sql){
		if(op == Constant.OP_EXEC_CACHE_SQL){
			return (cacheJdbcTemplate.queryForList(sql));
		}else if(op == Constant.OP_EXEC_ORACLE_SQL){
			return (jdbcTemplate.queryForList(sql));
		}else {
			return null;
		}
	}
}
