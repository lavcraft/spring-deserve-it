package spring.deserve.it;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import spring.deserve.it.game.GameMaster;
import spring.deserve.it.game.StoneSpider;

import java.util.Random;

@SpringBootApplication
public class Main {


    @Bean
    public StoneSpider stoneSpider() {
        return new StoneSpider();
    }


    public static void main(String[] args) {


        ConfigurableApplicationContext context = SpringApplication.run(Main.class);
        context.getBean(GameMaster.class).fight();

      /*  System.out.println("Starting game");
        var        applicationContext = new ApplicationContext("spring.deserve.it");
        GameMaster gameMaster         = applicationContext.getBean(GameMaster.class);
        gameMaster.fight();*/
    }

}
