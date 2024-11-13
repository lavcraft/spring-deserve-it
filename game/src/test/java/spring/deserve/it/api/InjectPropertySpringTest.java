package spring.deserve.it.api;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import static org.assertj.core.api.Assertions.assertThat;

//TODO разделите ответственности загрузки properties из файла если не сделали это после первого дня
// 1. Подумайте как бы преобразовались тесты при переходе на spring в этом случае
// 2. В чём недостаток этого теста, о чём в нём не говорится?
// 3. Могут ли быть сосканированы бины из @TestConfiguration в соседних классах?
@SpringJUnitConfig
@RequiredArgsConstructor
public class InjectPropertySpringTest {
    private final ApplicationContext applicationContext;

    @TestConfiguration
    public static class Config {
        @Bean
        public PropertyObjectConfigurator propertyObjectConfigurator() {
            return new PropertyObjectConfigurator();
        }
        @Bean
        public InjectSpringTestClass injectSpringTestClass() {
            return new InjectSpringTestClass();
        }
    }

    public static class InjectSpringTestClass {
        @InjectProperty("spider.default.lives") int lives;
    }

    @Test
    void should_inject_property_from_application_properties_for_beans_in_spring_context() {
        var bean = applicationContext.getBean(InjectSpringTestClass.class);
        assertThat(bean.lives).isEqualTo(5);
    }
}
