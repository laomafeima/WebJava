package Web;

/**
 * 数据库操作
 * @author Ma
 */

import com.sun.org.apache.regexp.internal.REUtil;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Types;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DB {

    public static Connection Connection;
    public static int lastUserTime;

    public static void connection() {

        if ((Options.DBDriver != null) && (Options.DBURL != null) && (Options.DBUser != null) && (Options.DBPassword != null)) {
            String driver = Options.DBDriver;         // 驱动程序名
            String url = Options.DBURL;     // URL指向要访问的数据库名
            try {
                Class.forName(driver);    // 加载驱动程序
                Connection conn = DriverManager.getConnection(url, Options.DBUser, Options.DBPassword);      // 连续数据库
                //验证是否连接成功
                if (!conn.isClosed()) {
                    DB.Connection = conn;
                    DB.lastUserTime = (int) (System.currentTimeMillis() / 1000);
                } else {
                    DB.Connection = null;
                }

                //DB.Connection.close();如果会自动处理就不用手动释放资源了
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(Allocation.class.getName()).log(Level.SEVERE, null, ex);
                System.exit(-1);
            } catch (SQLException ex) {
                Logger.getLogger(Allocation.class.getName()).log(Level.SEVERE, null, ex);
                System.exit(-1);
            } catch (Exception ex) {
                Logger.getLogger(Allocation.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    /**
     * 用原生态的executeQuery 返回ResultSet
     *
     * @param sql
     * @param params
     * @return ResultSet
     */
    public static ResultSet executeQuery(String sql, String... params) {
        DB.cursor();
        PreparedStatement statement;
        try {
            statement = DB.Connection.prepareStatement(sql);
            for (int i = 0; i < params.length; i++) {
                statement.setString((i + 1), params[i]);
            }
            ResultSet r = statement.executeQuery();
            return r;
        } catch (SQLException ex) {
            Logger.getLogger(DB.class.getName()).log(Level.SEVERE, null, ex);
        }

        return null;
    }

    /**
     * 用原生态的executeUpdate
     *
     * @param sql
     * @param params
     * @return
     */
    public static int executeUpdate(String sql, String... params) {
        DB.cursor();
        try {
            PreparedStatement statement = DB.Connection.prepareStatement(sql);
            for (int i = 0; i < params.length; i++) {
                statement.setString((i + 1), params[i]);
            }
            int r = statement.executeUpdate();
            statement.close();
            return r;
        } catch (SQLException ex) {
            Logger.getLogger(DB.class.getName()).log(Level.SEVERE, null, ex);
        }
        return 0;

    }

    /**
     * 获取多个结果集
     *
     * @param sql
     * @param params
     */
    public static Object[][] get(String sql, String... params) {
        DB.cursor();
        Object[][] data = null;
        try {
            PreparedStatement statement = DB.Connection.prepareStatement(sql);
            for (int i = 0; i < params.length; i++) {
                statement.setString((i + 1), params[i]);
            }

            ResultSet r = statement.executeQuery();
            //获取字段信息getMetaData
            ResultSetMetaData columns = r.getMetaData();

            int columnCount = columns.getColumnCount();
            String[] columnName = new String[columnCount];
            int[] columnTypeName = new int[columnCount];
            for (int i = 1; i <= columnCount; i++) {
                columnName[i - 1] = columns.getColumnName(i);
                columnTypeName[i - 1] = DB.getTypeName(columns.getColumnType(i));
            }

            r.last();

            int rowCount = r.getRow();
            if (rowCount < 1) {
                return new Object[rowCount][columnCount];
            }
            r.first();
            data = new Object[rowCount][columnCount];
            int i = 0;

            do {
                Object[] temp = new Object[columnCount];
                for (int j = 0; j < columnName.length; j++) {
                    switch (columnTypeName[j]) {
                        case 0:
                            temp[j] = r.getBoolean(j + 1);
                            break;
                        case 1:
                            temp[j] = r.getInt(j + 1);
                            break;
                        default:
                            temp[j] = r.getString(j + 1);
                            break;

                    }
                }

                data[i] = temp;
                i++;
            } while (r.next());
            //随手关闭
            r.close();

            statement.close();
            return data;


        } catch (SQLException ex) {
            Logger.getLogger(Handler.class.getName()).log(Level.SEVERE, null, ex);
        }
        return data;
    }

    /**
     * 获取一行
     *
     * @param sql
     * @param params
     * @return
     */
    public static Object[] find(String sql, String... params) {
        Object[][] r = DB.get(sql, params);
        if (r.length < 1) {
            return new Object[0];
        }
        return r[0];
    }

    /**
     * 获取一个字段
     *
     * @param sql
     * @param params
     * @return
     */
    public static Object getField(String sql, String... params) {
        Object[][] r = DB.get(sql, params);
        if (r.length < 1) {
            return null;
        } else {
            return r[0][0];
        }

    }

    /**
     * 更新数据
     *
     * @param sql
     * @param params
     * @return
     */
    public static int update(String sql, String... params) {
        DB.cursor();
        try {
            PreparedStatement statement = DB.Connection.prepareStatement(sql);
            for (int i = 0; i < params.length; i++) {
                statement.setString((i + 1), params[i]);
            }
            //获取字段信息getMetaData
            int r = statement.executeUpdate();
            statement.close();
            return r;
        } catch (SQLException ex) {
            Logger.getLogger(DB.class.getName()).log(Level.SEVERE, null, ex);
        }
        return 0;
    }

    public static int insert(String sql, String... params) {
        return DB.update(sql, params);
    }

    public static int delete(String sql, String... params) {
        return DB.update(sql, params);
    }

    /**
     * 解析SQL便于开发
     *
     * @param sql
     * @param params
     * @return
     */
    public static String getSQL(String sql, String... params) {
        for (int i = 0; i < params.length; i++) {
            sql = sql.replaceFirst("\\?", sql);
        }
        return sql;
    }

    public static int getTypeName(int typeId) {
        switch (typeId) {
            case Types.NUMERIC:
                //Int 类型
                return 1;
            case Types.BOOLEAN:
                //Boolean类型
                return 0;
            default:
                //其余的当作String处理
                return 2;
        }
    }

    /**
     * 检查链接时间是否过期
     */
    public static void cursor() {
        if ((DB.lastUserTime + Options.DBIdleTime) < System.currentTimeMillis()) {
            DB.connection();
        } else {
            DB.lastUserTime = (int) System.currentTimeMillis() / 1000;
        }
    }
}
