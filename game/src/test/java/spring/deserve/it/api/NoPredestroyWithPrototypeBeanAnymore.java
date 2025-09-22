package spring.deserve.it.api;

import jakarta.annotation.PreDestroy;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.diagnostics.FailureAnalyzer;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;
import spring.deserve.it.game.PreDestroyBeanFactoryPostProcessor;

import static org.assertj.core.api.Assertions.assertThat;

//TODO насколько хорошо тестируемым получился PreDestroyBFPP ?. Попробуйте запустить все тесты сразу
// 1. Придумайте как это исправить
// 2. Придумайте как надёжно это протестировать
// 3. не забудьте запустить все тесты разом и посмотреть на результат
public class NoPredestroyWithPrototypeBeanAnymore {
    @TestConfiguration
    static class Config {
//        @Autowired PrototypeBean prototypeBean;

                @Bean
        public static PrototypeWithPreDestroyChecker preDestroyBfpp() {
            return new PrototypeWithPreDestroyChecker();
        }

//        @Bean
//        public String string() {
//            System.out.println("prototypeBean = " + prototypeBean);
//
//            return new String();
//        }

        @Bean
        public PreDestroyBeanFactoryPostProcessor preDestroyBeanFactoryPostProcessor() {
            return new PreDestroyBeanFactoryPostProcessor();
        }

        @Bean
        public FailureAnalyzer failureAnalyzer() {
            return new MyFailureAnalyzer();
        }

        @Bean
        @Scope("prototype")
        public PrototypeBean prototypeBean() {
            return new PrototypeBean();
        }

    }

    public static class PrototypeBean {
        @PreDestroy
        public void init() {
        }
    }

    @Test
    void should_fail_context_when_find_predestroy() {
        new ApplicationContextRunner()
                .withUserConfiguration(Config.class)
                .run(context -> assertThat(context).hasFailed()
                                                   .getFailure()
                                                   .isInstanceOf(FailException.class));

    }
}
