package spring.deserve.it.api;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.system.CapturedOutput;
import org.springframework.boot.test.system.OutputCaptureExtension;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.PropertySource;
import spring.deserve.it.api.epp.less5spiders.Spider00;
import spring.deserve.it.api.epp.more5spiders.Spider0;
import spring.deserve.it.api.epp.nospiders.RootClassExample;

import java.util.Map;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.*;

//TODO подумайте, можно ли протестировать EPP с помощью
// 1. @SpringJUnitConfig,
// 2. ApplicationContextRunner,
// 3. @SpringBootTest?
// Обратите внимание на замокированные объекты, - на что влияет DEEP_STUBS ?
// Стоит ли тестировать приложение более детально?
@ExtendWith({
        MockitoExtension.class,
        OutputCaptureExtension.class
})
class GodModeEnvironmentPostProcessorTest {
    @InjectMocks
    GodModeEnvironmentPostProcessor processor;
    @Mock(answer = Answers.RETURNS_DEEP_STUBS) SpringApplication       app;
    @Mock(answer = Answers.RETURNS_DEEP_STUBS) ConfigurableEnvironment env;
    @Captor
    ArgumentCaptor<PropertySource<Map<String, Object>>> captor;

    //TODO Допишите тест/создайте окружение в котором можно протестировать краевые
    // случаи GodModeEnvironmentPostProcessor - разное количество классов. Как бы вы это делали?
    @Test
    void should_add_property_when_more_than_5_spider_classes_in_classpath() {
        //given
        when(app.getMainApplicationClass()).thenAnswer(invocation -> Spider0.class);

        //when
        processor.postProcessEnvironment(env, app);

        //then
        verify(env.getPropertySources()).addLast(captor.capture());
        assertThat(captor.getValue().getProperty("godMode"))
                .isEqualTo(true);
    }


    //TODO Допишите тест/создайте окружение в котором можно протестировать краевые
    // случаи GodModeEnvironmentPostProcessor - разное количество классов. Как бы вы это делали?
    @Test
    void should_not_add_property_when_less_than_5_spider_classes_in_classpath() {
        //given
        when(app.getMainApplicationClass()).thenAnswer(invocation -> Spider00.class);

        //when
        processor.postProcessEnvironment(env, app);

        //then
        verify(env, times(0)).getPropertySources();
    }

    @Test
    void should_not_fail_when_no_spiders_in_classpath(CapturedOutput capturedOutput) {
        //given
        when(app.getMainApplicationClass()).thenAnswer(invocation -> RootClassExample.class);

        //when
        assertAll(
                () ->
                        assertThatCode(() -> processor.postProcessEnvironment(env, app))
                                .as("Should not thrown when finding existing Spider in classpath")
                                .doesNotThrowAnyException()
                ,
                () ->
                        assertThat(capturedOutput.getAll())
                                .as("Should notify root search package name")
                                .contains("spring.deserve.it.api.epp.nospiders")
        );
    }
}
