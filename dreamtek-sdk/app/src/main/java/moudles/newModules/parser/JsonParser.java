package moudles.newModules.parser;

import android.util.Log;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by RuihaoS on 2021/5/8.
 */
public class JsonParser<T> implements BaseParser<T> {

    private ParserListener listener;
//    private volatile static JsonParser singleton;
//
//    private JsonParser() {
//    }
//
//    public static JsonParser getInstance() {
//        if (singleton == null) {
//            synchronized (JsonParser.class) {
//                if (singleton == null) {
//                    singleton = new JsonParser();
//                }
//            }
//        }
//        return singleton;
//    }

    @Override
    public ArrayList<T> parse(Class<T> clazz,String path) {
        ArrayList<T> list = new ArrayList<>();
        StringBuffer sb = new StringBuffer();
        String inString;
        FileReader fr = null;
        BufferedReader inStream = null;
        try {
            fr = new FileReader(new File(path));
            inStream = new BufferedReader(fr);

            while ((inString = inStream.readLine()) != null) {
                sb.append(inString);
            }
            inStream.close();
            fr.close();
        } catch (IOException e) {
            e.printStackTrace();

        }

        Log.i("json", "json string = " + sb.toString());
        JSONArray objects = null;
        try {
            objects = JSON.parseArray(sb.toString());
            for (int i = 0; i < objects.size(); i++) {
                T singleCase = objects.getJSONObject(i).toJavaObject(clazz);
                list.add(singleCase);
            }
        } catch (Exception e) {
            Log.i("json", "error: " + e.getMessage());
        }
        return list;
    }

    public void setListener(ParserListener listener) {
        this.listener = listener;
    }

    public interface ParserListener {
        void onFinish();
    }
}
