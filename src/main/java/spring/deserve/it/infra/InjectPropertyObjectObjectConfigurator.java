package spring.deserve.it.infra;


import lombok.SneakyThrows;
import spring.deserve.it.api.InjectProperty;
import spring.deserve.it.game.ObjectConfigurator;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Properties;
import java.util.Set;

import static org.reflections.ReflectionUtils.getAllFields;

public class InjectPropertyObjectObjectConfigurator implements ObjectConfigurator {
    private Properties properties;
    private DataLoader dataLoader;

    private void enhacneData() {
        dataLoader.load(this.properties);
    }

    public InjectPropertyObjectObjectConfigurator(DataLoader dataLoader) {
        this.dataLoader = dataLoader;
    }

    public InjectPropertyObjectObjectConfigurator() {
        this(new DataLoader("application.properties"));
        this.properties = new Properties();
        this.enhacneData();
    }


    public InjectPropertyObjectObjectConfigurator(Properties properties) {
        this();
        this.properties = properties;
        this.enhacneData();
    }

    @SneakyThrows
    @Override
    public void configure(Object abstractSpider) {
        for (Method method : abstractSpider.getClass().getMethods()) {
            Parameter[] parameters = method.getParameters();
//            Parameter parameter = parameters[0];
        }
        Set<Field> fields = getAllFields(abstractSpider.getClass());
        for (Field field : fields) {
            field.setAccessible(true);

            if (field.isAnnotationPresent(InjectProperty.class)) {
                String value = field.getAnnotation(InjectProperty.class).value();
                Object propValue = properties.get(value);
                if (propValue == null) return;

                int i = Integer.parseInt((String) propValue);
                field.setInt(abstractSpider, i);
            }

        }

    }
}
