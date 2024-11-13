package spring.deserve.it.game;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

import java.io.InputStream;
import java.util.Properties;

//TODO использовать PropertyLoader в PropertyObjectConfigurator
// для разделения ответственности по инжекту значение и загрузке из файлов
// обратите внимание, как при интеграции изменятся тесты
@RequiredArgsConstructor
public class PropertyLoader {
    private final String targetPropertiesFileName;

    @SneakyThrows
    public Properties loadProperties() {

        try (InputStream inputStream = this.getClass()
                                           .getClassLoader()
                                           .getResourceAsStream(targetPropertiesFileName)) {
            var properties = new Properties();
            properties.load(inputStream);
            return properties;
        }

    }
}
