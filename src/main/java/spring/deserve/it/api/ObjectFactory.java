package spring.deserve.it.api;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.reflections.ReflectionUtils;
import org.reflections.Reflections;

import java.lang.reflect.*;
import java.util.*;
import java.util.stream.Collectors;


import lombok.SneakyThrows;
import org.reflections.Reflections;
import org.springframework.util.ClassUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
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

    @SneakyThrows
    public <T> T createObject(Class<T> type) {
        // Создаем объект указанного типа
        T obj = type.getDeclaredConstructor().newInstance();

        // Конфигурируем объект
        configure(obj);

        // Вызываем метод @PostConstruct, если он есть
        invokePostConstruct(obj);


        if(ReflectionUtils.getAllMethods(obj.getClass(), method -> method.isAnnotationPresent(Log.class)).size()>0){
            return (T) Proxy.newProxyInstance(obj.getClass().getClassLoader(), ClassUtils.getAllInterfaces(obj), new InvocationHandler() {
                public Object invoke(Object proxy, Method method, Object[] args) throws InvocationTargetException, IllegalAccessException {
                    // Поиск оригинального метода в классе, чтобы получить аннотацию @Log
                    Method originalMethod = null;
                    try {
                        originalMethod = type.getMethod(method.getName(), method.getParameterTypes());
                    } catch (NoSuchMethodException e) {
                        throw new RuntimeException(e);
                    }

                    if (originalMethod.isAnnotationPresent(Log.class)) {
                        Log log = originalMethod.getAnnotation(Log.class);
                        Set<String> fieldsToLog = new HashSet<>(Arrays.asList(log.value()));

                        // Логируем значения полей до вызова метода
                        System.out.println("Before method " + originalMethod.getName() + " call:");
                        logFieldValues(fieldsToLog);

                        // Вызов метода
                        Object result = null;
                        try {
                            result = method.invoke(obj, args);
                        } catch (IllegalAccessException e) {
                            throw new RuntimeException(e);
                        } catch (InvocationTargetException e) {
                            throw new RuntimeException(e);
                        }

                        // Логируем значения полей после вызова метода
                        System.out.println("After method " + originalMethod.getName() + " call:");
                        logFieldValues(fieldsToLog);

                        return result;
                    }

                    // Вызов метода без логирования, если аннотации @Log нет
                    return method.invoke(obj, args);
                }

                @SneakyThrows
                private void logFieldValues(Set<String> fieldsToLog) {
                    for (String fieldName : fieldsToLog) {
                        try {
                            Field field = type.getDeclaredField(fieldName);
                            field.setAccessible(true);
                            System.out.println(fieldName + " = " + field.get(obj));
                        } catch (NoSuchFieldException e) {
                            System.out.println("Field " + fieldName + " not found in class " + type.getName());
                        }
                    }
                }
            });
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