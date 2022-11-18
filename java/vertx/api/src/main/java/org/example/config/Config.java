package org.example.config;

import io.vertx.core.impl.logging.Logger;
import io.vertx.core.impl.logging.LoggerFactory;
import io.vertx.core.json.JsonObject;
import java.util.Optional;
import org.example.domain.exception.InvalidConfigException;
import org.example.domain.logger.Log;

public class Config {

	private static final Logger LOGGER = LoggerFactory.getLogger(Config.class);
	private static final Byte LOCK = (byte) 0;
	private static Config instance;

	public static final int DEFAULT_PORT = 8080;

	private JsonObject config;

	public static Config getInstance() {
		if (instance == null) {
			synchronized (LOCK) {
				if (instance == null) {
					instance = new Config();
				}
			}
		}

		return instance;
	}

	public void init(JsonObject config) {
		this.config = config;
	}

	private Object getValue(String pattern) {
		return getValue(pattern, null);
	}

	private <T> T getValue(String pattern, T defaultValue) {
		var accessors = pattern.split("\\.");
		var numAccessors = accessors.length;
		var lastAccessorIndex = numAccessors - 1;
		var conf = config;

		for (var i = 0; i < lastAccessorIndex; ++i) {
			conf = conf.getJsonObject(accessors[i]);
		}

		if (conf == null) {
			LOGGER.info(
				Log.builder()
					.setMessage("Config not found : " + pattern + " defaultValue: " + defaultValue)
					.build().toJson());

			return defaultValue;
		}

		return (T) conf.getValue(accessors[lastAccessorIndex], defaultValue);
	}

	private Object getValueOrThrow(String pattern) {
		return Optional.ofNullable(getValue(pattern))
			.orElseThrow(() -> new InvalidConfigException(pattern));
	}

	public Integer getPort() {
		return getValue("server.port", DEFAULT_PORT);
	}
}
