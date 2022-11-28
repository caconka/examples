package org.example;

import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Single;
import io.vertx.config.ConfigRetrieverOptions;
import io.vertx.config.ConfigStoreOptions;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.VertxOptions;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.core.json.JsonObject;
import io.vertx.micrometer.MicrometerMetricsOptions;
import io.vertx.micrometer.VertxPrometheusOptions;
import io.vertx.rxjava3.config.ConfigRetriever;
import io.vertx.rxjava3.core.AbstractVerticle;
import io.vertx.rxjava3.core.Vertx;
import java.util.List;
import org.example.config.Config;
import org.example.domain.logger.Log;
import org.example.verticle.MainVerticle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main {

	private static final Logger LOGGER = LoggerFactory.getLogger(Main.class);

	public static void main(String[] args) {
		var vertx = createVertx();

		getConfig(vertx)
			.flatMap(conf -> {
				Config.getInstance().init(conf);

				var deploymentOptions = new DeploymentOptions()
					.setInstances(1)
					.setConfig(conf);

				return run(Flowable.just(MainVerticle.class), deploymentOptions, vertx);
			})
			.subscribe(
				resList ->
					resList.forEach(res ->
						LOGGER.info(Log.builder().setMessage("Verticle running with id " + res.toLowerCase()).build().toJson())),
				err -> {
					err.printStackTrace();
					LOGGER.error(Log.builder().setMessage("Error starting!! " + err.getMessage()).build().toJson());
					System.exit(1);
				});
	}

	private static Vertx createVertx() {
		var prometheusOptions = new VertxPrometheusOptions()
			.setPublishQuantiles(false)
			.setStartEmbeddedServer(true)
			.setEmbeddedServerOptions(new HttpServerOptions().setPort(9091))
			.setEnabled(true);

		var micrometerOptions = new MicrometerMetricsOptions()
			.setPrometheusOptions(prometheusOptions)
			.setEnabled(true);

		var vertxOptions = new VertxOptions()
			.setEventLoopPoolSize(5)
			.setWorkerPoolSize(1)
			.setInternalBlockingPoolSize(1)
			.setMetricsOptions(micrometerOptions);

		return Vertx.vertx(vertxOptions);
	}

	private static Single<JsonObject> getConfig(Vertx vertx) {
		var configPath = System.getenv("VERTX_CONFIG_PATH");

		if (configPath == null || configPath.isEmpty()) {
			LOGGER.error("Missing VERTX_CONFIG_PATH environment variable.");
			configPath = "config.yaml";
		}

		var store = new ConfigStoreOptions()
			.setType("file")
			.setFormat("yaml")
			.setConfig(new JsonObject().put("path", configPath));

		return ConfigRetriever
			.create(vertx, new ConfigRetrieverOptions().addStore(store))
			.rxGetConfig();
	}

	private static Single<List<String>> run(Flowable<Class<? extends AbstractVerticle>> verticles,
		DeploymentOptions deploymentOptions,
		Vertx vertx) {

		return verticles
			.flatMapSingle(verticle -> vertx.rxDeployVerticle(verticle.getName(), deploymentOptions))
			.toList();
	}

}
