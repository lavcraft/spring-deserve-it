package spring.deserve.it.game;

import spring.deserve.it.api.ApplicationContext;

public class Main {

    public static void main(String[] args) {
        System.out.println("Starting game");
        var        applicationContext = new ApplicationContext("spring.deserve.it");
        GameMaster gameMaster         = applicationContext.getBean(GameMaster.class);
        gameMaster.fight();
    }

}
