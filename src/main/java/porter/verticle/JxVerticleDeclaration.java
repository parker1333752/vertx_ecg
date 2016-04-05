package porter.verticle;

import io.vertx.core.json.JsonObject;

/**
 * Created by parker on 2015/10/14.
 */
public class JxVerticleDeclaration {
    private String className;
    private Integer number;
    private JsonObject configuration;

    public String getVerticleClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public JsonObject getConfiguration() {
        return configuration;
    }

    public void setConfiguration(JsonObject configuration) {
        this.configuration = configuration;
    }
}
