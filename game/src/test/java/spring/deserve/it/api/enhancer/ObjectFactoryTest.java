package spring.deserve.it.api.enhancer;

import org.junit.jupiter.api.Test;
import spring.deserve.starter.core.Log;

//TODO напишете тесты для тестирование подгрузку конфигураторов ObjectFactory
// Напишите тест без использования реального объекта Reflections. Воспользуйтесь DEEP_MOCK
// Не делайте внутренних классов, попытайтесь извлечь из контекст ObjectFactoryTest для лаконичности
class ObjectFactoryTest {
    String logMePlease = "Log because @log";

    @Log("logMePlease")
    void tryLog() {

    }

    @Test
    void should_get_object_without_interface() {

    }

    @Test
    void should_log_field_when_call_tryLog_with_log_annotation() {

    }
}