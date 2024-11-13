package spring.deserve.it.game;


import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;
import spring.deserve.it.api.DefaultSpider;
import spring.deserve.it.api.MyPlayerQualifier;

import static org.assertj.core.api.Assertions.assertThat;

//TODO сделайте так, чтобы тест заработал
// Внимательно прочитайте какие есть варианты Condition
public class DefaultSpiderTest {
    @DefaultSpider("1")
//    @PlayerQualifier(playerName = "1")
    public static class Spider1 {

    }

//    @PlayerQualifier(playerName = "1")
    @MyPlayerQualifier(test = "1")
    public static class Spider2 {

    }

    @DefaultSpider("2")
//    @PlayerQualifier(playerName = "2")
    public static class Spider3 {

    }

    @Test
    void should_not_create_default_spider_when_others_spiders_has_been_presented() {
        new ApplicationContextRunner()
                .withBean(Spider1.class)
                .withBean(Spider2.class)
                .withBean(Spider3.class)
                .run(context -> {
                    assertThat(context)
                            .doesNotHaveBean(Spider1.class)
                            .hasSingleBean(Spider2.class);
                });
    }

    @Test
    void should_create_default_spider_when_others_spiders_has_been_presented() {
        new ApplicationContextRunner()
                .withBean(Spider1.class)
                .withBean(Spider3.class)
                .run(context -> {
                    assertThat(context)
                            .hasSingleBean(Spider1.class)
                            .hasSingleBean(Spider3.class);
                });
    }
}
