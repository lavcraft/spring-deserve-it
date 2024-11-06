package spring.deserve.it.api;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.reflections.Reflections;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@RequiredArgsConstructor
public class ApplicationContext {
    private final Map<Class<?>, Object> singletonBeans = new HashMap<>();
    private final Reflections reflections;
    private final ObjectFactory objectFactory;

    public ApplicationContext(String... packagesToScan) {
        // Инициализация Reflection API для указанных пакетов
        this.reflections = new Reflections((Object[]) packagesToScan);

        // Создание ObjectFactory и передача контекста
        this.objectFactory = new ObjectFactory(this);
    }

    @SneakyThrows
    public <T> T getBean(Class<T> type) {
        // Если объект уже кеширован, возвращаем его
        if (singletonBeans.containsKey(type)) {
            return type.cast(singletonBeans.get(type));
        }

        // Если тип является интерфейсом, ищем реализацию
        if (type.isInterface()) {
            // Находим все реализации интерфейса с помощью Reflections
            Set<Class<? extends T>> implementations = reflections.getSubTypesOf(type);
            if (implementations.size() != 1) {
                throw new IllegalStateException("Ожидалась одна реализация для интерфейса " + type + ", но найдено: " + implementations.size());
            }
            // Берем единственную реализацию
            Class<? extends T> implementationClass = implementations.iterator().next();
            type = (Class<T>) implementationClass;  // Подменяем `type` на реализацию
        }

        // Создаем объект через ObjectFactory
        T bean = objectFactory.createObject(type);

        // Если у класса есть аннотация @Singleton, кешируем его по интерфейсу или классу
        if (type.isAnnotationPresent(Singleton.class)) {
            Class<?> key = type.isInterface() ? type.getInterfaces()[0] : type; // кешируем по интерфейсу, если он есть
            singletonBeans.put(key, bean);
        }
        return bean;
    }

    public Reflections getReflections() {
        return reflections;
    }
}