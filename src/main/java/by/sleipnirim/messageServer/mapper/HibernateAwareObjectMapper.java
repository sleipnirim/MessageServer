package by.sleipnirim.messageServer.mapper;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.hibernate5.Hibernate5Module;

/**
 * Created by sleipnir on 20.3.17.
 */
public class HibernateAwareObjectMapper extends ObjectMapper{

    public HibernateAwareObjectMapper(){
        super.registerModule(new Hibernate5Module().configure(Hibernate5Module.Feature.FORCE_LAZY_LOADING, true));
    }
}
