package Web;

import java.nio.channels.SocketChannel;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousFileChannel;
import java.nio.channels.CompletionHandler;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributeView;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.concurrent.TimeUnit;

/**
 * 静态文件服务器
 *
 * @author 马
 */
public class StaticFileServer {

    public Request request;
    public int size;
    public ByteBuffer byteBuffer;
    public SocketChannel socketChannel;
    public Path filePath;
    public String ExName;//扩展名字

    /**
     * 初始化一些东西
     *
     * @param request
     */
    public StaticFileServer(Request request) {
        this.request = request;
        String path = System.getProperty("user.dir").replace("\\", "/");
        path += (Options.StaticPath.startsWith("/") ? Options.StaticPath : "/" + Options.StaticPath);
        path = path + this.request.Path.substring(1);
        this.filePath = Paths.get(path);
        //设置扩展名
        int index = this.request.Path.lastIndexOf(".");
        this.ExName = this.request.Path.substring(index + 1);
    }

    /**
     * 检测文件是否存在
     *
     * @return
     */
    public boolean exists() {
        return Files.exists(filePath);

    }

    /**
     * 检查文件是否需要更新还是304状态吗
     *
     * @return
     */
    public boolean update() {
        try {
            int lastTime = (int) Files.getLastModifiedTime(filePath).to(TimeUnit.SECONDS);//获取最后修改的时间戳
            Object paramTime = this.request.Header.get("If-Modified-Since");

            if ((paramTime != null) && (Integer.parseInt((String) paramTime) <= lastTime)) {
                //处理304
                Response.setStatus(304);
                Response.setContentType(Response.findContentType(this.ExName));
                Response.header("Last-Modified: " + lastTime);
                Response.header("Cache-Control:public, max-age=2592000");
                byte[] headerBytes = Response.getHeader().getBytes();
                ByteBuffer writerBuffer = ByteBuffer.allocate(headerBytes.length);
                writerBuffer.clear();
                writerBuffer.put(headerBytes);
                writerBuffer.flip();
                while (writerBuffer.hasRemaining()) {
                    this.socketChannel.write(writerBuffer);
                }
                writerBuffer.clear();
                this.socketChannel.close();
                //输出debug信息
                if (Options.DEBUG || Options.ParseLine) {
                    System.out.println(Response.Status + " " + this.request.Header.get("Host") + this.request.Path);
                }
                Response.finish();//清理信息
                return false;
            }
        } catch (IOException ex) {
            Logger.getLogger(StaticFileServer.class.getName()).log(Level.SEVERE, null, ex);
        }
        return true;
    }

    public void read(SocketChannel socketChannel) {
        this.socketChannel = socketChannel;
        //处理304
        if (this.update()) {
            //输出debug信息
            if (Options.DEBUG || Options.ParseLine) {
                System.out.println("Static File" + " " + this.request.Header.get("Host") + this.request.Path);
            }
            //处理200
            BasicFileAttributeView basicView = Files.getFileAttributeView(this.filePath, BasicFileAttributeView.class);

            try {
                BasicFileAttributes basicAttrs = basicView.readAttributes();
                long size = basicAttrs.size();
                if (size > 2097152) {
                    //说明文件大于2M
                }
                this.size = (int) size;
                int lastTime = (int) Files.getLastModifiedTime(filePath).to(TimeUnit.SECONDS);//获取最后修改的时间戳
                this.byteBuffer = ByteBuffer.allocate((int) size);//申请缓存空间
                //开始获取通道，读取文件
                AsynchronousFileChannel afc = AsynchronousFileChannel.open(this.filePath);
                afc.read(byteBuffer, 0, lastTime, new CompletionHandlerImpl(afc));

            } catch (IOException ex) {
                //处理500错误
                Logger.getLogger(StaticFileServer.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    /**
     * 成员类 处理文件完成后的操作
     */
    private class CompletionHandlerImpl implements CompletionHandler<Integer, Object> {

        AsynchronousFileChannel afc;

        public CompletionHandlerImpl(AsynchronousFileChannel afc) {
            this.afc = afc;
        }

        /**
         * 处理成功的操作
         *
         * @param result
         * @param attachment
         */
        @Override
        public void completed(Integer result, Object attachment) {

            Integer lastModifyInteger = (Integer) attachment;
            int lastModify = lastModifyInteger.intValue();
            //Header不能使用Response
            String header = "HTTP/1.1 200 OK\r\n"
                    + "Content-type: " + Response.findContentType(StaticFileServer.this.ExName) + ";\r\n"
                    + "Content-ength: " + StaticFileServer.this.size + "\r\n"
                    + "Last-Modified: " + lastModify + "\r\n"
                    + "Cache-Control:public, max-age=2592000 \r\n"
                    + "Server: " + Response.Server + "\r\n\r\n";

            byte[] headerBytes = header.getBytes();
            byte[] contentBytes = StaticFileServer.this.byteBuffer.array();
            ByteBuffer writerBuffer = ByteBuffer.allocate(headerBytes.length + contentBytes.length);
            writerBuffer.clear();

            writerBuffer.put(headerBytes);
            writerBuffer.put(contentBytes);
            writerBuffer.flip();
            try {
                while (writerBuffer.hasRemaining()) {
                    StaticFileServer.this.socketChannel.write(writerBuffer);
                }
                writerBuffer.clear();

                StaticFileServer.this.socketChannel.close();
                this.afc.close();
            } catch (IOException ex) {
                Logger.getLogger(StaticFileServer.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        /**
         * 处理失败的操作
         *
         * @param exc
         * @param attachment
         */
        @Override
        public void failed(Throwable exc, Object attachment) {
            System.out.println(exc.getCause());
            //@todo 处理500错误
            try {
                StaticFileServer.this.socketChannel.close();
                this.afc.close();
            } catch (IOException ex) {
                Logger.getLogger(StaticFileServer.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
