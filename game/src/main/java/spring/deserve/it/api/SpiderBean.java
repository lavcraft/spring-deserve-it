package spring.deserve.it.api;

import org.springframework.context.annotation.Bean;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@FightScope
@Bean
public @interface SpiderBean {
}
