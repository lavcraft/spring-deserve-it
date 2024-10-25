package spring.deserve.it.infra;

import java.io.InputStream;
import java.util.Properties;

public class DataLoader {
    private final String propertyFileName;

    public DataLoader(String propertyFileName) {
        this.propertyFileName = propertyFileName;
    }

    public void load(Properties properties) {
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(propertyFileName)) {
            properties.load(inputStream);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
}
