package spring.deserve.it.api;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.annotation.AliasFor;
import org.springframework.stereotype.Component;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@PlayerQualifier
public @interface MyPlayerQualifier {
    @AliasFor(annotation = PlayerQualifier.class, attribute = "playerName")
    String test() default "";
}
