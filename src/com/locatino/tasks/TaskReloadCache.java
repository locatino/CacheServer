package com.locatino.tasks;

import com.locatino.cacher.CacheDatabase;
import com.locatino.util.Logger;


public class TaskReloadCache {
	private static final Logger logger = Logger.getLogger(TaskReloadCache.class);
    public static void  reloadAll() throws Exception  
    {  
    	logger.info("start reload...");
    	CacheDatabase.reloadHainanHisAlarm();
    } 
}
