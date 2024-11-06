package spring.deserve.it.game;

import lombok.Getter;
import lombok.Setter;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import spring.deserve.it.api.InjectProperty;
import spring.deserve.it.api.PropertyObjectConfigurator;
import spring.deserve.it.api.RPSEnum;
import spring.deserve.it.api.Spider;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
public class InjectPropertyTest {
    @InjectMocks PropertyObjectConfigurator injectPropertyConfigurator;
    @Mock        PropertyLoader             propertyLoader;

    static class TestSpider extends AbstractSpider {
        @Getter
        @Setter
        @InjectProperty("spider.default.lives")
        int customField = 0;

        @Override
        public RPSEnum fight(Spider opponent, int battleId) {
            return null;
        }
    }


    @Test
    void should_inject_lives_in_filed() {
        //given
        var testSpider = new TestSpider();

        //when
        injectPropertyConfigurator.configure(testSpider);

        //then
        assertThat(testSpider.getLives())
                .as("Should set field to 5 from test value")
                .isEqualTo(5);

        assertThat(testSpider.getCustomField())
                .as("Should set custom field to 5 from test value")
                .isEqualTo(5);
    }
}
