package Web;

import java.util.HashMap;

/**
 * @author Ma
 */
public class BaseTemplate {

    public HashMap param;

    public BaseTemplate() {
        this.param = new HashMap();
    }

    public String display() {
        return null;
    }

    public String get(String s) {
        if (this.param.containsKey(s)) {
            return this.param.get(s).toString();
        } else {
            return null;
        }

    }

    /**
     * 获取boolean值
     *
     * @param s
     * @return
     */
    public boolean getBoolean(String s) {
        Boolean bool = (Boolean) this.param.get(s);
        return bool.booleanValue();
    }

    /**
     * 获取int值
     *
     * @param s
     * @return
     */
    public int getInt(String s) {
        Integer num = (Integer) this.param.get(s);
        return num.intValue();
    }

    /**
     * 获取一维数组
     *
     * @param s
     * @return
     */
    public Object[] getArray(String s) {
        return (Object[]) this.param.get(s);
    }

    /**
     * 获取二维数组
     *
     * @param s
     * @return
     */
    public Object[][] getArray2(String s) {
        return (Object[][]) this.param.get(s);
    }

    /**
     * 赋值操作
     */
    public void assign(String key, String value) {
        this.param.put(key, value);
    }

    /**
     * 赋值操作
     */
    public void assign(String key, int value) {
        this.param.put(key, value);
    }

    /**
     * 赋值操作
     */
    public void assign(String key, boolean value) {
        this.param.put(key, value);
    }

    /**
     * 赋值操作
     */
    public void assign(String key, Object[] value) {
        this.param.put(key, value);
    }
}
