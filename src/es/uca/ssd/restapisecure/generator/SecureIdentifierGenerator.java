package es.uca.ssd.restapisecure.generator;

import java.io.Serializable;
import java.util.UUID;

import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;

public class SecureIdentifierGenerator implements IdentifierGenerator {
	
	public static final String GENERATOR_NAME = "secureIdentifierGenerator";

	@Override
	public Serializable generate(SharedSessionContractImplementor arg0, Object arg1) throws HibernateException {
		return UUID.randomUUID().toString().replace("-", "");
	}

}
