package org.jasig.services.persondir.support.jdbc;

import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.sql.DataSource;

import org.apache.commons.lang.Validate;
import org.jasig.services.persondir.IPersonAttributes;
import org.jasig.services.persondir.support.NamedPersonImpl;
import org.springframework.dao.support.DataAccessUtils;

public class Kan21SingleRowJdbcPersonAttributeDao extends SingleRowJdbcPersonAttributeDao {

	public Kan21SingleRowJdbcPersonAttributeDao(DataSource ds, String sql) {
		super(ds, sql);
	}

	@Override
	public IPersonAttributes getPerson(String uid) {
        Validate.notNull(uid, "uid may not be null.");
        
        // weisd 因为是第三方登录那么可能是第三方的uid 查询的时候需要转换成我方
        // 同时需要验证 第三方的id和我方一致
        
        //Generate the seed map for the uid
        final Map<String, List<Object>> seed = this.toSeedMap(uid);
        
        //Run the query using the seed
        final Set<IPersonAttributes> people = this.getPeopleWithMultivaluedAttributes(seed);
        
        //Ensure a single result is returned
        IPersonAttributes person = (IPersonAttributes)DataAccessUtils.singleResult(people);
        if (person == null) {
            return null;
        }
        
        //Force set the name of the returned IPersonAttributes if it isn't provided in the return object
        if (person.getName() == null) {
            person = new NamedPersonImpl(uid, person.getAttributes());
        }
        
        return person;
		
	}

	
	
}