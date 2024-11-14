package spring.deserve.starter.core.importselector.spring.withsingletons;

import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;
import spring.deserve.starter.core.DeserverItCoreAutoConfiguration;
import spring.deserve.starter.core.Singleton;

import static org.assertj.core.api.Assertions.assertThat;

//TODO подумайте в чём преимущество такого теста через ApplicationContextRunner?
// 1. Допишите тест проверяющий что не нашлись лишние классы
// 2. Допишите тест для фиксации поведения в случае отсутствия property для basepackage от которого нужно искать
//      - например проверить что в этом случае контекст не поднимается с ошибкой
class SingletonImportSelectorSpringTest {
    @Singleton
    public static class S1 {

    }

    @Singleton
    public static class S2 {

    }

    public static class S3 {

    }

    //TODO допишите тест основанный на ApplicationContextRunner
    @Test
    void should_return_singletons_class_names() {
//        new ApplicationContextRunner()
//                ...
    }
}