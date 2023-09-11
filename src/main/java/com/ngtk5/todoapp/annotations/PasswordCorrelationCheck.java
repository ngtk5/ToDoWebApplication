package com.ngtk5.todoapp.annotations;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import jakarta.validation.ReportAsSingleViolation;

import java.lang.annotation.*;

/*
 * パスワード相関チェックの独自アノテーションインターフェイス
 */
@Documented
@Target({ElementType.TYPE, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = PasswordCorrelationCheckImpl.class)
@ReportAsSingleViolation
public @interface PasswordCorrelationCheck {

    String message() default "{passwordCorrelationCheck.errorMessage}";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

    @Documented
    @Target({ElementType.TYPE, ElementType.ANNOTATION_TYPE})
    @Retention(RetentionPolicy.RUNTIME)
    public @interface List {
              PasswordCorrelationCheck[] value();
       }

}
