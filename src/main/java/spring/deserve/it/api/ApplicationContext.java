package spring.deserve.it.api;

import lombok.RequiredArgsConstructor;
import org.reflections.Reflections;

import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
public class ApplicationContext {
    private final Map<Class<?>, Object> singletonBeans = new HashMap<>();
    private final Reflections reflections;
    private final ObjectFactory objectFactory;

    public ApplicationContext(String packagesToScan) {
        // Инициализация Reflection API для указанных пакетов
        this.reflections = new Reflections(packagesToScan);

        // Создание ObjectFactory и передача контекста
        this.objectFactory = new ObjectFactory(this);
    }

    public <T> T getBean(Class<T> type) {
        // Проверяем, есть ли бин в синглтонах
        if (singletonBeans.containsKey(type)) {
            return type.cast(singletonBeans.get(type));
        }

        // Если бин отмечен как синглтон, создаем его и сохраняем
        T bean = objectFactory.createObject(type);
        if (type.isAnnotationPresent(Singleton.class)) {
            singletonBeans.put(type, bean);
        }
        return bean;
    }

    public Reflections getReflections() {
        return reflections;
    }
}