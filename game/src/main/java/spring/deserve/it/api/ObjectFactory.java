package spring.deserve.it.api;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.reflections.Reflections;
import spring.deserve.starter.core.ProxyConfigurator;

import java.util.stream.Collectors;


import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Set;

@RequiredArgsConstructor
public class ObjectFactory {
    private final ApplicationContext       context;
    private final List<ObjectConfigurator> configurators;
    private final List<ProxyConfigurator>  proxyConfigurators;

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

        // Инициализация ProxyConfigurators
        // Инициализация ProxyConfigurators
        Set<Class<? extends ProxyConfigurator>> proxyConfiguratorClasses = scanner.getSubTypesOf(ProxyConfigurator.class);
        proxyConfigurators = proxyConfiguratorClasses.stream()
                                                     .map(this::createProxyConfigurator)
                                                     .peek(this::injectContextIfNeeded)
                                                     .collect(Collectors.toList());

    }


    private ProxyConfigurator createProxyConfigurator(Class<? extends ProxyConfigurator> clazz) {
        try {
            return clazz.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            throw new RuntimeException("Не удалось создать прокси-конфигуратор: " + clazz.getName(), e);
        }
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
                    throw new RuntimeException(
                            "Не удалось инжектировать контекст в конфигуратор: " + configurator.getClass()
                                                                                               .getName(),
                            e
                    );
                }
            }
        }
    }

    @SneakyThrows
    public <T> T createObject(Class<T> type) {
        // Создаем объект указанного типа
        T obj = type.getDeclaredConstructor().newInstance();

        // Конфигурируем объект
        configure(obj);

        // Вызываем метод @PostConstruct, если он есть
        invokePostConstruct(obj);


        // Завернуть в прокси все объекты

        // 4. Проксируем объект через ProxyConfigurators
        obj = wrapWithProxies(obj, type);


        return obj;
    }

    private <T> T wrapWithProxies(T obj, Class<T> type) {
        for (var proxyConfigurator : proxyConfigurators) {
            obj = (T) proxyConfigurator.wrapWithProxy(obj, type);
        }
        return obj;
    }

    private void configure(Object obj) {
        for (ObjectConfigurator configurator : configurators) {
            configurator.configure(obj);
        }
    }

    private void invokePostConstruct(Object obj) {
        for (Method method : obj.getClass().getDeclaredMethods()) {
            if (method.isAnnotationPresent(PostConstruct.class)) {
                method.setAccessible(true);
                try {
                    method.invoke(obj);
                } catch (Exception e) {
                    throw new RuntimeException("Не удалось вызвать @PostConstruct метод: " + method.getName(), e);
                }
            }
        }
    }
}