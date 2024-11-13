package spring.deserve.it.api;

import lombok.Getter;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;
import org.springframework.boot.test.system.CapturedOutput;
import org.springframework.boot.test.system.OutputCaptureExtension;
import org.springframework.context.annotation.Bean;
import spring.deserve.starter.core.DynamicLogProxyConfigurator;
import spring.deserve.starter.core.Log;

import static org.assertj.core.api.Assertions.assertThat;

//TODO сделайте тест, который тестирует комбинацию вызова метода tryLog, в условиях:
// 1. работает и DynamicLogProxyConfigurator
// 2. работать и LogAspect
// одновременно
// Как бы стали писать такой тест? Отдельно, в рамках уже имеющихся в репозитории? Почему?
@ExtendWith(OutputCaptureExtension.class)
public class DynamicLogProxyConfiguratorSpringTest {
    public interface BeanWithLogClassInterface {
        void tryLog();
    }

    @Getter
    public static class BeanWithLogClass implements BeanWithLogClassInterface {
        String fieldToLog = "fieldToLogValue";

        @Log("fieldToLog")
        public void tryLog() {

        }
    }

    @TestConfiguration
    public static class Config {
        @Bean
        public static DynamicLogProxyConfigurator dynamicLogProxyConfigurator() {
            return new DynamicLogProxyConfigurator();
        }

        @Bean
        public BeanWithLogClass dynamicLogProxyConfiguratorTest() {
            return new BeanWithLogClass();
        }
    }


    @Test
    void should_log_with_BPP_based_on_dynamic_proxy_implementation(CapturedOutput output) {
        new ApplicationContextRunner()
                .withUserConfiguration(Config.class)
                .run(context -> {
                    //Доступны Spring Assertions
                    assertThat(context).hasSingleBean(BeanWithLogClassInterface.class);

                    //given
                    var bean = context.getBean(BeanWithLogClassInterface.class);

                    //when
                    bean.tryLog();

                    //then
                    assertThat(output.toString())
                            .as("Should print field name with value because @Log('myfield')")
                            .contains("Логирование поля fieldToLog: fieldToLogValue");
                });
    }
}
