package porter.util.json;

/**
 * Created by parker on 2015/11/1.
 */
public class JxObject {
    private Boolean _isArray;
    private Object value;

    public JxObject(Boolean _isArray){
        this._isArray = _isArray;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public Boolean isArray() {
        return _isArray;
    }

}
