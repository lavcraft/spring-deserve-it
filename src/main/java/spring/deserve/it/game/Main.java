package spring.deserve.it.game;

public class Main {

    public static void main(String[] args) {
        System.out.println("Starting game");
        var        applicationContext = new ApplicationContext("spring.deserve.it.game");
        GameMaster gameMaster         = applicationContext.getObject(GameMaster.class);
        gameMaster.fight();
    }

}
