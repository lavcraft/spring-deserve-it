package spring.deserve.it.api.epp.less5spiders;

import lombok.experimental.Delegate;
import spring.deserve.it.api.Spider;
import spring.deserve.it.game.PaperSpider;

public class Spider00 implements Spider {
    @Delegate PaperSpider paperSpider;
}