package spring.deserve.it.infra.enhancer;

import lombok.SneakyThrows;
import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.MethodInterceptor;
import spring.deserve.it.game.Log;
import spring.deserve.it.infra.ProxyConfigurator;

import java.lang.reflect.Method;
import java.util.Arrays;

public class LogAnnotationProxyViaEnhancerConfigurator implements ProxyConfigurator {

    @Override
    @SneakyThrows
    public <T> T wrapWithProxy(T obj, Class<T> originalClass) {
        // Проверяем, есть ли методы с аннотацией @Log в реализации класса
        if (Arrays.stream(originalClass.getMethods()).anyMatch(this::hasLogAnnotation)) {

            MethodInterceptor handler = (target, method, args, proxy) -> {
                if (method.isAnnotationPresent(Log.class)) {
                    Log logAnnotation = method.getAnnotation(Log.class);
                    for (String fieldName : logAnnotation.value()) {
                        try {
                            // Используем геттер для получения значений полей
                            Method getter = originalClass.getMethod("get" + capitalize(fieldName));
                            Object fieldValue = getter.invoke(target);
                            System.out.println("Логирование поля " + fieldName + ": " + fieldValue);
                        } catch (NoSuchMethodException e) {
                            System.out.println("Геттер для поля " + fieldName + " не найден.");
                        }
                    }
                }

                return proxy.invoke(obj, args);
            };

            return (T) Enhancer.create(originalClass, handler);
        }
        return obj;
    }

    // Логирование состояния полей перед вызовом метода
    @SneakyThrows
    private void logFieldsBefore(Object obj, String[] fields) {
        System.out.println("Логируем перед вызовом метода:");
        for (String field : fields) {
            Method getter = obj.getClass().getMethod("get" + capitalize(field));
            Object value = getter.invoke(obj);
            System.out.println(field + " = " + value);
        }
    }

    // Логирование состояния полей после вызова метода
    @SneakyThrows
    private void logFieldsAfter(Object obj, String[] fields) {
        System.out.println("Логируем после вызова метода:");
        for (String field : fields) {
            Method getter = obj.getClass().getMethod("get" + capitalize(field));
            Object value = getter.invoke(obj);
            System.out.println(field + " = " + value);
        }
    }

    private boolean hasLogAnnotation(Method method) {
        return method.isAnnotationPresent(Log.class);
    }

    private String capitalize(String fieldName) {
        return fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
    }
}
