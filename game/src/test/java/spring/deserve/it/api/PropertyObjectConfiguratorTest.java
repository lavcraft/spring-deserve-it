package spring.deserve.it.api;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import spring.deserve.it.game.PaperSpider;

import static org.junit.jupiter.api.Assertions.*;
class PropertyObjectConfiguratorTest {

    private PropertyObjectConfigurator injector;

    @BeforeEach
    public void setup() {
        injector = new PropertyObjectConfigurator();
    }

    @Test
    void inject_property_should_work() {
        Spider spider = new PaperSpider();

        // Внедряем свойства
        injector.configure(spider);

        // Проверяем, что значение lives установилось из application.properties
        assertEquals(5, spider.getLives(), "Expected 5 lives to be injected from properties");
    }
}