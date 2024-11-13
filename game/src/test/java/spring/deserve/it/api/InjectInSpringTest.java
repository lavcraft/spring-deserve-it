package spring.deserve.it.api;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

@SpringJUnitConfig
public class InjectInSpringTest {
    public static class InjectedSpringTestClass {
    }
    public static class InjectSpringTestClass {
        @Inject InjectedSpringTestClass field;
    }

    @TestConfiguration
    public static class Config {
        @Bean
        public InjectSpringTestClass injectSpringTestClass() {
            return new InjectSpringTestClass();
        }

        @Bean
        public InjectedSpringTestClass injectedSpringTestClass() {
            return new InjectedSpringTestClass();
        }
    }

    @Autowired ApplicationContext    applicationContext;
    @Inject    InjectSpringTestClass field;

    @Test
    void should_inject_works_in_spring_application() {
        var bean = applicationContext.getBean(InjectSpringTestClass.class);
        assertThat(bean.field).isNotNull();
    }

    @Test
    void should_contain_target_bean() {
        //expect
        assertThatCode(() -> applicationContext.getBean(InjectSpringTestClass.class))
                .doesNotThrowAnyException();
    }
}
