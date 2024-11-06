package spring.deserve.it.api;

import lombok.SneakyThrows;
import spring.deserve.it.game.ApplicationContext;

import java.lang.reflect.Field;

public class InjectObjectConfigurator implements ObjectConfigurator {
    private ApplicationContext applicationContext;


    @Override
    @SneakyThrows
    public void configure(Object obj) {
        Class<?> clazz = obj.getClass();
        for (Field field : clazz.getDeclaredFields()) {
            if (field.isAnnotationPresent(Inject.class)) {
                field.setAccessible(true);
                // Создаем объект нужного типа с помощью фабрики
                Object fieldObject = applicationContext.getObject(field.getType());
                // Устанавливаем объект в поле
                field.set(obj, fieldObject);
            }
        }
    }
}
