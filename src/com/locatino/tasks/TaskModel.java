package com.locatino.tasks;

public class TaskModel {
	public String getClassName() {  
        return className;  
    }  
    public void setClassName(String className) {  
        this.className = className;  
    }  
    public String getMethodName() {  
        return methodName;  
    }  
    public void setMethodName(String methodName) {  
        this.methodName = methodName;  
    }  
    public long getInitialDelay() {  
        return initialDelay;  
    }  
    public void setInitialDelay(long initialDelay) {  
        this.initialDelay = initialDelay;  
    }  
    public long getPeriod() {  
        return period;  
    }  
    public void setPeriod(long period) {  
        this.period = period;  
    }  
    private String className;  
    private String methodName;  
    private long initialDelay;  
    private long period;  
}
