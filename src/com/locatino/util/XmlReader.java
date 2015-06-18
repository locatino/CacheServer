package com.eastcom.util;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

import com.eastcom.server.Constant;
import com.eastcom.tasks.TaskModel;

/**
 * analyze task config xml
 * @author locatino
 *
 */
public class XmlReader {
	private static final Logger logger = Logger.getLogger(XmlReader.class);
	public static void main(String[] args) {  
		  
        XmlReader.getTasks();  
    }  
  
    @SuppressWarnings({ "rawtypes", "unchecked" })
	public static List getTasks() {  
  
        List tasks = new ArrayList();  
          
        logger.info("load task config start...");  
        String taskConfig = System.getProperty("taskConfig");
        if(taskConfig == null)taskConfig =  Constant.TASK_CONFIG_PATH;
        String path =  taskConfig;
        logger.debug(path);
        logger.debug(System.getProperty("user.dir"));
        File file = new File(path);  
  
        if (file.exists()) {  
  
            try {  
                SAXBuilder sx = new SAXBuilder();  
                Document doc = sx.build(file);  
                Element rootelement = doc.getRootElement();  
                  
                  
                    List<Element> childs = rootelement.getChildren();  
                    for (int i = 0; i < childs.size(); i++) {  
                        TaskModel tModel = new TaskModel();  
                        tModel.setClassName(childs.get(i).getChildText("class"));  
                        logger.debug(childs.get(i).getChildText("class"));  
                        tModel.setMethodName(childs.get(i).getChildText("method"));  
                        logger.debug(childs.get(i).getChildText("method"));  
  
                        String initialDelay = childs.get(i).getChildText("initialDelay");  
                          
                        tModel.setInitialDelay((Long.valueOf(initialDelay)));  
                        logger.debug("距离首次运行还差" + initialDelay + "秒！");  
                        tModel.setPeriod(Integer.valueOf(childs.get(i).getChildText("period")));  
                        logger.debug(childs.get(i).getChildText("period"));  
                        tasks.add(tModel);  
                      
                }  
            } catch (NumberFormatException e) {  
                // TODO Auto-generated catch block  
                e.printStackTrace();  
            } catch (JDOMException e) {  
                // TODO Auto-generated catch block  
                e.printStackTrace();  
            } catch (IOException e) {  
                // TODO Auto-generated catch block  
                e.printStackTrace();  
            }  
  
        } else {  
        	logger.debug("file no exist!");  
  
        }  
        logger.debug("load task config end !");  
        return tasks;  
  
    }  
}
