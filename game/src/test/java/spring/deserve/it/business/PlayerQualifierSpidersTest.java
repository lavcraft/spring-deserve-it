package spring.deserve.it.business;

import lombok.Getter;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.cglib.proxy.Enhancer;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import spring.deserve.it.aop.AspectConfig;
import spring.deserve.it.api.*;
import spring.deserve.it.game.AbstractSpider;
import spring.deserve.it.game.PaperSpider;
import spring.deserve.it.game.StoneSpider;
import spring.deserve.starter.core.Log;

import java.lang.reflect.Proxy;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;


@SpringJUnitConfig({
        PlayerQualifierSpidersTest.Config.class,
        AspectConfig.class,
        LogAspect.class,
        PlayerQualifierPostProcessor.class
})
public class PlayerQualifierSpidersTest {
    @Autowired ApplicationContext applicationContext;

    /*********************************
     *** Test configuration **********
     ********************************/
    @TestConfiguration
    @EnableAspectJAutoProxy
    public static class Config {

        @Bean
        public PaperSpider paperSpider() {
            return new PaperSpider();
        }

        @Bean
        public StoneSpider stoneSpider() {
            return new StoneSpider();
        }

        @Bean
        @PlayerQualifier(playerName = "SuperDuperOwner")
        public Spider superDuperSpider() {
            return new AbstractSpider() {
                @Getter
                String test = "testfield";

                @Log("test")
                @Override
                public RPSEnum fight(Spider opponent, int battleId) {
                    return RPSEnum.ROCK;
                }
            };
        }
    }

    /*************************************
     *** End test configuration **********
     ************************************/

    @Test
    void should_contains_proxy_spiders_bean_in_context() {
        Map<String, Spider> spiders = applicationContext.getBeansOfType(Spider.class);

        assertThat(
                spiders.values()
                       .stream()
                       .anyMatch(
                               spider -> Enhancer.isEnhanced(spider.getClass())
                                       || Proxy.isProxyClass(spider.getClass())
                       )
        ).isTrue();
    }

    @Test
    void should_set_owner_to_all_beans() {
        Map<String, Spider> spiders = applicationContext.getBeansOfType(Spider.class);

        //expect
        assertThat(spiders.values())
                .allMatch(s -> s.getOwner() != null)
                .hasSize(3);
    }

    @Test
    void should_set_owner_from_qualifier() {
        Spider bean = applicationContext.getBean("superDuperSpider", Spider.class);

        assertThat(bean.getOwner()).isEqualTo("SuperDuperOwner");
    }
}
