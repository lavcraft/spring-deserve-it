package spring.deserve.it.game;

import org.junit.jupiter.api.Test;
import spring.deserve.it.game.objectfactorytestconfigurators.TestObjectConfigurator;

import static org.assertj.core.api.Assertions.assertThat;

class ObjectFactoryTest {
    @Test
    void should_create_configured_object() {
        //given
        ApplicationContext applicationContext = new ApplicationContext("spring.deserve.it.game.objectfactorytestconfigurators");
        ObjectFactory objectFactory = new ObjectFactory(applicationContext);

        //when
        objectFactory.createObject(PaperSpider.class);

        //then
        assertThat(TestObjectConfigurator.calls)
                .as("Should increment static calls of test configurator")
                .isEqualTo(1);
    }

    @Test
    void should_set_app_context_on_each_configurator() {
        //given
        ApplicationContext applicationContext = new ApplicationContext("spring.deserve.it.game.objectfactorytestconfigurators");
        ObjectFactory objectFactory = new ObjectFactory(applicationContext);

        //when
        objectFactory.createObject(PaperSpider.class);

        //then
        assertThat(TestObjectConfigurator.context)
                .as("Should set static context on test configurator")
                .isNotNull();
    }
    @Test
    void should_call_all_colnfigurators() {
//        new ObjectFactory(List.of(new TestObjectConfigurator()));
    }
}