package com.locatino.util;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;


/**
 * 
 * @author atom
 *
 * 该类是用于整合 Log4j  commonLog 的一种整合类.
 * 当系统中存在log4j的jar包时,则使用log4j,
 * 否则检查是否存在commons-logging包,如果有则使用commonLog,
 * 如果都没有,则使用jdk自带的Logging
 * 
 * 2013-07-11 添加 Log4j 的支持, 其它方式待添加
 * 
 */
public class Logger {

	/**
	 * 系统包类型 
	 * 0 - 系统自带;
	 * 1 - log4j
	 * 2 - common log
	 */
	private static int logType = 0;

	private static ClassLoader classloader = Logger.class.getClassLoader();
	
	private static enum LOG_LEVEL{
		FATAL,ERROR,WARN,INFO,DEBUG
	}
	
	
	private static Class<?> loggerClass;
	
	private static class Log4jArguments{
	
		private static Class<?> log4jLevelClass;
		private static Class<?> log4jRepositoryClass;
		private static Class<?> log4jLogEventClass;
		private static Class<?> priorityClass;
		private static Class<?> categoryClass;
		
		
		private static Method getLoggerRepository;
		private static Method repositoryIsDisabled;
		private static Method isGreaterOrEqual;
		private static Method getEffectiveLevel;
		private static Method callAppenders;
		
		private static Constructor<?> LoggingEvent;
		
		private static Object FATAL;
		private static Object ERROR;
		private static Object WARN;
		private static Object INFO;
		private static Object DEBUG;
		  
		
	}
	
	
	private Object logger;
	
	static{
		classloader = Logger.class.getClassLoader();
		findLogger();
	}
	
