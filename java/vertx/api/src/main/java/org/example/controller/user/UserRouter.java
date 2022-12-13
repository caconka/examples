package org.example.controller.user;

import io.vertx.rxjava3.ext.web.openapi.RouterBuilder;

public class UserRouter {

	private final UserController userController;

	public UserRouter(UserController userController) {
		this.userController = userController;
	}

	public RouterBuilder configure(RouterBuilder routerBuilder) {
		routerBuilder.operation("createUser")
			.handler(userController::createUser);

		routerBuilder.operation("getUserById")
			.handler(userController::findUserById);

		routerBuilder.operation("deleteUser")
			.handler(userController::deleteUser);

		return routerBuilder;
	}

}
