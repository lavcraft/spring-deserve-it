package spring.deserve.it.game;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import spring.deserve.it.aop.AspectConfig;
import spring.deserve.it.api.Spider;

import static org.assertj.core.api.Assertions.assertThat;

//TODO протестируйте что при восстановлении жизней пишется соответствующее сообщение
// 1. Подумайте, куда лучше ставить аннотацию @EnableAspectJAutoProxy. Какие есть плюсы и минусы?
// 2. Какие есть плюсы и минусы использования @SpringJunitTest или @SpringBootTest вместо ApplicationContextRunner?
public class ImmortalSpidersTest {
    @EnableAspectJAutoProxy
    public static class TestConfig {

    }

    @Test
    void should_refill_lives_by_AOP_advice_and_Pointcut() {
        new ApplicationContextRunner()
                .withUserConfiguration(AspectConfig.class, TestConfig.class)
                .withBean(StoneSpider.class)
                .run(context -> {
                    //given
                    var bean        = context.getBean(Spider.class);
                    var paperSpider = new PaperSpider();

                    //when
                    bean.fight(paperSpider, 1);
                    bean.loseLife();
                    bean.fight(paperSpider, 1);
                    bean.loseLife();
                    bean.fight(paperSpider, 1);
                    bean.loseLife();
                    bean.fight(paperSpider, 1);
                    bean.loseLife();

                    //last life, refill spider resource
                    bean.fight(paperSpider, 1);

                    //then
                    assertThat(bean.getLives())
                            .as("Should refill lives to 10, when it fall to 1 by AOP")
                            .isEqualTo(10);
                });

    }
}
