package Handler;

import Web.*;

/**
 * 博客管理模块，做一些基础过滤等
 *
 * @author Ma
 */
public class AdminHandler extends Handler {


    /**
     * 获取用户的ID
     *
     * @return 返回用户当前ID
     */
    public int getUserID() {
        String userID = Cookie.getCookie("id");
        if (userID == null) {
            return 0;
        }
        System.out.println(Integer.parseInt(userID));
        return Integer.parseInt(userID);
    }
    
    
}
