package Web;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;

import java.util.Iterator;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

public class HttpServer {

    public static void init(int port) {
        try {
            //初始化数据库连接
            DB.connection();
            Selector selector = Selector.open();//创建selector
            ServerSocketChannel serverChannel = ServerSocketChannel.open();
            serverChannel.socket().bind(new InetSocketAddress(port));
            serverChannel.configureBlocking(false);
            serverChannel.register(selector, SelectionKey.OP_ACCEPT);//注册
            SocketChannel channel;
            try {
                while (true) {
                    selector.select();
                    Iterator<SelectionKey> iter = selector.selectedKeys().iterator();
                    while (iter.hasNext()) {

                        SelectionKey key = iter.next();
                        iter.remove();

                        Object attach = key.attachment();
                        //如果有正在读取静态文件的标记就返回
                        if (attach != null && attach.equals(0)) {
                            continue;
                        }


                        //开始处理
                        if (key.isAcceptable()) { // 接收请求   
                            ServerSocketChannel server = (ServerSocketChannel) key.channel();
                            channel = server.accept();
                            //设置非阻塞模式   
                            channel.configureBlocking(false);
                            channel.register(selector, SelectionKey.OP_READ);
                        } else if (key.isReadable()) { // 读信息   
                            channel = (SocketChannel) key.channel();
                            ByteBuffer clientBuffer = ByteBuffer.allocate(1024);
                            int count = channel.read(clientBuffer);
                            String receive = "";
                            while (count > 0) {
                                clientBuffer.flip();
                                CharBuffer charBuffer = Charset.forName("UTF-8").decode(clientBuffer);
                                String temp = charBuffer.toString();
                                receive = receive + temp;
                                clientBuffer.clear();
                                count = channel.read(clientBuffer);
                                channel.register(selector, SelectionKey.OP_WRITE);
                            }

                            key.attach(receive);
                            clientBuffer.clear();
                        } else if (key.isWritable()) { // 写事件   

                            //开始处理事件
                            //开始时间
                            long startTime = System.currentTimeMillis();
                            channel = (SocketChannel) key.channel();
                            boolean accessStaticFile = false;//是不是访问的静态文件
                            // 写事件   
                            Request request = new Request((String) key.attachment());
                            if (request.Path.equals("/favicon.ico") || request.Path.equals("robots.txt") || request.Path.indexOf(Options.StaticPath) == 0) {
                                //##处理
                                //#说明是访问静态文件
                                accessStaticFile = true;
                                StaticFileServer staticFile = new StaticFileServer(request);
                                if (staticFile.exists()) {
                                    staticFile.read(channel);
                                    key.attach(0);//这个标记是正在读取静态文件
                                    continue;//读取下一个循环
                                } else {
                                    //如果不存在
                                    //当作非静态文件处理404 省事
                                    accessStaticFile = false;
                                    Response.setStatus(404);
                                }

                            }
                            //访问的不是静态文件，或者静态文件不存在
                            if (!accessStaticFile) {
                                //###处理请求
                                //获取客户端IP
                                request.setRemoteIP(channel.socket().getLocalAddress().getHostName());
                                //开始分配到Handler
                                Allocation.init(request);

                                //Handler结束

                                if (Response.Content == null) {
                                    Response.setContent("未知错误造成，无返回实体！");
                                    Response.setStatus(500);
                                }
                                byte[] contentBytes = Response.Content.getBytes("UTF-8");
                                //先设置实体长度
                                Response.setContentLength(contentBytes.length);

                                //然后获取header
                                byte[] headerBytes = Response.getHeader().getBytes("UTF-8");
                                ByteBuffer writerBuffer = ByteBuffer.allocate(headerBytes.length + contentBytes.length);
                                writerBuffer.clear();
                                writerBuffer.put(headerBytes);
                                writerBuffer.put(contentBytes);
                                writerBuffer.flip();
                                while (writerBuffer.hasRemaining()) {
                                    channel.write(writerBuffer);
                                }
                                writerBuffer.clear();
                                channel.close();
                                //输出debug信息
                                if (Options.DEBUG || Options.ParseLine) {
                                    System.out.println(Response.Status + " " + request.Header.get("Host") + request.Path + " " + (System.currentTimeMillis() - startTime) + "ms");
                                }
                                //完成响应，清理相应信息
                                Response.finish();
                            }

                        }

                    }
                }
            } finally {
                serverChannel.close();
            }
        } catch (IOException ex) {
            Logger.getLogger(HttpServer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * 设置URL
     *
     * @param path
     * @param handler
     */
    public static void setPATH(String path, Object handler) {
        Allocation.PATH.put(path, handler);
    }

    /**
     * 获取所有的Path
     *
     * @return
     */
    public static String[] getAllPath() {
        String[] pathArray = new String[Allocation.PATH.size()];
        Set set = Allocation.PATH.entrySet();
        Iterator iterator = set.iterator();
        int i = 0;
        while (iterator.hasNext()) {
            java.util.Map.Entry item = (java.util.Map.Entry) iterator.next();
            pathArray[i] = (String) item.getKey();
            i++;
        }
        return pathArray;
    }
}