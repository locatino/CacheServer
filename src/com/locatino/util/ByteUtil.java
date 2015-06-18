package com.locatino.util;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.log4j.Logger;

public class ByteUtil {
	private static final Logger logger = Logger.getLogger(ByteUtil.class);
	
	/**
	 * read bytes from stream
	 * @param is
	 * @return
	 * @throws IOException
	 */
	public static byte[] getBytes(InputStream is) throws IOException{
        int nIdx = 0;
        int nReadLen = 0;
        BufferedInputStream bis = new BufferedInputStream(is);
        byte[] len = new byte[4];
        bis.read(len);
        int nTotalLen =byteArrayToInt(len);
  //      logger.debug("nTotalLen: "+nTotalLen);
        byte[] buffer = new byte[nTotalLen];
        while (nIdx < nTotalLen)
        {
            nReadLen = bis.read(buffer, nIdx, nTotalLen - nIdx);
     //       logger.debug("read 1 byte!");
    //        logger.debug("nReadLen: "+nReadLen);
           
            if (nReadLen > 0)
            {
                nIdx = nIdx + nReadLen;
            }
            else if(bis.available() <= 0){
            	 logger.debug("break 1!");
           	 break;
            }
            else
            {
            	 logger.debug("break 2!");
                break;
            }
        }
        return buffer;
	}
	 public static int byteArrayToInt(byte[] bytes) {
        int value= 0;
        //由高位到低位
        for (int i = 0; i < 4; i++) {
            int shift= (4 - 1 - i) * 8;
            value +=(bytes[i] & 0x000000FF) << shift;//往高位游
        }
        return value;
  }
	 
		public static byte[] intToByteArray(int i) {   
			  byte[] result = new byte[4];   
			  result[0] = (byte)((i >> 24) & 0xFF);
			  result[1] = (byte)((i >> 16) & 0xFF);
			  result[2] = (byte)((i >> 8) & 0xFF); 
			  result[3] = (byte)(i & 0xFF);
			  return result;
			}
}
