package porter.util.xmljson;

import io.vertx.core.json.JsonObject;

import java.sql.Date;

/**
 * Created by parker on 2015/11/1.
 */
public class JxXmlJsonReader {

    private JsonObject json;

    public JxXmlJsonReader(JsonObject json){
        this.json = json;
    }

    public int getInteger(String key){
        return Integer.parseInt(json.getString(key));
    }

    public double getDouble(String key){
        return Double.parseDouble(json.getString(key));
    }

    public long getLong(String key){
        return Long.parseLong(json.getString(key));
    }

    public float getFloat(String key){
        return Float.parseFloat(json.getString(key));
    }

    public boolean getBoolean(String key){
        return Boolean.parseBoolean(json.getString(key));
    }

    public String getString(String key){
        return json.getString(key);
    }
}
