package com.locatino.server;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import com.locatino.tasks.BasicTask;
import com.locatino.tasks.TaskModel;
import com.locatino.util.Logger;
import com.locatino.util.XmlReader;


public class DbCacheServer {
	private static  Logger logger = Logger.getLogger(DbCacheServer.class);
	public static int SERVER_ID;
	public void init(){
		logger.debug("init server--");
	}
	
	public void start(){
		logger.info( " jvm mem info :" + toMemoryInfo ());
		
		//socket listender
		new  Thread(new TcpThread()).start();
		
		//socket handler
		for(int i = 0; i < Constant.HANDLER_THREAD_NUM; i++){
			new Thread(new RequestHandler()).start();
		}
		
		//task thread pool
		ScheduledExecutorService scheduExec = Executors.newScheduledThreadPool(Constant.TASK_THREAD_POOL_SIZE);  

		//load task
	    @SuppressWarnings("rawtypes")
		List tasks=XmlReader.getTasks();
	    for (int i=0;i<tasks.size();i++)  
	    {  
	        TaskModel tm=(TaskModel)tasks.get(i);  
	        scheduExec.scheduleAtFixedRate(new BasicTask(tm),tm.getInitialDelay(), tm.getPeriod(), TimeUnit.SECONDS);   
	    }  
	    
	}
	
	public static void main(String[] args){
		
		
		try {
			String log4jpath = System.getProperty("log4j.configuration");
			if (log4jpath == null)
				org.apache.log4j.BasicConfigurator.configure();
			else if (!log4jpath.toLowerCase().startsWith("file:")) {
				org.apache.log4j.PropertyConfigurator.configure(log4jpath);
			}
			String configFilePath = System.getProperty("Config");
			if (configFilePath == null) {
				configFilePath = Constant.GLOBAL_CONFIG_PATH;
			}
			//reserved property
			SERVER_ID = Integer.valueOf(args[0]);
			
			BeanFactory factory = new FileSystemXmlApplicationContext(configFilePath);
			DbCacheServer server = (DbCacheServer) factory.getBean("server");
			server.start();
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(-1);
		}

	    
	}
	
	/**
	 * jvm monitor
	 * @return
	 */
	public static String toMemoryInfo() { 

	       Runtime currRuntime = Runtime.getRuntime ();

	       int nFreeMemory = ( int ) (currRuntime.freeMemory() / 1024 / 1024);

	       int nTotalMemory = ( int ) (currRuntime.totalMemory() / 1024 / 1024);

	       return nFreeMemory + "M/" + nTotalMemory + "M(free/total)" ;

	    }

}
