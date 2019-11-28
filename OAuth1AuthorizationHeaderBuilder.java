import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.util.Arrays;
import java.util.Base64;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class OAuth1AuthorizationHeaderBuilder {
	private static final HashSet<Character> UnreservedChars = new HashSet<>(Arrays.asList(
				'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T',
				'U', 'V', 'W', 'X', 'Y', 'Z', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n',
				'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '0', '1', '2', '3', '4', '5', '6', '7',
				'8', '9', '-', '_', '.', '~'));

	public String consumerSecret;

	public String method;

	public String parameterString;

	public Map<String, String> parameters = new LinkedHashMap<>();

	public String signature;

	public String signatureBaseString;

	public String signingKey;

	public String tokenSecret;

	public String url;

	public OAuth1AuthorizationHeaderBuilder() {
	}

	public static String encodeURIComponent(String s) {
		StringBuilder o = new StringBuilder();
		for (char ch : s.toCharArray()) {
			if (isSafe(ch)) {
				o.append(ch);
			} else {
				o.append('%');
				o.append(toHex(ch>>4));
				o.append(toHex(ch & 0xf));
			}
		}
		return o.toString();
	}

	private static boolean isSafe(char ch) {
		return UnreservedChars.contains(ch);
	}

	private static char toHex(int ch) {
		return (char) (ch < 10 ? '0' + ch : 'A' + ch - 10);
	}

	public String build() {
		final String timestamp = Long.toString(Instant.now().getEpochSecond() - 100);
		if (!parameters.containsKey("oauth_timestamp")) {
			parameters.put("oauth_timestamp", timestamp);
		}

		parameters.put("oauth_nonce", timestamp);
		parameters.put("oauth_signature_method", "HMAC-SHA1");
		parameters.put("oauth_version", "1.0");

		// Build the parameter string after sorting the keys in lexicographic order per the OAuth v1 spec.
		parameterString = parameters.entrySet().stream()
			.sorted(Map.Entry.comparingByKey())
			.map(e -> encodeURIComponent(e.getKey()) + "=" + encodeURIComponent(e.getValue()))
			.collect(Collectors.joining("&"));

		// Build the signature base string
		signatureBaseString = new StringBuilder(method.toUpperCase())
			.append("&")
			.append(encodeURIComponent(url))
			.append("&")
			.append(encodeURIComponent(parameterString))
			.toString();

		// If the signing key was not provided, build it by encoding the consumer secret + the token secret
		if (signingKey == null) {
			signingKey = new StringBuilder(encodeURIComponent(consumerSecret))
				.append("&")
				.append((tokenSecret == null ? "" : encodeURIComponent(tokenSecret)))
				.toString();
		}

		// Sign the Signature Base String
		signature = generateSignature(signingKey, signatureBaseString);

		// Add the signature to be included in the header
		parameters.put("oauth_signature", signature);

		// Build the authorization header value using the order in which the parameters were added
		return "OAuth " + parameters.entrySet().stream()
			.map(e -> encodeURIComponent(e.getKey()) + "=\"" + encodeURIComponent(e.getValue()) + "\"")
			.collect(Collectors.joining(", "));
	}

	public String generateSignature(String secret, String message) {
		try {
			byte[] bytes = secret.getBytes(StandardCharsets.UTF_8);
			Mac mac = Mac.getInstance("HmacSHA1");
			mac.init(new SecretKeySpec(bytes, mac.getAlgorithm()));
			byte[] result = mac.doFinal(message.getBytes(StandardCharsets.UTF_8));
			return Base64.getEncoder().encodeToString(result);
		} catch (InvalidKeyException | NoSuchAlgorithmException e) {
			throw new RuntimeException(e);
		}
	}

	public OAuth1AuthorizationHeaderBuilder withConsumerSecret(String consumerSecret) {
		this.consumerSecret = consumerSecret;
		return this;
	}

	public OAuth1AuthorizationHeaderBuilder withMethod(String method) {
		this.method = method;
		return this;
	}

	public OAuth1AuthorizationHeaderBuilder withParameter(String name, String value) {
		parameters.put(name, value);
		return this;
	}

	public OAuth1AuthorizationHeaderBuilder withTokenSecret(String tokenSecret) {
		this.tokenSecret = tokenSecret;
		return this;
	}

	public OAuth1AuthorizationHeaderBuilder withURL(String url) {
		this.url = url;
		return this;
	}
}
