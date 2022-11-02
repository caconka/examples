import java.io.Reader;
import java.io.StringWriter;
import java.io.IOException;

import static net.javacrumbs.jsonunit.core.util.ResourceUtils.resource;
import static net.javacrumbs.jsonunit.JsonAssert.assertJsonEquals;
/*
   <dependency>
	   <groupId>io.netty</groupId>
	   <artifactId>netty-tcnative-boringssl-static</artifactId>
	   <scope>runtime</scope>
	   <version>2.0.25.Final</version>
   </dependency>
*/

public class FromFile {

	private String create_string_from_file(final String filename) {
		final Reader reader = resource(filename);
		final StringWriter stringWriter = new StringWriter();
		try {
			reader.transferTo(stringWriter);
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
		stringWriter.flush();

		return stringWriter.toString();
	}

	private void and_response_is_like_file(final String filename) {
		verify(response).end(responseBody.capture());
		final Reader expected = resource(filename);

		assertJsonEquals(expected, responseBody.getValue());
	}
}
