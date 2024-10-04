package vn.dencooper.fracejob.utils.annotation.validator;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

@Constraint(validatedBy = StrongPasswordValidator.class)
@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface StrongPassword {
    String message() default "Ít nhất 8 kí tự (chữ thường, hoa, số, kí tự đặc biệt)";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
