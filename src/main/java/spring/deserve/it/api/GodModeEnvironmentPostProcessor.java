package spring.deserve.it.api;

import org.reflections.Reflections;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.core.env.PropertySource;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class GodModeEnvironmentPostProcessor implements EnvironmentPostProcessor {

    @Override
    public void postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application) {
        // Получаем основной пакет из свойства "main.configuration.package"

        Package basePackage   = application.getMainApplicationClass().getPackage();
        // Используем Reflections для поиска всех реализаций интерфейса Spider в указанном пакете
        Reflections reflections = new Reflections(basePackage.getName());
        Set<Class<? extends Spider>> spiderClasses = reflections.getSubTypesOf(Spider.class);

        // Проверяем, есть ли 3 и более реализации Spider
        if (spiderClasses.size() >= 5) {
            // Создаем новую Map для добавления свойства GodMode=true
            Map<String, Object> godModeProperty = new HashMap<>();
            godModeProperty.put("godMode", true);

            // Добавляем это свойство в Environment
            PropertySource<Map<String, Object>> godModeSource = new MapPropertySource("godModeSource", godModeProperty);
            environment.getPropertySources().addLast(godModeSource);
        }
    }
}
