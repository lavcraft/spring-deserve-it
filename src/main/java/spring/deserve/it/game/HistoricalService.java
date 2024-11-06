package spring.deserve.it.game;

import spring.deserve.it.api.Log;

import java.util.List;

public interface HistoricalService {
    // Сохранение истории боя и обновление статистики пауков
    @Log("battleHistory")
    void saveHistory(int battleId, HistoricalServiceImpl.Move move);

    // Получение статистики паука по его ID
    HistoricalServiceImpl.SpiderStatistics getSpiderStatistics(int spiderId);

    // Получение истории ходов по бою
    List<HistoricalServiceImpl.Move> getBattleHistory(int battleId);

    // Формирование таблички с историей боёв
    String getBattleHistory();
}
