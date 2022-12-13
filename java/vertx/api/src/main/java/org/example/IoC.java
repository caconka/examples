package org.example;

import static java.util.Objects.isNull;

import org.example.config.Config;
import org.example.controller.Router;
import org.example.controller.handler.FailureHandler;
import org.example.controller.handler.HealthCheckHandler;
import org.example.controller.user.UserController;
import org.example.controller.user.UserRouter;

public class IoC {

	private static IoC instance = null;

	public final Config config;
	public final Router router;

	private IoC() {
		config = Config.getInstance();

		var userRouter = new UserRouter(new UserController());
		router = new Router(new HealthCheckHandler(), new FailureHandler(), userRouter);
	}

	public static synchronized IoC getInstance() {
		if (isNull(instance)) {
			instance = new IoC();
		}

		return instance;
	}

}
