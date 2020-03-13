package br.com.laersondev.goldenraspberryawardsapi.util;

import static java.text.MessageFormat.format;

import java.util.Objects;

public class Precondition {

	private static final String VAR_MUST_NOT_BE_NEGATIVE = "''{0}'' must not be negative.";
	private static final String VAR_MUST_NOT_BE_EMPTY = "''{0}'' must not be empty.";
	private static final String VAR_MUST_NOT_BE_NULL = "''{0}'' must not be null.";

	public static String checkIfNotBlank(String variable, final String variableName) {
		if (Objects.requireNonNull(variable, format(VAR_MUST_NOT_BE_NULL, variableName)).isEmpty()) {
			throw new IllegalArgumentException(format(VAR_MUST_NOT_BE_EMPTY, variableName));
		}
		return variable;
	}

	public static <T> T checkIfNotNull(T variable, final String variableName) {
		return Objects.requireNonNull(variable, format(VAR_MUST_NOT_BE_NULL, variableName));
	}

	public static int checkIfPositive(int variable, final String variableName) {
		if(variable < 0 ) {
			throw new IllegalArgumentException(format(VAR_MUST_NOT_BE_NEGATIVE, variableName));
		}
		return variable;
	}

}
