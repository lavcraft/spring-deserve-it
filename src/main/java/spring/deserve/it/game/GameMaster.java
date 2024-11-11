package spring.deserve.it.game;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.stereotype.Component;
import spring.deserve.it.api.Inject;
import spring.deserve.it.api.RPSEnum;
import spring.deserve.it.api.Spider;

import java.util.*;


@Component
public class GameMaster {
    @Value("${spider.default.lives}")
    int maxLives;
    int battleId = 0;

    @Inject List<Spider>                    spiders;  // Лист со всеми пауками
    @Inject HistoricalService               historicalService;
    @Inject ConfigurableListableBeanFactory applicationContext;

    // Метод для запуска игры
    public void fight() {
        while (true) {
            battleId++;
            List<Spider> shuffledSpiders = new ArrayList<>(spiders);
            Collections.shuffle(shuffledSpiders);
            Spider spider1 = shuffledSpiders.get(0);
            Spider spider2 = findOpponent(spider1, shuffledSpiders);

            if (spider2 == null) {
                System.out.println("Игра окончена! Все пауки одного игрока.");
                announceWinner(spider1);
                return;
            }

            System.out.println("Начинаем бой №" + battleId + " между " + spider1.getOwner() + " и " + spider2.getOwner());

            // Логика боя
            runBattle(spider1, spider2);

            // Обновляем трофеи
            String winner = spider1.isAlive()
                            ? spider1.getOwner()
                            : spider2.getOwner();

            System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
            System.out.println("!!! Победитель боя №" + battleId + ": " + winner);
            System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!\n");
        }
    }

    private Spider findOpponent(Spider spider1, List<Spider> shuffledSpiders) {
        // Ищем первого паука с другим владельцем
        for (Spider spider : shuffledSpiders) {
            if (!spider.getOwner().equals(spider1.getOwner()) && spider.isAlive()) {
                return spider;
            }
        }
        return null;
    }

    private void runBattle(Spider spider1, Spider spider2) {
        while (spider1.isAlive() && spider2.isAlive()) {
            System.out.println("=".repeat(51));
            RPSEnum move1 = spider1.fight(spider2, battleId);
            RPSEnum move2 = spider2.fight(spider1, battleId);

            historicalService.saveHistory(
                    battleId, HistoricalServiceImpl.Move.builder()
                                                        .player1Id(spider1.hashCode())
                                                        .player1Move(move1)
                                                        .player2Id(spider2.hashCode())
                                                        .player2Move(move2)
                                                        .build()
            );

            if (isWinningMove(move1, move2)) {
                spider2.loseLife();
            } else if (isWinningMove(move2, move1)) {
                spider1.loseLife();
            } else {
                System.out.println("Ничья в раунде! Оба паука сохраняют жизни.");
            }

            System.out.printf(
                    "%2$5s/%3$s жизней у %1$-20s%n",
                    spider1.getClass().getSimpleName(),
                    spider1.getLives(),
                    maxLives
            );
            System.out.printf(
                    "%2$5s/%3$s жизней у %1$-20s%n",
                    spider2.getClass().getSimpleName(),
                    spider2.getLives(),
                    maxLives
            );
        }

        var fightScope = applicationContext.getRegisteredScope("fightScope");
        if (!spider1.isAlive()) {
            var beanNamesForType = applicationContext.getBeanNamesForType(spider1.getClass());
            if (beanNamesForType.length > 0) {
                fightScope.remove(beanNamesForType[0]);
            }
            spiders.remove(spider1);

        }
        if (!spider2.isAlive()) {
            var beanNamesForType = applicationContext.getBeanNamesForType(spider2.getClass());
            if (beanNamesForType.length > 0) {
                fightScope.remove(beanNamesForType[0]);
            }
            spiders.remove(spider2);
        }
    }

    private boolean isWinningMove(RPSEnum move1, RPSEnum move2) {
        return (move1 == RPSEnum.ROCK && move2 == RPSEnum.SCISSORS) ||
                (move1 == RPSEnum.SCISSORS && move2 == RPSEnum.PAPER) ||
                (move1 == RPSEnum.PAPER && move2 == RPSEnum.ROCK);
    }


    private void announceWinner(Spider spider) {
        String winner = spider.getOwner();
        System.out.println("Победитель игры: " + winner);
    }
}