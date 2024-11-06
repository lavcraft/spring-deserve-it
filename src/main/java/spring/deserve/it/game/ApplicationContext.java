package spring.deserve.it.game;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.reflections.Reflections;
import spring.deserve.it.api.ObjectFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

//TODO подумайте, какие ответственности возложены именно на ApplicationContext?
// А какие бы вы делегировали другим объектам?
@RequiredArgsConstructor
public class ApplicationContext {
    private final ObjectFactory         objectFactory;
    private final Map<Class<?>, Object> singletonCache = new HashMap<>();
    private final String                packagesToScan;
    @Getter
    private final Reflections           scanner;  // Поле для сканера Reflections

    public ApplicationContext(String packagesToScan) {
        this.packagesToScan = packagesToScan;
        this.scanner        = new Reflections(packagesToScan);  // Инициализируем сканер на основе переданных пакетов
        this.objectFactory  = new ObjectFactory(this);  // Передаем ссылку на контекст фабрике
    }

    public <T> T getObject(Class<T> clazz) {

        return objectFactory.createObject(clazz);
    }
}
