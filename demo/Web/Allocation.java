/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Web;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 检查path，分配调用的handler
 *
 * @author Ma
 */
public class Allocation {

    /**
     * 路径配置
     */
    public static HashMap PATH = new HashMap();

    /**
     * 初始化，分配器
     *
     * @param request 请求header
     */
    public static void init(Request request) {


        //说明访问的非静态文件
        String[] pathArray = HttpServer.getAllPath();
        for (int i = 0; i < pathArray.length; i++) {
            Pattern pattern = Pattern.compile(pathArray[i]);

            Matcher matcher = pattern.matcher(request.Path);

            if (matcher.matches()) {
                try {
                    Class handler = Allocation.PATH.get(pathArray[i]).getClass();
                    //处理参数
                    Class[] paramType = new Class[matcher.groupCount()];
                    Arrays.fill(paramType, String.class);
                    Method method = handler.getMethod(request.Method.toLowerCase(), paramType);
                    //把request赋值过去 使用
                    handler.getMethod("init", new Class[]{Request.class}).invoke(Allocation.PATH.get(pathArray[i]), request);
                    //调用类似构造方法；
                    handler.getMethod("construct").invoke(Allocation.PATH.get(pathArray[i]));
                    String[] param = new String[matcher.groupCount()];
                    for (int j = 0; j < param.length; j++) {
                        param[j] = matcher.group(j + 1);

                    }
                    method.invoke(Allocation.PATH.get(pathArray[i]), param);

                } catch (NoSuchMethodException ex) {
                    //Handler中方法不存在
                    ExceptionalHandling.set404(ex);
                    Logger.getLogger(Allocation.class.getName()).log(Level.SEVERE, null, ex);
                } catch (SecurityException ex) {
                    //安全异常。由安全管理器抛出，用于指示违反安全情况的异常。
                    Logger.getLogger(Allocation.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IllegalAccessException ex) {
                    //设置成了私有方法，应该为public
                    InternalServerError500.set500(ex);
                    Logger.getLogger(Allocation.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IllegalArgumentException ex) {
                    //调用方法的时候传递的参数不正确
                    InternalServerError500.set500(ex);
                    Logger.getLogger(Allocation.class.getName()).log(Level.SEVERE, null, ex);
                } catch (InvocationTargetException ex) {
                    //ex.getCause() 才能获取真实的异常
                    //一般是构造方法的错误。所以Handler中的构造方法不能有参数
                    Logger.getLogger(Allocation.class.getName()).log(Level.SEVERE, null, ex.getCause());
                } catch (Exception ex) {
                    //这里处理其他异常，因为是用反射运行的这里要处理异常
                    Logger.getLogger(Allocation.class.getName()).log(Level.SEVERE, null, ex);
                }
                //如果有就直接Return，终止往下执行，下面处理没找到。
                return;
            } else {

                continue;
            }
        }

        //执行到这里，说明没有找到匹配的URL发送404错误

        NotFound404.set404("未设置对应URL：" + request.Path);

    }

}
