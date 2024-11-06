package spring.deserve.it.game;

import jakarta.annotation.PostConstruct;
import spring.deserve.it.api.RPSEnum;
import spring.deserve.it.api.Spider;

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
