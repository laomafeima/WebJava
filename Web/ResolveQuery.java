/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Web;

import java.util.HashMap;

/**
 * 解析处理URL Query
 *
 * @author 马
 */
public class ResolveQuery {

    public static HashMap query(String query) {
        HashMap hashMap = new HashMap();
        String[] split = query.split("&");
        for (int i = 0; i < split.length; i++) {
            String string = split[i];
            String[] kvsplit = string.split("=",2);
            hashMap.put(java.net.URLDecoder.decode(kvsplit[0]), java.net.URLDecoder.decode(kvsplit[1]));
        }
        return hashMap;
    }
}
