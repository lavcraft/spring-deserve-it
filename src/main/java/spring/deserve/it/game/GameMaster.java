package spring.deserve.it.game;

import spring.deserve.it.api.RPSEnum;

public class GameMaster {

    @Inject
    private ScissorsSpider spider1 ;
    @Inject
    private StatisticalSpider spider2 ;

    @Inject
    private HistoricalService historicalService;
    private int battleId;


    public void fight() {
        battleId++;
        System.out.println("Начинаем бой между " + spider1.getClass().getSimpleName() + " и " + spider2.getClass().getSimpleName() + "!");

        while (spider1.isAlive() && spider2.isAlive()) {
            RPSEnum move1 = spider1.fight(spider2,battleId);
            RPSEnum move2 = spider2.fight(spider1,battleId);

            System.out.println(spider1.getClass().getSimpleName() + " ходит: " + move1);
            System.out.println(spider2.getClass().getSimpleName() + " ходит: " + move2);


            // Логика боя остаётся прежней
            if (isWinningMove(move1, move2)) {
                spider2.loseLife();
            } else if (isWinningMove(move2, move1)) {
                spider1.loseLife();
            } else {
                System.out.println("Ничья в раунде! Оба паука сохраняют жизни.");
            }
            historicalService.saveHistory(battleId, HistoricalService.Move.builder()
                    .player1Id(spider1.hashCode())
                    .player1Move(move1)
                    .player2Id(spider2.hashCode())
                    .player2Move(move2)
                    .build());



            System.out.println("Жизни " + spider1.getClass().getSimpleName() + ": " + spider1.getLives());
            System.out.println("Жизни " + spider2.getClass().getSimpleName() + ": " + spider2.getLives());
        }

        // Определяем победителя
        String winner = spider1.isAlive() ? spider1.getClass().getSimpleName() : spider2.getClass().getSimpleName();
        System.out.println("Бой окончен! Победитель: " + winner);
    }

    private boolean isWinningMove(RPSEnum move1, RPSEnum move2) {
        return (move1 == RPSEnum.ROCK && move2 == RPSEnum.SCISSORS) ||
                (move1 == RPSEnum.SCISSORS && move2 == RPSEnum.PAPER) ||
                (move1 == RPSEnum.PAPER && move2 == RPSEnum.ROCK);
    }

}
