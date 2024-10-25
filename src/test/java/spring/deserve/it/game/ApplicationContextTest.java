package spring.deserve.it.game;

import org.junit.jupiter.api.Test;
import spring.deserve.it.infra.Singleton;

import static org.junit.jupiter.api.Assertions.*;

@Singleton
class ApplicationContextTest {

    static class MockNotSingleton {
    }

    @Test
    void testThatSingletonIsReallySingleton() {
        //given
        ApplicationContext context = new ApplicationContext("spring.deserve.it");

        //when
        ApplicationContextTest bean1 = context.getBean(ApplicationContextTest.class);
        ApplicationContextTest bean2 = context.getBean(ApplicationContextTest.class);

        //then
        assertEquals(bean1, bean2);

    }

    @Test
    void should_no_singleton_by_default() {
        //given
        ApplicationContext context = new ApplicationContext("spring.deserve.it");

        //when
        MockNotSingleton prototype1 = context.getBean(MockNotSingleton.class);
        MockNotSingleton prototype2 = context.getBean(MockNotSingleton.class);

        //then
        assertNotEquals(prototype1, prototype2);
    }
}