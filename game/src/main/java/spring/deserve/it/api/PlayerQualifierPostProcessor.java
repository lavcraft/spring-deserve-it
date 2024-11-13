package spring.deserve.it.api;

import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

@Component
public class PlayerQualifierPostProcessor implements BeanPostProcessor {

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) {
        // Проверяем, является ли бин экземпляром Spider
        if (bean instanceof Spider) {
            Class<?> spiderClass = bean.getClass();

            // Проверяем, есть ли аннотация @PlayerQualifier на классе
            if (spiderClass.isAnnotationPresent(PlayerQualifier.class)) {
                PlayerQualifier qualifier = spiderClass.getAnnotation(PlayerQualifier.class);

                // Извлекаем значение playerName и вызываем setOwner у паука
                String playerName = qualifier.playerName();
                ((Spider) bean).setOwner(playerName);
                System.out.println();
            }
        }
        return bean;
    }

}
