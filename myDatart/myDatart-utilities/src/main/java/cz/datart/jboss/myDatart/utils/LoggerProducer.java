package cz.datart.jboss.myDatart.utils;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.InjectionPoint;

import org.apache.log4j.Logger;

@ApplicationScoped
public class LoggerProducer {

	@Produces
	public Logger getLogger(InjectionPoint p) {
		return Logger.getLogger(p.getMember().getDeclaringClass().getName());
	}
}
