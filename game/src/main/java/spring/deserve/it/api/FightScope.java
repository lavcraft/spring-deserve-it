package spring.deserve.it.api;

import org.springframework.context.annotation.Scope;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@Scope(value = "fightScope")
public @interface FightScope {
    String value() default "fightScope";
}
