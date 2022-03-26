package io.github.apace100.apoli.util;

public enum Comparison {

	NONE("", (a, b) -> false),
	EQUAL("==", (a, b) -> a == b),
	LESS_THAN("<", (a, b) -> a < b),
	GREATER_THAN(">", (a, b) -> a > b),
	LESS_THAN_OR_EQUAL("<=", (a, b) -> a <= b),
	GREATER_THAN_OR_EQUAL(">=", (a, b) -> a >= b),
	NOT_EQUAL("!=", (a, b) -> a != b);

	public static Comparison getFromString(String comparisonString) {
		return switch (comparisonString) {
			case "==" -> EQUAL;
			case "<" -> LESS_THAN;
			case ">" -> GREATER_THAN;
			case "<=" -> LESS_THAN_OR_EQUAL;
			case ">=" -> GREATER_THAN_OR_EQUAL;
			case "!=" -> NOT_EQUAL;
			default -> NONE;
		};
	}

	private final String comparisonString;
	private final DoubleBiPredicate comparison;

	Comparison(String comparisonString, DoubleBiPredicate comparison) {
		this.comparisonString = comparisonString;
		this.comparison = comparison;
	}

	public boolean compare(double a, double b) {
		return this.comparison.compare(a, b);
	}

	public String getComparisonString() {
		return this.comparisonString;
	}

	public int getOptimalStoppingIndex(int compareTo) {
		return switch (this) {
			case EQUAL, LESS_THAN_OR_EQUAL, GREATER_THAN -> compareTo + 1;
			case LESS_THAN, GREATER_THAN_OR_EQUAL -> compareTo;
			default -> -1;
		};
	}

	public Comparison inverse() {
		return switch (this) {
			case NONE -> NONE;
			case EQUAL -> NOT_EQUAL;
			case LESS_THAN -> GREATER_THAN_OR_EQUAL;
			case GREATER_THAN -> LESS_THAN_OR_EQUAL;
			case LESS_THAN_OR_EQUAL -> GREATER_THAN;
			case GREATER_THAN_OR_EQUAL -> LESS_THAN;
			case NOT_EQUAL -> EQUAL;
		};
	}

	@FunctionalInterface
	interface DoubleBiPredicate {
		boolean compare(double a, double b);
	}
}
