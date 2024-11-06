package spring.deserve.it.game;

import lombok.SneakyThrows;
import spring.deserve.it.api.Inject;
import spring.deserve.it.api.ObjectConfigurator;

import java.lang.reflect.Field;

public class InjectObjectConfiguratror implements ObjectConfigurator {
    private ApplicationContext context;

    @Override
    public void configure(Object object) {
        Class<?> clazz = object.getClass();
        while (clazz != null) {
            injectFields(object, clazz.getDeclaredFields());
            clazz = clazz.getSuperclass();  // Рекурсивно поднимаемся по иерархии классов
        }
    }

    @SneakyThrows
    private void injectFields(Object object, Field[] fields) {
        for (Field field : fields) {
            if (field.isAnnotationPresent(Inject.class)) {
                field.setAccessible(true);
                Object injectedObject = context.getObject(field.getType());
                field.set(object, injectedObject);
            }
        }
    }
}