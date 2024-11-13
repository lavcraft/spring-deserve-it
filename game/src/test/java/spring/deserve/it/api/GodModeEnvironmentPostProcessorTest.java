package spring.deserve.it.api;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Answers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.SpringApplication;
import org.springframework.core.env.ConfigurableEnvironment;

import static org.mockito.Mockito.when;

//TODO подумайте, можно ли протестировать EPP с помощью
// 1. @SpringJUnitConfig,
// 2. ApplicationContextRunner,
// 3. @SpringBootTest?
// Обратите внимание на замокированные объекты, - на что влияет DEEP_STUBS ?
// Стоит ли тестировать приложение более детально?
@ExtendWith(MockitoExtension.class)
class GodModeEnvironmentPostProcessorTest {
    @InjectMocks
    GodModeEnvironmentPostProcessor processor;
    @Mock(answer = Answers.RETURNS_DEEP_STUBS) SpringApplication       app;
    @Mock(answer = Answers.RETURNS_DEEP_STUBS) ConfigurableEnvironment env;

    @Test
    void should_get_package_from_main_class_and_start_scanning() {
        Class<?> aClass = this.getClass();
        when(app.getMainApplicationClass()).thenAnswer(invocation -> aClass);
        processor.postProcessEnvironment(env, app);
    }

    //TODO Допишите тест/создайте окружение в котором можно протестировать краевые
    // случаи GodModeEnvironmentPostProcessor - разное количество классов. Как бы вы это делали?
    @Test
    void should_add_property_when_more_than_5_spider_classes_in_classpath() {
    }


    //TODO Допишите тест/создайте окружение в котором можно протестировать краевые
    // случаи GodModeEnvironmentPostProcessor - разное количество классов. Как бы вы это делали?
    @Test
    void should_not_add_property_when_less_than_5_spider_classes_in_classpath() {
    }
}
