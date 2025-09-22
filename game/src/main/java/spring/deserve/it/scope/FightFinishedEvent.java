package spring.deserve.it.scope;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;
import spring.deserve.it.api.Spider;

import java.util.List;

@Getter
public class FightFinishedEvent extends ApplicationEvent {
    private final List<Spider> winners;

    public FightFinishedEvent(List<Spider> winners) {
        super(winners);
        this.winners = winners;
    }
}
