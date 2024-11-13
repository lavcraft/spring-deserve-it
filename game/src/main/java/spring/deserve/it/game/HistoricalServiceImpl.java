package spring.deserve.it.game;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import org.springframework.stereotype.Component;
import spring.deserve.starter.core.Log;
import spring.deserve.it.api.RPSEnum;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class HistoricalServiceImpl implements HistoricalService {

    public void init() {
        System.out.println("************* " + this.hashCode() + "  **************");
    }

    // Объект для хранения статистики ходов по каждому пауку
    public static class SpiderStatistics {
        private int rockCount;
        private int paperCount;
        private int scissorsCount;

        public void addMove(RPSEnum move) {
            switch (move) {
                case ROCK -> rockCount++;
                case PAPER -> paperCount++;
                case SCISSORS -> scissorsCount++;
            }
        }

        public int getRockCount() {
            return rockCount;
        }

        public int getPaperCount() {
            return paperCount;
        }

        public int getScissorsCount() {
            return scissorsCount;
        }
    }

    // Объект для хранения хода паука в рамках боя
    @Getter
    @Builder
    @ToString
    public static class Move {
        private final int     player1Id;  // Уникальный ID паука 1
        private final RPSEnum player1Move;  // Ход, который сделал паук 1
        private final int     player2Id;  // Уникальный ID паука 2
        private final RPSEnum player2Move;  // Ход, который сделал паук 2
    }

    // Мапа для хранения общей статистики по каждому пауку
    private final Map<Integer, SpiderStatistics> lifetimeStatistics = new HashMap<>();

    // Мапа для хранения статистики по каждому бою
    private final Map<Integer, List<Move>> battleHistory = new HashMap<>();

    // Сохранение истории боя и обновление статистики пауков
    @Log("battleHistory")
    @Override
    public void saveHistory(int battleId, Move move) {
        // Сохраняем ходы для данного боя
        battleHistory.computeIfAbsent(battleId, id -> new ArrayList<>()).add(move);

        // Обновляем статистику по первому пауку
        lifetimeStatistics.computeIfAbsent(move.getPlayer1Id(), id -> new SpiderStatistics())
                          .addMove(move.getPlayer1Move());

        // Обновляем статистику по второму пауку
        lifetimeStatistics.computeIfAbsent(move.getPlayer2Id(), id -> new SpiderStatistics())
                          .addMove(move.getPlayer2Move());
    }

    // Получение статистики паука по его ID
    @Override
    public SpiderStatistics getSpiderStatistics(int spiderId) {
        return lifetimeStatistics.get(spiderId);
    }

    // Получение истории ходов по бою
    @Override
    public List<Move> getBattleHistory(int battleId) {
        return battleHistory.get(battleId);
    }

    // Формирование таблички с историей боёв
    @Override
    public String getBattleHistory() {
        StringBuilder battlefieldLog = new StringBuilder();
        Formatter     fm             = new Formatter(battlefieldLog);

        if (battleHistory.isEmpty()) {
            return "История боев пуста.";
        }

        int charInCol = 20;
        var collect = battleHistory.values()
                                   .stream().map(moves -> {
                    // Форматируем заголовок с хэш-кодами игроков
                    fm.format("%n|-------|%s|%s|%n", "-".repeat(charInCol), "-".repeat(charInCol));
                    fm.format(
                            "| Ход № | И1 %-" + (charInCol - 5) + "s | И2 %-" + (charInCol - 5) + "s |%n",
                            moves.get(0).getPlayer1Id(),
                            moves.get(0).getPlayer2Id()
                    );
                    fm.format("|-------|%s|%s|%n", "-".repeat(charInCol), "-".repeat(charInCol));

                    // Итерируем по списку ходов
                    for (int i = 0; i < moves.size(); i++) {
                        Move move = moves.get(i);
                        fm.format("| %5s | %18s | %18s |%n", i + 1, move.getPlayer1Move(), move.getPlayer2Move());
                    }
                    fm.format("|-------|%s|%s|%n", "-".repeat(charInCol), "-".repeat(charInCol));

                    return fm.toString();
                })
                                   .collect(Collectors.joining("\n"));

        return collect;
    }
}