	private static void findLogger(){
		
		String className = "org.apache.log4j.Logger";
		try {
			loggerClass = classloader.loadClass(className);
			
			logType = 1;
			return;
		} catch (ClassNotFoundException e) {
			System.out.println("未成功获取Log4j日志包");
		}
	
		
		className = "org.apache.commons.logging.Log";
		try {
			loggerClass = classloader.loadClass(className);
			logType = 2;
			return;
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	
		className = "java.util.logging.Logger";
		return;
	}
	
	
	
	public static Logger getLogger(Class<?> c){
		Logger rt = new Logger();
		
		if(logType == 0 || logType == 1){
			try {
				Method m = loggerClass.getMethod("getLogger", Class.class);
				rt.logger = m.invoke(null, c);	//反射时传入对象为空,调用静态方法
			} catch (SecurityException e) {
				e.printStackTrace();
			} catch (NoSuchMethodException e) {
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			}
		}else if(logType == 2){
			try {
				Class<?> logFactory = classloader.loadClass("org.apache.commons.logging.LogFactory");
				Method m = logFactory.getMethod("getLog", Class.class);
				rt.logger = m.invoke(null, c);
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (SecurityException e) {
				e.printStackTrace();
			} catch (NoSuchMethodException e) {
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			}
			
		}
		return rt;
	}
	
	public static Logger getLogger(String className){
		try {
			return getLogger(classloader.loadClass(className));
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public void info(Object message){
		try {
			
			if(logType == 1){
				print(LOG_LEVEL.INFO,message);
			}else{
				Method m = loggerClass.getMethod("info",Object.class);
				m.invoke(logger, message);
			}
			
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
	}
	
	public void error(Object message){

		try {
			if(logType == 1){
				print(LOG_LEVEL.ERROR,message);
			}else{
				Method m = loggerClass.getMethod("error",Object.class);
				m.invoke(logger, message);
			}
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
	
	}
	public void warn(Object message){
		try {
			if(logType == 1){
				print(LOG_LEVEL.WARN,message);
			}else{
				Method m = loggerClass.getMethod("warn",Object.class);
				m.invoke(logger, message);
			}
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
	}
	
	public void debug(Object message){
		try {
			if(logType == 1){
				print(LOG_LEVEL.DEBUG,message);
			}else{
				Method m = loggerClass.getMethod("debug",Object.class);
				m.invoke(logger, message);
			}
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
	}
	
	public void setConfigFile(String fileName){
		
	}
	
	public void setConfigFile(java.io.File file){
		
	}
	
	
	private void print(LOG_LEVEL level,Object object){
		
		switch(logType){
		case 0:
			break;
		case 1:
			
			if(Log4jArguments.getLoggerRepository == null){
				initLog4jArguments();
			}
			
			Object lv = getLog4jLevel(level);
			
			try {
				Object repository = Log4jArguments.getLoggerRepository.invoke(logger);
				Object effectiveLevel = Log4jArguments.getEffectiveLevel.invoke(logger);
				Boolean isdisable = (Boolean) Log4jArguments.repositoryIsDisabled.invoke(repository, getLog4jLevelInt(level));
				
				if(isdisable){
					return;
				}
				
				Boolean isGreat = (Boolean) Log4jArguments.isGreaterOrEqual.invoke(lv,effectiveLevel);
				
				if(isGreat){
					Object loggingEvent = Log4jArguments.LoggingEvent.newInstance(Logger.class.getName(),logger,lv,object,null);
					Log4jArguments.callAppenders.invoke(logger, loggingEvent);
				}
				
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			} catch (InstantiationException e) {
				e.printStackTrace();
			}
			
			
			break;
		case 2:
			break;
		}
	}
	
	
	private static void initLog4jArguments(){
		try {
			
			Log4jArguments.log4jLevelClass = classloader.loadClass("org.apache.log4j.Level");
			Log4jArguments.log4jRepositoryClass = classloader.loadClass("org.apache.log4j.spi.LoggerRepository");
			Log4jArguments.log4jLogEventClass = classloader.loadClass("org.apache.log4j.spi.LoggingEvent");
			Log4jArguments.priorityClass = classloader.loadClass("org.apache.log4j.Priority");
			Log4jArguments.categoryClass = classloader.loadClass("org.apache.log4j.Category");
			
			
			Log4jArguments.getLoggerRepository = loggerClass.getMethod("getLoggerRepository");
			Log4jArguments.isGreaterOrEqual = Log4jArguments.log4jLevelClass.getMethod("isGreaterOrEqual", Log4jArguments.priorityClass);
			Log4jArguments.getEffectiveLevel = loggerClass.getMethod("getEffectiveLevel");
			Log4jArguments.callAppenders = loggerClass.getMethod("callAppenders", Log4jArguments.log4jLogEventClass);
			Log4jArguments.repositoryIsDisabled = Log4jArguments.log4jRepositoryClass.getMethod("isDisabled", int.class);
			
			
			Log4jArguments.LoggingEvent = Log4jArguments.log4jLogEventClass.getConstructor(String.class,Log4jArguments.categoryClass,Log4jArguments.priorityClass,Object.class,Throwable.class);
			
			
			Field f = null;
			f = Log4jArguments.log4jLevelClass.getField("FATAL");
			Log4jArguments.FATAL = f.get(null);
			f = Log4jArguments.log4jLevelClass.getField("ERROR");
			Log4jArguments.ERROR = f.get(null);
			f = Log4jArguments.log4jLevelClass.getField("WARN");
			Log4jArguments.WARN = f.get(null);
			f = Log4jArguments.log4jLevelClass.getField("INFO");
			Log4jArguments.INFO = f.get(null);
			f = Log4jArguments.log4jLevelClass.getField("DEBUG");
			Log4jArguments.DEBUG = f.get(null);
			
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
	}
	
	private static Object getLog4jLevel(LOG_LEVEL level){
		
		
		if(Log4jArguments.getLoggerRepository == null){
			initLog4jArguments();
		}
		
		
		Object lv = null;
		
		if(level == LOG_LEVEL.FATAL){
			lv = Log4jArguments.FATAL;
		}else if(level == LOG_LEVEL.ERROR){
			lv = Log4jArguments.ERROR;
		}else if(level == LOG_LEVEL.WARN){
			lv = Log4jArguments.WARN;
		}else if(level == LOG_LEVEL.INFO){
			lv = Log4jArguments.INFO;
		}else if(level == LOG_LEVEL.DEBUG){
			lv = Log4jArguments.DEBUG;
		}
		
		return lv;
	}

	
	private static int getLog4jLevelInt(LOG_LEVEL level){
		  
		int lv = -1;
		if(level == LOG_LEVEL.FATAL){
			lv = 50000;
		}else if(level == LOG_LEVEL.ERROR){
			lv = 40000;
		}else if(level == LOG_LEVEL.WARN){
			lv = 30000;
		}else if(level == LOG_LEVEL.INFO){
			lv = 20000;
		}else if(level == LOG_LEVEL.DEBUG){
			lv = 10000;
		}
		
		return lv;
	}
	
}
