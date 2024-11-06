package spring.deserve.it.api;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import spring.deserve.it.game.ApplicationContext;

import static org.mockito.Mockito.*;

//TODO Доделайте @Inject через Setter
// Нужно ли тестировать отдельно комбинацию объекта которых есть и сеттер с @Inject и field с @Inject?
@ExtendWith(MockitoExtension.class)
class InjectObjectConfiguratorTest {
    @InjectMocks InjectObjectConfigurator injectObjectConfigurator;
    @Mock        ApplicationContext       applicationContext;

    public static class InjectTestClass {
        @Inject
        InjectedTestClass injectedTestClass;
    }

    public static class InjectTestClassSetterVersion {
        InjectedTestClass injectedTestClassBySetter;

        @Inject
        public void setInjectedTestClass(InjectedTestClass injectedTestClass) {
            this.injectedTestClassBySetter = injectedTestClass;
        }
    }

    public static class InjectedTestClass {

    }

    @BeforeEach
    void setUp() {
        when(applicationContext.getObject(InjectedTestClass.class)).thenReturn(new InjectedTestClass());
    }

    @Test
    void should_inject_new_object_into_field_with_annotation() {
        //given
        var injectTestClass = new InjectTestClass();

        //when
        injectObjectConfigurator.configure(injectTestClass);

        //then
        verify(applicationContext, times(1)).getObject(InjectedTestClass.class);
        Assertions.assertThat(injectTestClass.injectedTestClass).isNotNull();
    }


    @Test
    @Disabled("Advanced Homework")
    void should_inject_new_object_into_field_by_setter_with_annotation() {
        //given
        var injectTestClass = new InjectTestClassSetterVersion();

        //when
        injectObjectConfigurator.configure(injectTestClass);

        //then
        verify(applicationContext, times(1)).getObject(InjectedTestClass.class);
        Assertions.assertThat(injectTestClass.injectedTestClassBySetter).isNotNull();
    }
}