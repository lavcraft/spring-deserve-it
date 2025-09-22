package spring.deserve.it.scope;

import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.event.EventListener;

public class FightScopeEventListener {
    private final ConfigurableListableBeanFactory beanFactory;
    private final FightScopeImplementation fightScope;

    public FightScopeEventListener(ConfigurableListableBeanFactory beanFactory) {
        this.beanFactory = beanFactory;
        this.fightScope  = (FightScopeImplementation) beanFactory.getRegisteredScope("fightScope");
    }

    @EventListener
    public void on(FightFinishedEvent event) {
        System.out.println("Fight scope event: " + event);
        var winners = event.getWinners();
        for (var i = 0; i < winners.size(); i++) {
            var winner = winners.get(i);
            beanFactory.registerSingleton("winner" + i, winner);
        }
        fightScope.reset();
    }
}