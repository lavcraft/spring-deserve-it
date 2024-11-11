package spring.deserve.it.api;

import jakarta.annotation.PreDestroy;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.stereotype.Component;


@Component
public class PrototypeWithPreDestroyChecker implements BeanFactoryPostProcessor {

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        // Получаем все имена бинов, определенных в контексте
        String[] beanNames = beanFactory.getBeanDefinitionNames();

        for (String beanName : beanNames) {
            BeanDefinition beanDefinition = beanFactory.getBeanDefinition(beanName);

            // Проверяем, является ли бин прототипом
            if (beanDefinition.isPrototype()) {
                String beanClassName = beanDefinition.getBeanClassName();

                // Пропускаем бины без имени класса
                if (beanClassName == null) {
                    continue;
                }

                try {
                    // Проверяем, есть ли в классе метода с аннотацией @PreDestroy
                    Class<?> beanClass = Class.forName(beanClassName);
                    if (hasPreDestroyMethod(beanClass)) {
                        throw new IllegalStateException("Bean '" + beanName + "' с scope 'prototype' содержит метод с @PreDestroy, что недопустимо.");
                    }
                } catch (ClassNotFoundException e) {
                    throw new RuntimeException("Не удалось загрузить класс для бина: " + beanName, e);
                }
            }
        }
    }

    // Метод для проверки наличия @PreDestroy метода в классе
    private boolean hasPreDestroyMethod(Class<?> beanClass) {
        return java.util.Arrays.stream(beanClass.getMethods())
                               .anyMatch(method -> method.isAnnotationPresent(PreDestroy.class));
    }
}
