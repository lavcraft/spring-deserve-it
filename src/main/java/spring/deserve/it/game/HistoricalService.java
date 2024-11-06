package spring.deserve.it.game;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import spring.deserve.it.api.RPSEnum;
import spring.deserve.it.api.Singleton;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Singleton
public class HistoricalService {

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
        private final int player1Id;  // Уникальный ID паука 1
        private final RPSEnum player1Move;  // Ход, который сделал паук 1
        private final int player2Id;  // Уникальный ID паука 2
        private final RPSEnum player2Move;  // Ход, который сделал паук 2
    }

    // Мапа для хранения общей статистики по каждому пауку
    private final Map<Integer, SpiderStatistics> lifetimeStatistics = new HashMap<>();

    // Мапа для хранения статистики по каждому бою
    private final Map<Integer, List<Move>> battleHistory = new HashMap<>();

    // Сохранение истории боя и обновление статистики пауков
    public void saveHistory(int battleId, Move move) {
        // Сохраняем ходы для данного боя
        battleHistory.computeIfAbsent(battleId, id -> new ArrayList<>()).add(move);

        // Обновляем статистику по первому пауку
        lifetimeStatistics.computeIfAbsent(move.getPlayer1Id(), id -> new SpiderStatistics()).addMove(move.getPlayer1Move());

        // Обновляем статистику по второму пауку
        lifetimeStatistics.computeIfAbsent(move.getPlayer2Id(), id -> new SpiderStatistics()).addMove(move.getPlayer2Move());
    }

    // Получение статистики паука по его ID
    public SpiderStatistics getSpiderStatistics(int spiderId) {
        return lifetimeStatistics.get(spiderId);
    }

    // Получение истории ходов по бою
    public List<Move> getBattleHistory(int battleId) {
        return battleHistory.get(battleId);
    }

    // Формирование таблички с историей боёв
    public String getBattleHistory() {
        StringBuilder battlefieldLog = new StringBuilder();

        if (battleHistory.isEmpty()) {
            return "История боев пуста.";
        }

        List<Move> moves = battleHistory.values().stream().findFirst().orElse(new ArrayList<>());

        // Форматируем заголовок с хэш-кодами игроков
        battlefieldLog.append("\n| Ход № | И1 (").append(moves.get(0).getPlayer1Id()).append(") | И2 (").append(moves.get(0).getPlayer2Id()).append(") |\n");
        battlefieldLog.append("|-------|-----------------|-----------------|\n");

        // Итерируем по списку ходов
        for (int i = 0; i < moves.size(); i++) {
            Move move = moves.get(i);
            battlefieldLog.append("|   ").append(i + 1).append("   |");

            // Ход игрока 1
            battlefieldLog.append("     ").append(move.getPlayer1Move() != null ? move.getPlayer1Move() : "").append("     |");

            // Ход игрока 2
            battlefieldLog.append("     ").append(move.getPlayer2Move() != null ? move.getPlayer2Move() : "").append("     |\n");
        }

        return battlefieldLog.toString();
    }
}
