package br.com.laersondev.goldenraspberryawardsapi.util;

import static br.com.laersondev.goldenraspberryawardsapi.util.Precondition.checkIfNotNull;

import java.util.function.Consumer;

import javax.enterprise.context.control.RequestContextController;
import javax.enterprise.inject.spi.CDI;

import org.jboss.logging.Logger;

public class CDIContexts {

	private final static Logger LOG = Logger.getLogger(CDIContexts.class);


	public static <T> void runInRequestScope(Consumer<T> doInReqContext, T params) {
		final RequestContextController requestContextController = CDI.current().select(RequestContextController.class).get();
		final boolean activated = requestContextController.activate();
		if (activated) {
			LOG.info("New request scope activated");
		}
		try {
			checkIfNotNull(doInReqContext, "doInReqContext").accept(params);
		} finally {
			requestContextController.deactivate();
		}
	}

}
