package spring.deserve.it.api;

import org.springframework.context.annotation.Conditional;
import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Conditional(DefaultSpiderCondition.class)
@PlayerQualifier
public @interface DefaultSpider {
    @AliasFor(annotation = PlayerQualifier.class, attribute = "playerName")
    String value() default "";
}
