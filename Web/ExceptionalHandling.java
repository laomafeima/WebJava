package Web;

/**
 * 处理整体异常的
 *
 * @author 马
 */
public class ExceptionalHandling {

    /**
     * 404异常处理
     *
     * @param ex
     */
    public static void set404(Exception ex) {
        NotFound404.set404(ex);
    }

    /**
     * 500错误处理
     *
     * @param ex
     */
    public static void set500(Exception ex) {
        InternalServerError500.set500(ex);
    }
    
    /**
     * 处理其他异常
     * @param ex 
     */
    public static void handling(Exception ex){
        
    }
}
