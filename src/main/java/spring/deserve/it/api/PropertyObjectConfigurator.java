package spring.deserve.it.api;

import jakarta.annotation.PostConstruct;
import lombok.SneakyThrows;
import org.reflections.ReflectionUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.Properties;

//TODO подумайте в чём плюс не ставить @Component над BPP
// как это может помочь в тестировании приложения
// обратите внимания на старые тесты к PropertyObjectConfigurator, до перехода на Spring
@Component
public class PropertyObjectConfigurator implements ObjectConfigurator , BeanPostProcessor {

    @Autowired
    private Environment environment;

    @PostConstruct
    public void printEnv(){
        System.out.println(environment.getProperty("OS"));
    }

    private final Properties properties = new Properties();

    public PropertyObjectConfigurator() {
        try (InputStream input = getClass().getClassLoader().getResourceAsStream("application.properties")) {
            if (input == null) {
                throw new IllegalArgumentException("Configuration file not found");
            }
            properties.load(input);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        configure(bean);
        return bean;
    }

    @Override
    @SneakyThrows
    public void configure(Object target) {
        Class<?> clazz = target.getClass();
        for (Field field : ReflectionUtils.getAllFields(target.getClass())) {
            if (field.isAnnotationPresent(InjectProperty.class)) {
                InjectProperty annotation = field.getAnnotation(InjectProperty.class);
                String propertyKey = annotation.value().isEmpty() ? field.getName() : annotation.value();
                String value = properties.getProperty(propertyKey);
                if (value != null) {
                    field.setAccessible(true);
                    if (field.getType() == int.class) {
                        field.setInt(target, Integer.parseInt(value));
                    } else {
                        field.set(target, value);
                    }
                }
            }
        }
    }
}
