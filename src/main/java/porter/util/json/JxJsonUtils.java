package porter.util.json;

import net.sf.json.*;

import java.util.Collections;
import java.util.Map;
import java.util.Set;

/**
 * Created by parker on 2015/11/1.
 */
public class JxJsonUtils {
    public static String bean2json(Object bean) {
        return JSONObject.fromObject(bean).toString();
    }

    public static JxObject json2bean(String jsonstr, Class type) {
        JSON jsonobj = JSONSerializer.toJSON(jsonstr);
        JxObject rt;
        if (jsonobj instanceof JSONObject) {
            rt = new JxObject(false);
            rt.setValue(JSONObject.toBean((JSONObject) jsonobj, type));
            return rt;
        } else if (jsonobj instanceof JSONArray) {
            rt = new JxObject(true);
            JSONArray.toArray((JSONArray) jsonobj, type);
            return rt;
        } else return null;
    }

    public static Set<Map.Entry<String, Object>> json2set(String jsonstr){
        try{
            JSON jsonobj = JSONSerializer.toJSON(jsonstr);
            if(jsonobj instanceof JSONArray){
                return null;
            }else if(jsonobj instanceof JSONObject){
                return ((JSONObject) jsonobj).entrySet();
            }else return null;
        }catch (Exception e)
        {
            return null;
        }
    }
}
