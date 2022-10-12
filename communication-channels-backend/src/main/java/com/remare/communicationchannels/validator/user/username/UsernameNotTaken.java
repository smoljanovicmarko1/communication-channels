package com.remare.communicationchannels.validator.user.username;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = UsernameNotTakenValidator.class)
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface UsernameNotTaken {

  String message() default "this username is already taken";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};
}
