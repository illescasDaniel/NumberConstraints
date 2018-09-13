package es.illescasdaniel.commons.validators;

import es.illescasdaniel.commons.annotations.NumberConstraints;
import es.illescasdaniel.commons.types.OpenRange;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * Validates a number.
 * <p>
 * More or less equivalent to using: @Digits, @Range, @NotNull, (unsigned) @Min(0).
 *
 * @author Daniel Illescas Romero (dillescas)
 * <br><a href="https://github.com/illescasDaniel/NumberConstraints">GitHub link</a>
 * <br><a href="https://github.com/illescasDaniel/NumberConstraints/blob/master/LICENSE">MIT LICENSE</a>
 * @see NumberConstraints
 */
public final class NumberConstraintsValidator implements ConstraintValidator<NumberConstraints, java.lang.Number> {

	private NumberConstraints numberConstraints;

	@Override
	public void initialize(final NumberConstraints constraintAnnotation) {
		this.numberConstraints = constraintAnnotation;
	}

	@Override
	public boolean isValid(final java.lang.Number value, final ConstraintValidatorContext context) {

		if (value == null) {
			return this.numberConstraints.nullable();
		}

		if (value.longValue() < 0 && this.numberConstraints.unsigned()) {
			return false;
		}

		if (value.doubleValue() < this.numberConstraints.minValue() || value.doubleValue() > this.numberConstraints.maxValue()) {
			return false;
		}

		if (this.numberConstraints.digits() < -1 || this.numberConstraints.minDigits() < -1 || this.numberConstraints.maxDigits() < -1) {
			return false;
		}

		if (value.getClass().equals(Double.class) || value.getClass().equals(Float.class)) {

			if (this.numberConstraints.digits() != -1 && this.numberConstraints.minDigits() == -1 && this.numberConstraints.maxDigits() == -1) {
				double opResult = value.doubleValue() / Math.pow(10, this.numberConstraints.digits() - 1);
				return (opResult <= 1d) && ((long) (opResult * 10d) > 0d);
			}

			if (this.numberConstraints.minDigits() != -1 && this.numberConstraints.digits() == -1 && this.numberConstraints.maxDigits() == -1) {
				return value.doubleValue() >= Math.pow(10, this.numberConstraints.minDigits() - 1 - 1);
			}

			if (this.numberConstraints.maxDigits() != -1 && this.numberConstraints.digits() == -1 && this.numberConstraints.minDigits() == -1) {
				return value.doubleValue() <= Math.pow(10, this.numberConstraints.maxDigits());
			}

			return true;
		}

		int numberOfDigits = value.longValue() > 0 ? value.toString().length() : (value.toString().length() - 1);

		if (this.numberConstraints.digits() != -1 && this.numberConstraints.minDigits() == -1 && this.numberConstraints.maxDigits() == -1) {
			return numberOfDigits == this.numberConstraints.digits();
		}

		if (this.numberConstraints.minDigits() != -1 && this.numberConstraints.digits() == -1 && this.numberConstraints.maxDigits() == -1) {
			return numberOfDigits >= this.numberConstraints.minDigits();
		}

		if (this.numberConstraints.maxDigits() != -1 && this.numberConstraints.digits() == -1 && this.numberConstraints.minDigits() == -1) {
			return numberOfDigits <= this.numberConstraints.maxDigits();
		}

		return true;
	}
}
