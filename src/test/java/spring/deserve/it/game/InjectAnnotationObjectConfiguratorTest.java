package spring.deserve.it.game;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class InjectAnnotationObjectConfiguratorTest {

    @Inject
    private StoneSpider spider;


    private PaperSpider paperSpider;


    @Inject
    public void setPaperSpider(PaperSpider paperSpider) {
        this.paperSpider = paperSpider;
    }

    @Test
    void injectForFieldsIsWorking() {
        //given
        ApplicationContext applicationContext = new ApplicationContext("spring.deserve.it");
        InjectAnnotationObjectConfigurator configurator = new InjectAnnotationObjectConfigurator();
        //Should set app context because split responsibility between application context and configurators
        configurator.setApplicationContext(applicationContext);

        //when
        configurator.configure(this);

        //then
        assertNotNull(spider);
    }

    @Test
    void testInjectIsWorkingForMethods() {
        //given
        ApplicationContext applicationContext = new ApplicationContext("spring.deserve.it");
        InjectAnnotationObjectConfigurator configurator = new InjectAnnotationObjectConfigurator();
        //Should set app context because split responsibility between application context and configurators
        configurator.setApplicationContext(applicationContext);

        //when
        configurator.configure(this);

        //then
        assertNotNull(paperSpider);
    }
}