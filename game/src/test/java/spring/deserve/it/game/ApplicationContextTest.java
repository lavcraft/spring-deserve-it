package spring.deserve.it.game;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.reflections.Reflections;
import spring.deserve.it.api.ApplicationContext;
import spring.deserve.it.api.ObjectFactory;
import spring.deserve.it.api.Singleton;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ApplicationContextTest {
    @InjectMocks ApplicationContext applicationContext;
    @Mock        ObjectFactory      objectFactory;
    @Spy         Reflections        reflections = new Reflections("spring.deserve.it.game");

    public interface SingletonTestClassWithInterfaceInterface {

    }

    @Singleton
    public static class SingletonTestClassWithInterface implements SingletonTestClassWithInterfaceInterface {

    }

    @Singleton
    public static class SingletonTestClass {

    }

    public static class NotASingletonTestClass {

    }


    @Test
    void should_get_the_same_instance_of_singleton_object_with_interface() {
        //given
        //TODO подумайте где и какой тест нужно ещё добавить чтобы закрепить приведённое ниже поведение, объясните его
        when(objectFactory.createObject(SingletonTestClassWithInterface.class)).thenAnswer(i -> new SingletonTestClassWithInterface());

        //when
        var object1 = applicationContext.getBean(SingletonTestClassWithInterfaceInterface.class);
        var object2 = applicationContext.getBean(SingletonTestClassWithInterfaceInterface.class);


        //then
        assertThat(object1)
                .as("Should return the same object when get singleton")
                .isSameAs(object2);
    }

    @Test
    void should_get_the_same_instance_of_singleton_object() {
        //given
        //TODO подумайте где и какой тест нужно ещё добавить чтобы закрепить приведённое ниже поведение, объясните его
        when(objectFactory.createObject(SingletonTestClass.class)).thenAnswer(i -> new SingletonTestClass());

        //when
        var object1 = applicationContext.getBean(SingletonTestClass.class);
        var object2 = applicationContext.getBean(SingletonTestClass.class);


        //then
        assertThat(object1)
                .as("Should return the same object when get singleton")
                .isSameAs(object2);
    }

    @Test
    void should_return_new_object_when_get_again() {
        //when
        var object1 = applicationContext.getBean(NotASingletonTestClass.class);
        var object2 = applicationContext.getBean(NotASingletonTestClass.class);


        //then
        verify(objectFactory, times(2))
                .createObject(NotASingletonTestClass.class);

        //TODO Подумайте, у какого подхода какие есть плюсы и минусы
        // 1. подход выше с verify
        // 2. подход ниже с assertThat
        // Какой выбрали вы?
//        assertThat(object1)
//                .as("Should return different object when get generic class")
//                .isNotSameAs(object2);
    }
}