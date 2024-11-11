package spring.deserve.it.aop;

import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty(name = "godMode",havingValue = "true")
public class AspectConfig {

    @Bean
    public SpiderLifeBoostPointcut spiderLifeBoostPointcut() {
        return new SpiderLifeBoostPointcut();
    }

    @Bean
    public SpiderLifeBoostAdvice spiderLifeBoostAdvice() {
        return new SpiderLifeBoostAdvice();
    }

    @Bean
    public DefaultPointcutAdvisor spiderLifeBoostAdvisor(
            SpiderLifeBoostPointcut pointcut,
            SpiderLifeBoostAdvice advice
    ) {
        return new DefaultPointcutAdvisor(pointcut, advice); // Объединяем Pointcut и Advice
    }
}
