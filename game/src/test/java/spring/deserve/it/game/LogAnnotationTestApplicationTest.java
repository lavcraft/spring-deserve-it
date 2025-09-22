package spring.deserve.it.game;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;
import spring.deserve.starter.core.DeserverItCoreAutoConfiguration;
import spring.deserve.starter.core.DynamicLogProxyConfigurator;

import static org.assertj.core.api.Assertions.assertThat;

//TODO почему тест снова сломался? Подумайте, как изолировать поведение
// и достичь оптимального набора тестов для нашего DynamicLogProxyConfigurator
@RequiredArgsConstructor
public class LogAnnotationTestApplicationTest {

    @Test
    void should_works_with_log_annotation() {
        new ApplicationContextRunner()
                .withConfiguration(AutoConfigurations.of(DeserverItCoreAutoConfiguration.class))
                .run(context -> {
                    assertThat(context).hasSingleBean(DynamicLogProxyConfigurator.class);
                });
    }
}
