package com.remare.communicationchannels.validator.user.email;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static com.remare.communicationchannels.constant.ErrorMessageConstant.EMAIL_TAKEN;

@Constraint(validatedBy = EmailNotTakenValidator.class)
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface EmailNotTaken {

  String message() default EMAIL_TAKEN;

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};
}
