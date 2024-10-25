package spring.deserve.it.game;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import spring.deserve.it.infra.DataLoader;

import java.util.Properties;

class DataLoaderTest {

    @Test
    void shouldLoadDataFromFile() {
        //given
        DataLoader dataLoader = new DataLoader("application-test.properties");
        Properties props = new Properties();

        //when
        dataLoader.load(props);

        //then
        Assertions.assertThat(props.getProperty("test"))
                .as("Properties should contain test property and read it from file application-test.properties")
                .isEqualTo("88");
    }
}