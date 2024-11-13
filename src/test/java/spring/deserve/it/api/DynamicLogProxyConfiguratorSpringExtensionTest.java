package spring.deserve.it.api;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import static org.assertj.core.api.Assertions.*;

@SpringJUnitConfig
@RequiredArgsConstructor
public class DynamicLogProxyConfiguratorSpringExtensionTest {
    private final ApplicationContext context;

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


    //TODO подумайте почему этот тест падает
    // 1. Как это можно починить?
    // 2. Откуда берётся .ORIGINAL класс? Кто его добавляет?
    // 3. Сравнить с тестом в котором применяется ApplicationContextRunner
    @Test
    void should_log_with_BPP_based_on_dynamic_proxy_implementation() {
        assertThatThrownBy(() -> context.getBean(BeanWithLogClassInterface.class))
                .hasMessageContaining(".ORIGINAL' available");
    }
}
