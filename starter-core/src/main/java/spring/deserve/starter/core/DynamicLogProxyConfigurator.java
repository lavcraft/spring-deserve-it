package spring.deserve.starter.core;

import lombok.SneakyThrows;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.util.ClassUtils;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

//@Component
public class DynamicLogProxyConfigurator implements ProxyConfigurator, BeanPostProcessor {
    private final Map<String, Class> types = new HashMap<>();

    @Autowired
    private ConfigurableListableBeanFactory factory;

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        types.put(beanName, bean.getClass());
        return bean;
    }

    @Override
    public Object wrapWithProxy(Object target, Class<?> originalClass) {
        // Проверяем, есть ли аннотация @Log на методах оригинального класса
        boolean hasLogAnnotatedMethods = Arrays.stream(originalClass.getDeclaredMethods())
                                               .filter(method -> !method.isSynthetic())  // Исключаем синтетические методы
                                               .anyMatch(method -> method.isAnnotationPresent(Log.class));

        if (hasLogAnnotatedMethods) {
            // Создаем Dynamic Proxy
            return Proxy.newProxyInstance(
                    originalClass.getClassLoader(),
                    ClassUtils.getAllInterfaces(target),
                    new LogInvocationHandler(target, originalClass)
            );
        }
        return target;  // Если аннотации @Log нет, возвращаем оригинальный объект
    }

    @Override
    @SneakyThrows
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        //TODO
//        BeanDefinition beanDefinition = factory.getBeanDefinition(beanName);
//        String         beanClassName  = beanDefinition.getBeanClassName();
//        var            beanClass = types.get(beanName);
//
        return wrapWithProxy(bean,bean.getClass() );
    }

    private class LogInvocationHandler implements InvocationHandler {
        private final Object   target;
        private final Class<?> originalClass;

        public LogInvocationHandler(Object target, Class<?> originalClass) {
            this.target        = target;
            this.originalClass = originalClass;
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            // Получаем оригинальный метод из класса
            Method originalMethod = originalClass.getMethod(method.getName(), method.getParameterTypes());

            // Логика для аннотации @Log на оригинальном методе
            if (originalMethod.isAnnotationPresent(Log.class)) {
                Log logAnnotation = originalMethod.getAnnotation(Log.class);
                for (String fieldName : logAnnotation.value()) {
                    try {
                        // Используем геттер для получения значений полей
                        Method getter     = originalClass.getMethod("get" + capitalize(fieldName));
                        Object fieldValue = getter.invoke(target);
                        System.out.println("Логирование поля " + fieldName + ": " + fieldValue);
                    } catch (NoSuchMethodException e) {
                        System.out.println("Геттер для поля " + fieldName + " не найден.");
                    }
                }
            }

            // Выполнение метода оригинального объекта
            return method.invoke(target, args);
        }


        private String capitalize(String fieldName) {
            return Character.toUpperCase(fieldName.charAt(0)) + fieldName.substring(1);
        }
    }
}

