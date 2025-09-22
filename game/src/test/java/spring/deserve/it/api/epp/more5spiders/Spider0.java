package spring.deserve.it.api.epp.more5spiders;

import lombok.experimental.Delegate;
import spring.deserve.it.api.Spider;
import spring.deserve.it.game.PaperSpider;

public class Spider0 implements Spider {
    @Delegate PaperSpider paperSpider;
}