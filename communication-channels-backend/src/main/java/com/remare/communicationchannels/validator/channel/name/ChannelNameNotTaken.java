package com.remare.communicationchannels.validator.channel.name;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static com.remare.communicationchannels.constant.ErrorMessageConstant.CHANNEL_NAME_TAKEN;

@Constraint(validatedBy = ChannelNameNotTakenValidator.class)
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ChannelNameNotTaken {

  String message() default CHANNEL_NAME_TAKEN;

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};
}
