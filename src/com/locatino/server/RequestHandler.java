package com.locatino.server;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.Socket;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.locatino.cacher.CacheDatabase;
import com.locatino.util.ByteUtil;
import com.locatino.util.Logger;

/**
 * socket handler
 * @author locatino
 *
 */
public class RequestHandler implements Runnable {
	private static final Logger logger = Logger.getLogger(RequestHandler.class);
	
	@SuppressWarnings("unused")
	@Override
	public void run() {
		logger.info("RequestHandler start!!");
		while(true){
		//pool request
		Socket socket = TcpThread.requestQueue.poll();
		if(socket == null)continue;
		if(socket.isClosed()){
			logger.warn("socket is closed!");
			continue;
		}
		logger.info("poll a request! requestQ size: " + TcpThread.requestQueue.size());
		try {
			byte[] request = ByteUtil.getBytes(socket.getInputStream());
			JSONArray requestArray = JSONArray.fromObject(new String(request));
			logger.info("Received   message: " + requestArray);
			
			
			JSONArray respJSON = new JSONArray();
			//protocol
			for(int i = 0; i < requestArray.size(); i++){
				JSONObject json = JSONObject.fromObject(requestArray.get(i));
				int source = json.getInt("source");
				int op = json.getInt("op");
				String clazz = json.getString("clazz");
				String data = json.getString("data");
				
				//from cmdcenter
				if(source == Constant.SOURCE_FROM_CMDCENTER){
					//exec cache sql
					if(op == Constant.OP_EXEC_CACHE_SQL){
						respJSON.add(CacheDatabase.execSql(op, data));
					}
					//exec oracle sql
					if(op == Constant.OP_EXEC_ORACLE_SQL){
						respJSON.add(CacheDatabase.execSql(op, data));
					}
				}
				//from processor
				if(source == Constant.SOURCE_FROM_PROCESSOR){
					
				}
				
			}
			byte[] serializedResult = respJSON.toString().getBytes();
			logger.info("result length: " + serializedResult.length);
			socket.getOutputStream().write(ByteUtil.intToByteArray(serializedResult.length));
			socket.getOutputStream().write(serializedResult);
			socket.getOutputStream().flush();
                // send response
           socket.getOutputStream().close();
           socket.close();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch(Exception e){
			e.printStackTrace();
		}
		}
		
	}

}
