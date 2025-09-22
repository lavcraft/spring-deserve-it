package spring.deserve.it;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import spring.deserve.it.game.GameMaster;
import spring.deserve.it.game.PreDestroyBeanFactoryPostProcessor;

@SpringBootApplication
public class Main {

    @Bean
    public static PreDestroyBeanFactoryPostProcessor preDestroyBfpp() {
      return new PreDestroyBeanFactoryPostProcessor();
    }

    public static void main(String[] args) {
        var context = SpringApplication.run(Main.class);
        GameMaster master = context.getBean(GameMaster.class);
        master.fight();
    }

}
