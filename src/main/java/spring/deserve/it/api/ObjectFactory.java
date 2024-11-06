package spring.deserve.it.api;

import lombok.RequiredArgsConstructor;
import org.reflections.Reflections;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class ObjectFactory {
    private final ApplicationContext context;
    private final List<ObjectConfigurator> configurators;

    public ObjectFactory(ApplicationContext context) {
        this.context = context;

        // Используем Reflections для сканирования пакетов
        Reflections scanner = context.getReflections();
        // Инициализация ObjectConfigurators
        Set<Class<? extends ObjectConfigurator>> configuratorClasses = scanner.getSubTypesOf(ObjectConfigurator.class);
        configurators = configuratorClasses.stream()
                                           .map(this::createConfigurator)
                                           .peek(this::injectContextIfNeeded)
                                           .collect(Collectors.toList());

    }

    private ObjectConfigurator createConfigurator(Class<? extends ObjectConfigurator> clazz) {
        try {
            return clazz.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            throw new RuntimeException("Не удалось создать конфигуратор: " + clazz.getName(), e);
        }
    }

    private void injectContextIfNeeded(Object configurator) {
        for (Field field : configurator.getClass().getDeclaredFields()) {
            if (field.getType().equals(ApplicationContext.class)) {
                field.setAccessible(true);
                try {
                    field.set(configurator, context);
                } catch (IllegalAccessException e) {
                    throw new RuntimeException("Не удалось инжектировать контекст в конфигуратор: " + configurator.getClass().getName(), e);
                }
            }
        }
    }

    public <T> T createObject(Class<T> clazz) {
        try {
            // 1. Создаем объект через Reflection
            T object = clazz.getDeclaredConstructor().newInstance();

            // 2. Настраиваем объект через ObjectConfigurators
            for (ObjectConfigurator configurator : configurators) {
                configurator.configure(object);
            }

            return object;
        } catch (Exception e) {
            throw new RuntimeException("Не удалось создать объект класса " + clazz.getName(), e);
        }
    }



}
