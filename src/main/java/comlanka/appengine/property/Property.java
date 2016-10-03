package comlanka.appengine.property;

import com.googlecode.objectify.annotation.Entity;

import javax.persistence.Id;
import java.io.Serializable;

/**
 * Created by IntelliJ IDEA.
 * User: ishara
 * Date: 9/3/11
 * Time: 2:12 PM
 * To change this template use File | Settings | File Templates.
 */
@Entity
public class Property implements Serializable{
    @Id
    String code;
    String value;

    public Property() {
    }

    public Property(String code, String value) {
        this.code = code;
        this.value = value;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
