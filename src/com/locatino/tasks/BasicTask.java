package com.locatino.tasks;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Date;

import com.locatino.util.Logger;


public class BasicTask implements Runnable{
	private static final Logger logger = Logger.getLogger(BasicTask.class);
	
    private TaskModel taskModel;  
    public BasicTask() {}  
      
    public BasicTask(TaskModel tm) {  
        this.taskModel = tm;  
    }  
        
    public void run() {  
    	logger.info("call at " + (new Date()));  
         try {  
                Class<?> classType = Class.forName(taskModel.getClassName());  
                Method getMethod = classType.getMethod(taskModel.getMethodName());  
                getMethod.invoke(classType);  
                  
         } catch (SecurityException e) {  
                e.printStackTrace();  
         } catch (IllegalArgumentException e) {  
                e.printStackTrace();  
         } catch (ClassNotFoundException e) {  
                e.printStackTrace();  
         } catch (NoSuchMethodException e) {  
                e.printStackTrace();  
         } catch (IllegalAccessException e) {  
                e.printStackTrace();  
         } catch (InvocationTargetException e) {  
                e.printStackTrace();  
         }  
          
    }  
}
