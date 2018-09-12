package es.illescasdaniel.commons.validators;

import es.illescasdaniel.commons.annotations.NumberConstraints;
import es.illescasdaniel.commons.types.OpenRange;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * Validates a number..
 *
 * @author Daniel Illescas Romero (dillescas)
 * <br><a href="https://github.com/illescasDaniel/NumberConstraints">GitHub link</a>
 * <br><a href="https://github.com/illescasDaniel/NumberConstraints/blob/master/LICENSE">MIT LICENSE</a>
 * @see NumberConstraints
 */
public final class NumberConstraintsValidator implements ConstraintValidator<NumberConstraints, java.lang.Number> {

	private int digits;
	private int minDigits;
	private int maxDigits;
	private double minValue;
	private double maxValue;
	private boolean nullable;
	private boolean unsigned;

	@Override
	public void initialize(NumberConstraints constraintAnnotation) {
		this.digits = constraintAnnotation.digits();
		this.minDigits = constraintAnnotation.minDigits();
		this.maxDigits = constraintAnnotation.maxDigits();
		this.minValue = constraintAnnotation.minValue();
		this.maxValue = constraintAnnotation.maxValue();
		this.nullable = constraintAnnotation.nullable();
		this.unsigned = constraintAnnotation.unsigned();
	}

	@Override
	public boolean isValid(java.lang.Number value, ConstraintValidatorContext context) {

		if (value == null) {
			return this.nullable;
		}

		if (value.longValue() < 0 && this.unsigned) {
			return false;
		}

		if (value.doubleValue() < this.minValue || value.doubleValue() > this.maxValue) {
			return false;
		}

		if (this.digits < -1 || this.minDigits < -1 || this.maxDigits < -1) {
			return false;
		}

		// needs to be a bit optimized
		if (value.getClass().equals(Double.class) || value.getClass().equals(Float.class)) {

			StringBuilder digitsString = new StringBuilder();

			if (this.digits != -1 && this.minDigits == -1 && this.maxDigits == -1) {
				OpenRange.to(this.digits).forEach(ignore -> digitsString.append("9"));
				val opResult = value.doubleValue() / Double.parseDouble(digitsString.toString());
				return (opResult <= 1d) && ((long) (opResult * 10d) > 0d);
			}

			if (this.minDigits != -1 && this.digits == -1 && this.maxDigits == -1) {
				OpenRange.to(this.minDigits - 1).forEach((i) -> digitsString.append("9"));
				return value.doubleValue() >= Double.parseDouble(digitsString.toString());
			}

			if (this.maxDigits != -1 && this.digits == -1 && this.minDigits == -1) {
				OpenRange.to(this.maxDigits).forEach((i) -> digitsString.append("9"));
				return value.doubleValue() <= Double.parseDouble(digitsString.toString());
			}

			return true;
		}

		int numberOfDigits = value.longValue() > 0 ? value.toString().length() : (value.toString().length() - 1);

		if (this.digits != -1 && this.minDigits == -1 && this.maxDigits == -1) {
			return numberOfDigits == digits;
		}

		if (this.minDigits != -1 && this.digits == -1 && this.maxDigits == -1) {
			return numberOfDigits >= this.minDigits;
		}

		if (this.maxDigits != -1 && this.digits == -1 && this.minDigits == -1) {
			return numberOfDigits <= this.maxDigits;
		}

		return true;
	}
}
