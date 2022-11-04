package org.example.prometheus;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.Tag;
import io.vertx.micrometer.backends.BackendRegistries;
import java.util.List;
import java.util.Optional;


public class Prometheus {

	public static final String CONSUMED = "consumed";
	public static final String CONSUMED_OK = "consumedOK";
	public static final String CONSUMED_ERROR = "consumedERROR";

	public void writeMetrics(String metricName) {
		Optional.ofNullable(BackendRegistries.getDefaultNow())
			.ifPresent(registry ->
				Counter.builder(metricName)
					.description("Amount of " + metricName)
					.register(registry)
					.increment()
			);
	}

	public void writeMetricsTag(String metricName, Tag tag) {
		Optional.ofNullable(BackendRegistries.getDefaultNow())
			.ifPresent(registry ->
				Counter.builder(metricName + ".node.count")
					.description("Amount of " + metricName)
					.tag(tag.getKey(), tag.getValue())
					.register(registry)
					.increment()
			);
	}

	public void writeMetricsTags(String metricName, List<Tag> tags) {
		Optional.ofNullable(BackendRegistries.getDefaultNow())
			.ifPresent(registry ->
				Counter.builder(metricName + ".node.count")
					.description("Amount of " + metricName)
					.tags(tags)
					.register(registry)
					.increment()
			);
	}

}
