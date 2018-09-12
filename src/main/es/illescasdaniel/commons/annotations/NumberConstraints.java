package es.illescasdaniel.commons.annotations;

import es.illescasdaniel.commons.validators.NumberConstraintsValidator;
import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Annotation to make basic checks on a NumberConstraints type.
 *
 * @author Daniel Illescas Romero (dillescas)
 * <br><a href="https://github.com/illescasDaniel/NumberConstraints">GitHub link</a>
 * <br><a href="https://github.com/illescasDaniel/NumberConstraints/blob/master/LICENSE">MIT LICENSE</a>
 * @see NumberConstraintsValidator
 */
@Target({FIELD,
		METHOD,
		PARAMETER,
		LOCAL_VARIABLE})
@Retention(RUNTIME)
@Constraint(validatedBy = NumberConstraintsValidator.class)
@Documented
public @interface NumberConstraints {

	String message() default "Invalid number value.\n" +
			"\tDigits: ${digits != -1 ? digits : ''}, MinDigits: ${minDigits != -1 ? minDigits : ''}, MaxDigits: ${maxDigits != -1 ? maxDigits : ''}.\n" +
			"\tRange: [{minValue}, {maxValue}].\n\tNullable: {nullable}\n\tUnsigned: {unsigned}";

	int digits() default -1;

	int minDigits() default -1;

	int maxDigits() default -1;

	double minValue() default Double.MIN_VALUE;

	double maxValue() default Double.MAX_VALUE;

	boolean nullable() default false;

	boolean unsigned() default false;

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};
}
