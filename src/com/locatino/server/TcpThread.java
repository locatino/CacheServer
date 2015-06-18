package com.locatino.server;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ConcurrentLinkedQueue;

import com.locatino.util.Logger;


public class TcpThread implements Runnable  {
	private static final Logger logger = Logger.getLogger(TcpThread.class);
	ServerSocket server;
	//socket queue
	static ConcurrentLinkedQueue<Socket> requestQueue = new ConcurrentLinkedQueue<Socket>();
	
	public void init() throws IOException{
		server = new ServerSocket(Constant.TCP_PORT);
	}
	
	@Override
	public void run() {
		try {
			init();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		while(true){
			try {
				Socket socket = server.accept();
				if(socket != null){
					requestQueue.add(socket);
					logger.info("receive socket! requestQ size: "+requestQueue.size());
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}
