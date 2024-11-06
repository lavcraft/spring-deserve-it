package spring.deserve.it.game;


import spring.deserve.it.api.Inject;
import spring.deserve.it.api.RPSEnum;
import spring.deserve.it.api.Spider;

public class StatisticalSpider extends AbstractSpider {

    @Inject
    private HistoricalService historicalServiceInterface;

    @Override
    public RPSEnum fight(Spider opponent, int battleId) {
        // Получаем общую статистику оппонента по всем боям
        HistoricalServiceImpl.SpiderStatistics opponentStats = historicalServiceInterface.getSpiderStatistics(opponent.hashCode());

        // Если статистика пуста, создаём объект статистики с нулями, избегая null
        opponentStats = opponentStats != null
                        ? opponentStats
                        : new HistoricalServiceImpl.SpiderStatistics();

        // Определяем на основе общей статистики, какой ход оппонент, вероятно, сделает
        int rockCount     = opponentStats.getRockCount();
        int paperCount    = opponentStats.getPaperCount();
        int scissorsCount = opponentStats.getScissorsCount();

        // Простейший алгоритм: выбираем ход, который побеждает наиболее частый ход оппонента
        if (rockCount > paperCount && rockCount > scissorsCount) {
            return RPSEnum.PAPER;  // Побеждает камень
        } else if (paperCount > rockCount && paperCount > scissorsCount) {
            return RPSEnum.SCISSORS;  // Побеждает бумага
        } else {
            return RPSEnum.ROCK;  // Побеждает ножницы
        }
    }
}

