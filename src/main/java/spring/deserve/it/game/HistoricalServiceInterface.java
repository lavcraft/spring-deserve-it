package spring.deserve.it.game;

import java.util.List;

public interface HistoricalServiceInterface {
    // Сохранение истории боя и обновление статистики пауков
    void saveHistory(int battleId, HistoricalServiceImpl.Move move);

    // Получение статистики паука по его ID
    HistoricalServiceImpl.SpiderStatistics getSpiderStatistics(int spiderId);

    // Получение истории ходов по бою
    List<HistoricalServiceImpl.Move> getBattleHistory(int battleId);

    // Формирование таблички с историей боёв
    String getBattleHistory();
}
