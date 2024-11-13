package spring.deserve.it.game;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;
import org.springframework.context.ApplicationContext;
import spring.deserve.starter.core.DynamicLogProxyConfigurator;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

//@SpringBootTest
@RequiredArgsConstructor
public class LogAnnotationTestApplicationTest {
//    @Autowired private final ApplicationContext context;

    @Test
    void should_works_with_log_annotation() {
//        assertThatCode(() -> {
//            context.getBean(DynamicLogProxyConfigurator.class);
//        });
        new ApplicationContextRunner()
                .withConfiguration(AutoConfigurations.of(AutoConfigurations.class))
//                .withConfiguration(AutoConfigurations.of(DeserverItCoreAutoConfiguration.class))
                .run(context -> {
                    assertThat(context).hasSingleBean(DynamicLogProxyConfigurator.class);
                });
    }
}
