package comlanka.appengine.property;

import com.googlecode.objectify.Objectify;
import com.googlecode.objectify.ObjectifyService;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: ishara
 * Date: 9/3/11
 * Time: 10:17 AM
 * Changed Konstantin Chvilyov 2016
 */
public class ASP {
    static {
        ObjectifyService.register(Property.class);
    }
/*
    public static List<Property> getProperty()
    {
        Objectify ofy = ofy();
        return ofy.query(Property.class).list();
    }
*/
    public static Property getProperty(String code)
    {
       Objectify ofy = ofy();
       //return ofy.query(Property.class).filter("code", code).get();
       return ofy.load().type(Property.class).id(code).now();
    }
    public static void setPropertyValue(String code,String value) {
        Objectify ofy = ofy();
        Property property = getProperty(code);
        if (property == null) {
            Property newProperty = new Property(code, value);
            //ofy.put(newProperty);
            ofy.save().entity(newProperty).now();
        }
        else {
            property.setValue(value);
            //ofy.put(property);
            ofy.save().entity(property).now();
        }
    }

    public static Objectify ofy() {
        return ObjectifyService.ofy();//prior to v.4.0 use .begin() , 
                                 //since v.4.0  use ObjectifyService.ofy();
    }
}
