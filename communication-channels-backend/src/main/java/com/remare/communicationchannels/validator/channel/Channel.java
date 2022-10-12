package com.remare.communicationchannels.validator.channel;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static com.remare.communicationchannels.constant.ErrorMessageConstant.CHANNEL_NAME_TAKEN;

@Constraint(validatedBy = ChannelValidator.class)
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Channel {

  String message() default CHANNEL_NAME_TAKEN;

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};
}
