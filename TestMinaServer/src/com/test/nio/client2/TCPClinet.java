package com.test.nio.client2; 

import java.net.InetSocketAddress; 
import java.nio.ByteBuffer; 
import java.nio.channels.SelectionKey; 
import java.nio.channels.Selector; 
import java.nio.channels.SocketChannel; 
/** 
* 客户端程序 
* @author Administrator 
* 
*/ 

public class TCPClinet { 
    private Selector selector; 
    
    SocketChannel channel; 
    
    private String ip; 
    private int port; 
    
    public TCPClinet(String ip,int port) throws Exception{ 
        this.ip = ip; 
        this.port = port; 
        //sendCmd(); 
        init(); 
    } 
    
    public void init() throws Exception{ 
        channel = SocketChannel.open(new InetSocketAddress(ip, port)); 
        channel.configureBlocking(false); 
        
        selector = Selector.open(); 
        channel.register(selector, SelectionKey.OP_READ); 
        new TCPClientReadThread(selector); 
    } 
    /** 
     * 发送byte[]数组到读卡器 
     * @throws Exception 
     */ 
    public void sendCmd() throws Exception{ 
        int[] cmd = {0xAC, 0x02, 0x00, 0x08, 0x00, 0x21, 0x40, 0x23, 0x23, 
                0xCD, 0xCE, 0x0D, 0xED, 0xAC, 0x01, 0x00, 0x00, 0x00  }; 
        
        byte[] b = new byte[cmd.length]; 
        for(int i = 0;i< b.length;i++){ 
            b[i] = (byte)cmd[i]; 
        } 
        ByteBuffer buffer = ByteBuffer.allocate(1024); 
        channel.write(buffer); 
    } 
    public static void main(String[] args) { 
        try { 
//        	TCPClinet tcp = new TCPClinet("192.168.14.243", 6655); 
            TCPClinet tcp = new TCPClinet("127.0.0.1", 1978); 
            tcp.sendCmd(); 
        } catch (Exception e) { 
            // TODO Auto-generated catch block 
            e.printStackTrace(); 
        } 
    } 
} 