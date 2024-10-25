package spring.deserve.it.game;

import org.junit.jupiter.api.Test;
import spring.deserve.it.api.InjectProperty;
import spring.deserve.it.api.RPSEnum;
import spring.deserve.it.api.Spider;
import spring.deserve.it.infra.InjectPropertyObjectObjectConfigurator;

import java.util.Properties;

import static org.assertj.core.api.Assertions.assertThat;

class InjectPropertyObjectObjectConfiguratorTest {
    @Test
    void should_set_lives() {
        AbstractSpider abstractSpider = new AbstractSpider() {
            @Override
            public RPSEnum fight(Spider opponent, int battleId) {
                return null;
            }
        };

        //when
        InjectPropertyObjectObjectConfigurator injectPropertyObjectConfigurator = new InjectPropertyObjectObjectConfigurator();
        injectPropertyObjectConfigurator.configure(abstractSpider);

        //then
        assert abstractSpider.getLives() == 5;
    }

    @Test
    void should_set_value_from_test2_key() {
        //given
        AbstractSpider abstractSpider = new AbstractSpider() {
            @InjectProperty("test2") public int test;

            @Override
            public RPSEnum fight(Spider opponent, int battleId) {
                return null;
            }

            @Override
            public int getLives() {
                return this.test;
            }
        };
        Properties test2 = new Properties();
        test2.put("test2", "88");

        //when
        InjectPropertyObjectObjectConfigurator injectPropertyObjectConfigurator = new InjectPropertyObjectObjectConfigurator(test2);
        injectPropertyObjectConfigurator.configure(abstractSpider);

        //then
        assertThat(abstractSpider.getLives())
                .as("Should be 88, because ")
                .isEqualTo(88);
    }

    @Test
    void should_load_lives_from_file() {
        //given
        AbstractSpider abstractSpider = new AbstractSpider() {
            @InjectProperty("test_unknown_val") public int test;

            @Override
            public RPSEnum fight(Spider opponent, int battleId) {
                return null;
            }

            public int getLives() {
                return this.test;
            }
        };

        //when
        InjectPropertyObjectObjectConfigurator injectPropertyObjectConfigurator = new InjectPropertyObjectObjectConfigurator();
        injectPropertyObjectConfigurator.configure(abstractSpider);

        //then
        assertThat(abstractSpider.getLives())
                .as("Should be 0, because it default value for int fields")
                .isEqualTo(0);
    }
}