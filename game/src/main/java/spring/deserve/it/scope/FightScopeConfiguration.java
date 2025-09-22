package spring.deserve.it.scope;

import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FightScopeConfiguration {

    @Bean
    public static FightScopeBFPP fightScopeBfpp() {
        return new FightScopeBFPP();
    }

    @Bean
    public FightScopeEventListener fighScopeEventListener(ConfigurableListableBeanFactory factory) {
        return new FightScopeEventListener(factory);
    }
}
