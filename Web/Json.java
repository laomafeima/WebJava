package Web;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Json处理，只处理声称Json字符串
 *
 * @author 马
 */
public class Json {

    public static String encode(HashMap map) {
        Iterator iters = map.entrySet().iterator();

        String json = "{";
        while (iters.hasNext()) {
            Map.Entry entry = (Map.Entry) iters.next();
            Object key = entry.getKey();
            Object val = entry.getValue();
            json += key.toString() + ":";
            if (val instanceof HashMap) {
                json += Json.encode((HashMap) val) + ",";
            } else {
                json = json + "\"" + val.toString() + "\",";
            }
        }
        json = json.substring(0, json.length() - 1) + "}";
        return json;
    }
}
