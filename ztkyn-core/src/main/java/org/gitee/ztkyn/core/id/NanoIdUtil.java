package org.gitee.ztkyn.core.id;

import java.security.SecureRandom;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author richard
 * @version 1.0.0
 * @date 2023-06-20 09:28
 * @description NanoIdUtil
 */
public class NanoIdUtil {

	private static final Logger logger = LoggerFactory.getLogger(NanoIdUtil.class);

	/**
	 * <code>NanoIdUtils</code> instances should NOT be constructed in standard
	 * programming. Instead, the class should be used as
	 * <code>NanoIdUtils.randomNanoId();</code>.
	 */
	private NanoIdUtil() {
		// Do Nothing
	}

	/**
	 * The default random number generator used by this class. Creates cryptographically
	 * strong NanoId Strings.
	 */
	public static final SecureRandom DEFAULT_NUMBER_GENERATOR = new SecureRandom();

	/**
	 * The default alphabet used by this class. Creates url-friendly NanoId Strings using
	 * 64 unique symbols.
	 */
	public static final char[] DEFAULT_ALPHABET = "_-0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ"
		.toCharArray();

	public static final char[] DEFAULT_NUMBER = "0123456789".toCharArray();

	/**
	 * The default size used by this class. Creates NanoId Strings with slightly more
	 * unique values than UUID v4.
	 */
	public static final int DEFAULT_SIZE = 21;

	/**
	 * Static factory to retrieve a url-friendly, pseudo randomly generated, NanoId
	 * String.
	 * <p>
	 * The generated NanoId String will have 21 symbols.
	 * <p>
	 * The NanoId String is generated using a cryptographically strong pseudo random
	 * number generator.
	 * @return A randomly generated NanoId String.
	 */
	public static String randomNanoId() {
		return randomNanoId(DEFAULT_NUMBER_GENERATOR, DEFAULT_ALPHABET, DEFAULT_SIZE);
	}

	public static Integer randomInteger() {
		int parsedInt = Integer.parseInt(randomNanoId(DEFAULT_NUMBER_GENERATOR, DEFAULT_NUMBER, 9));
		while (parsedInt <= 0) {
			parsedInt = Integer.parseInt(randomNanoId(DEFAULT_NUMBER_GENERATOR, DEFAULT_NUMBER, 9));
		}
		return parsedInt;
	}

	public static Long randomLong() {
		long parseLong = Long.parseLong(randomNanoId(DEFAULT_NUMBER_GENERATOR, DEFAULT_NUMBER, 19));
		while (parseLong <= 0) {
			parseLong = Long.parseLong(randomNanoId(DEFAULT_NUMBER_GENERATOR, DEFAULT_NUMBER, 19));
		}
		return parseLong;
	}

	/**
	 * Static factory to retrieve a NanoId String.
	 * <p>
	 * The string is generated using the given random number generator.
	 * @param random The random number generator.
	 * @param alphabet The symbols used in the NanoId String.
	 * @param size The number of symbols in the NanoId String.
	 * @return A randomly generated NanoId String.
	 */
	public static String randomNanoId(final Random random, final char[] alphabet, final int size) {

		if (random == null) {
			throw new IllegalArgumentException("random cannot be null.");
		}

		if (alphabet == null) {
			throw new IllegalArgumentException("alphabet cannot be null.");
		}

		if (alphabet.length == 0 || alphabet.length >= 256) {
			throw new IllegalArgumentException("alphabet must contain between 1 and 255 symbols.");
		}

		if (size <= 0) {
			throw new IllegalArgumentException("size must be greater than zero.");
		}

		final int mask = (2 << (int) Math.floor(Math.log(alphabet.length - 1) / Math.log(2))) - 1;
		final int step = (int) Math.ceil(1.6 * mask * size / alphabet.length);

		final StringBuilder idBuilder = new StringBuilder();

		while (true) {

			final byte[] bytes = new byte[step];
			random.nextBytes(bytes);

			for (int i = 0; i < step; i++) {

				final int alphabetIndex = bytes[i] & mask;

				if (alphabetIndex < alphabet.length) {
					idBuilder.append(alphabet[alphabetIndex]);
					if (idBuilder.length() == size) {
						return idBuilder.toString();
					}
				}

			}

		}

	}

}
