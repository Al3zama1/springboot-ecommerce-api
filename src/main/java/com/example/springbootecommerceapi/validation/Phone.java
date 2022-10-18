package com.example.springbootecommerceapi.validation;

import javax.validation.Payload;

public @interface Phone {

    String message() default "Phone number is invalid";
    Class<?>[] groups() default { };
    Class<? extends Payload>[] payload() default { };
}
