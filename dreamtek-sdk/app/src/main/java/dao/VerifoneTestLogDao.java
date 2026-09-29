package dao;

import android.content.Context;
import android.util.Log;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import Utils.JdbcUtil;
import entity.VTestLog;

/**

 * @author wlj
 */
public class VerifoneTestLogDao {

    private static final String TAG = "VerifoneTestLogDao";

    JdbcUtil jdbcUtil = JdbcUtil.getInstance();


    Connection conn = jdbcUtil.getConnection("test_log","root","123456");


    public  boolean insertLog(VTestLog vTestLog){
        if (conn==null){
            Log.i(TAG,"insertLog:conn is null");
            return false;
        }else {

            String sql = "insert into v_test_log(moudle_name,method_name,request_params,back_value,log_content,result,create_time) " +
                    "values(?,?,?,?,?,?,?)";
            try {
                PreparedStatement pre = conn.prepareStatement(sql);
                pre.setString(1,vTestLog.getMoudleName());
                pre.setString(2,vTestLog.getMethodName());
                pre.setString(3,vTestLog.getRequestParams());
                pre.setString(4,vTestLog.getBackValue());
                pre.setString(5,vTestLog.getLogContent());
                pre.setString(6,vTestLog.getResult());
                pre.setString(7,vTestLog.getCreateTime());
                return pre.execute();
            } catch (SQLException e) {
                return false;
            }finally {
                try {
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void test(){
        System.out.println("test");
    }
}
