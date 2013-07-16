package com.test.nio.client2; 

import java.io.IOException; 
import java.nio.ByteBuffer; 
import java.nio.channels.SelectionKey; 
import java.nio.channels.Selector; 
import java.nio.channels.SocketChannel; 
import java.util.Iterator; 
import java.util.Set; 

public class TCPClientReadThread implements Runnable { 
    // 监听器，如果缓冲区有数据，通知程序接收 
    private Selector selector; 

    public TCPClientReadThread(Selector selector) { 
        this.selector = selector; 
        new Thread(this).start(); 
    } 

    @Override 
    public void run() { 
        try { 
            while (selector.select() > 0) { 
                Set<SelectionKey> keys = selector.selectedKeys(); 
                Iterator<SelectionKey> iter = keys.iterator(); 
                while (iter.hasNext()) { 
                    SelectionKey key = (SelectionKey) iter.next(); 
                    iter.remove(); 
                    System.out.println(selector.keys().size()); 

                    if (key.isReadable()) { 
                        SocketChannel socketChannel = (SocketChannel) key 
                                .channel(); 
                        ByteBuffer buffer = ByteBuffer.allocate(1024); 
                        int byteRead = socketChannel.read(buffer); 
                        buffer.clear(); 
                        if (byteRead == -1) { 
                            socketChannel.close(); 
                        } else { 
                            buffer.flip(); 
                            byte[] content = new byte[buffer.limit()]; 
                            // 从ByteBuffer中读取数据到byte数组中 
                            buffer.get(content); 

                            String receivedString = btyetoString(content); 
                            // String receivedString = 
                            // Charset.forName("UTF-16").newDecoder().decode(buffer).toString(); 
                            System.out.println("接收到来自服务器" 
                                    + socketChannel.socket() 
                                            .getRemoteSocketAddress() + "的信息:" 
                                    + receivedString); 
                            // 发送确认命令给服务器 
                            int[] cmds = { 0xAC, 0x10, 0xDA, 0x00, 0x00 }; 

                            byte[] bb = new byte[cmds.length]; 
                            for (int i = 0; i < bb.length; i++) { 
                                bb[i] = (byte) cmds[i]; 
                            } 
                            // buffer.get(bb); 
                            socketChannel.write(buffer); 
                            key.interestOps(SelectionKey.OP_READ 
                                    | SelectionKey.OP_WRITE); 
                        } 

                    } 
                } 
            } 
        } catch (IOException e) { 
            // TODO Auto-generated catch block 
            e.printStackTrace(); 
        } 

    } 

    public static final String btyetoString(byte[] bArray) { 
        StringBuffer sb = new StringBuffer(bArray.length); 
        String sTemp; 
        for (int i = 0; i < bArray.length; i++) { 
            sTemp = Integer.toHexString(0xFF & bArray[i]); 
            if (sTemp.length() == 1) { 
                sb.append(0); 

            } 
            sb.append(sTemp.toUpperCase()); 
            sb.append(" "); 
        } 
        return sb.toString(); 
    } 

} 