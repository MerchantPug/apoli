package dev.experiment.hud;

import it.unimi.dsi.fastutil.booleans.BooleanUnaryOperator;

public enum DrawType implements BooleanUnaryOperator {
	/**
	 * Draws even if the power doesn't want to draw.
	 */
	FORCE(x -> true),
	/**
	 * Draws if the power wants to.
	 */
	SHOW(BooleanUnaryOperator.identity()),
	/**
	 * Doesn't draw.
	 */
	HIDE(x -> false);

	private final BooleanUnaryOperator operator;

	DrawType(BooleanUnaryOperator operator) {this.operator = operator;}

	public boolean apply(boolean other) {
		return this.operator.apply(other);
	}
}
