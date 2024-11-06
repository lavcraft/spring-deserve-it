package spring.deserve.it.game;

import spring.deserve.it.api.Inject;
import spring.deserve.it.api.RPSEnum;

public class GameMaster {
    @Inject PaperSpider       spider1 = new PaperSpider();
    @Inject StatisticalSpider spider2 = new StatisticalSpider();
    @Inject HistoricalService historicalService;

    public void fight() {
        int battleId = 0;
        // Увеличиваем ID боя для каждого нового боя
        battleId++;
        System.out.println("Начинаем бой №" + battleId + " между " + spider1.getClass()
                                                                            .getSimpleName() + " и " + spider2.getClass()
                                                                                                              .getSimpleName() + "!");

        while (spider1.isAlive() && spider2.isAlive()) {
            RPSEnum move1 = spider1.fight(spider2, battleId);  // Передаём оппонента и battleId
            RPSEnum move2 = spider2.fight(spider1, battleId);  // Передаём оппонента и battleId

            // Сохраняем историю хода для каждого паука
            historicalService.saveHistory(
                    battleId, HistoricalServiceImpl.Move.builder()
                                                    .player1Id(spider1.hashCode())
                                                    .player1Move(move1)
                                                    .player2Id(spider2.hashCode())
                                                    .player2Move(move2)
                                                    .build()
            );
            System.out.println("Ходы");
            System.out.println("----");
            System.out.printf("%10s : %-15s\n", move1, spider1.getClass().getSimpleName());
            System.out.printf("%10s : %-15s\n", move2, spider2.getClass().getSimpleName());

            // Логика боя
            if (move1 == RPSEnum.ROCK && move2 == RPSEnum.SCISSORS) {
                spider2.loseLife();
            } else if (move1 == RPSEnum.SCISSORS && move2 == RPSEnum.PAPER) {
                spider2.loseLife();
            } else if (move1 == RPSEnum.PAPER && move2 == RPSEnum.ROCK) {
                spider2.loseLife();
            } else {
                spider1.loseLife();
            }

            System.out.println("Жизни игроков:");
            System.out.println("--------------");
            System.out.printf("%10s : %-20s\n", spider1.getLives(), spider1.getClass().getSimpleName());
            System.out.printf("%10s : %-20s\n", spider2.getLives(), spider2.getClass().getSimpleName());
        }

        // Определяем победителя
        String winner = spider1.isAlive()
                        ? spider1.getClass().getSimpleName()
                        : spider2.getClass().getSimpleName();
        System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
        System.out.println(" Бой №" + battleId + " окончен! Победитель: " + winner);
        System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
    }

}
