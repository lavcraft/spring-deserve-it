package spring.deserve.it.game;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Properties;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class PropertyLoaderTest {
    PropertyLoader propertyLoader = new PropertyLoader("application-supertest.properties");

    @Test
    void should_load_props_from_file() {
        //when
        var properties = propertyLoader.loadProperties();

        //then
        assertThat(properties)
                .as("Should contain test.prop because it contained in file application-supertest.properties")
                .containsKey("test.prop");
    }

    @Test
    void should_contain_string_value() {
        //when
        var properties = propertyLoader.loadProperties();

        //then
        assertThat(properties)
                .as("Should contain straing value 55")
                .containsValue("55");
    }


    @Test
    void should_not_contain_values_from_default_prop_file
            () {
        //when
        var properties = propertyLoader.loadProperties();

        //then
        assertThat(properties)
                .as("Should contain straing value 55")
                .doesNotContainKey("spider.default.lives");
    }
}