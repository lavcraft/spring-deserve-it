package spring.deserve.it.game;

import org.springframework.stereotype.Component;
import spring.deserve.it.api.PlayerQualifier;
import spring.deserve.it.api.RPSEnum;
import spring.deserve.it.api.Spider;

@PlayerQualifier(playerName = "Dima")
public class StoneSpider extends AbstractSpider {
    @Override
    public RPSEnum fight(Spider spider, int battleId) {
        return RPSEnum.ROCK;
    }
}
