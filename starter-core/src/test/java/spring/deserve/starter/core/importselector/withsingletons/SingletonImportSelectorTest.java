package spring.deserve.starter.core.importselector.withsingletons;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.env.Environment;
import org.springframework.core.type.AnnotationMetadata;
import spring.deserve.starter.core.Singleton;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Answers.RETURNS_DEEP_STUBS;
import static org.mockito.Mockito.when;

//TODO как бы вы дебажили поведение ImportSelector или ImportBeanDefinitionRegistrar?
// Напишите @SpringBootTest / ApplicationContextRunner тест для дебага передаваемых данных в ImportSelector*
@ExtendWith(MockitoExtension.class)
class SingletonImportSelectorTest {
    @InjectMocks                       SingletonImportSelector singletonImportSelector;
    @Mock(answer = RETURNS_DEEP_STUBS) Environment             environment;
    @Mock(answer = RETURNS_DEEP_STUBS) AnnotationMetadata      annotationMetadata;

    @Singleton
    public static class S1 {

    }

    @Singleton
    public static class S2 {

    }

    public static class S3 {

    }


    //TODO попробуйте изолировать тест ля поиска конкретных классов с аннотацией Singleton из пакета теста
    // 1. Измените проверку на тестирование наличие всех классов, чтобы не было лишних
    // 2. Напишите тест с Spring Context для изучения и дебага того как работает ImportSelector etc (см в шапке)
    @Test
    void should_return_singletons_class_names() {
        when(environment.getProperty("singltonpackage")).thenReturn(
                "spring.deserve.starter.core.importselector.withsingletons");
        var strings = singletonImportSelector.selectImports(annotationMetadata);

        assertThat(strings)
                .as("Should return class names with annotaitons Singleton")
                .containsAll(List.of(
                        "spring.deserve.starter.core.importselector.withsingletons.SingletonImportSelectorTest.S1")
                )
                .doesNotContain(
                        "spring.deserve.starter.core.importselector.withsingletons.SingletonImportSelectorTest.S3");
    }
}