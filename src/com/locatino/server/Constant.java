package com.eastcom.server;

/**
 * const
 * @author locatino
 *
 */
public class Constant {
	public static final int TCP_PORT = 5050;
	
	public static final int MAX_BYTE_SIZE = 100000;
	
	public static final int TASK_THREAD_POOL_SIZE = 2;
	
	public static final int HANDLER_THREAD_NUM = 2;

	public static final int SOURCE_FROM_CMDCENTER = 0;

	public static final int SOURCE_FROM_PROCESSOR = 1;

	public static final int OP_EXEC_CACHE_SQL = 0;

	public static final int OP_EXEC_ORACLE_SQL = 1;
	
	public static final String TASK_CONFIG_PATH = "../conf/taskconfig.xml";
	
	public static final String GLOBAL_CONFIG_PATH = "../conf/db.xml";
	
}
