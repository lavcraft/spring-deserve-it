package spring.deserve.it.api;

import jakarta.annotation.PreDestroy;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;
import spring.deserve.it.game.PreDestroyBFPP;

import static org.assertj.core.api.Assertions.assertThat;

//TODO насколько хорошо тестируемым получился PreDestroyBFPP ?. Попробуйте запустить все тесты сразу
// 1. Придумайте как это исправить
// 2. Придумайте как надёжно это протестировать
// 3. не забудьте запустить все тесты разом и посмотреть на результат
public class NoPredestroyWithPrototypeBeanAnymore {
    @TestConfiguration
    static class Config {
        @Bean
        public static PreDestroyBFPP preDestroyBfpp() {
            return new PreDestroyBFPP();
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
    void should_thrown() {
        new ApplicationContextRunner()
                .withUserConfiguration(Config.class)
                .run(context -> {
                    assertThat(context).hasSingleBean(PrototypeBean.class)
                                       .getBean(PrototypeBean.class);
//                    assertThat(context).hasFailed();
                });


    }
}
