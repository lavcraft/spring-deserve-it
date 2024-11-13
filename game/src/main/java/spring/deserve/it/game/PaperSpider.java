package spring.deserve.it.game;

import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;
import spring.deserve.it.api.PlayerQualifier;
import spring.deserve.it.api.RPSEnum;
import spring.deserve.it.api.Spider;

@PlayerQualifier(playerName = "Dima")
public class PaperSpider extends AbstractSpider {


    @PostConstruct
    public void init(){
        System.out.println(this.getLives());
    }


    @Override
    public RPSEnum fight(Spider spider, int battleId) {
        return RPSEnum.PAPER;
    }
}
