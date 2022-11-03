package org.example;

import static java.util.Objects.isNull;

import org.example.config.Config;
import org.example.http.router.Router;
import org.example.http.handler.FailureHandler;
import org.example.http.handler.HealthCheckHandler;
import org.example.http.handler.UserHandler;

public class IoC {

	private static IoC instance = null;

	public final Config config;
	public final Router router;

	private IoC() {
		config = Config.getInstance();
		router = new Router(new HealthCheckHandler(), new FailureHandler(), new UserHandler());
	}

	public static synchronized IoC getInstance() {
		if (isNull(instance)) {
			instance = new IoC();
		}

		return instance;
	}

}
