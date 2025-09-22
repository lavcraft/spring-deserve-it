package spring.deserve.starter.core;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

@AutoConfiguration
@Import(SingletonImportRegistrar.class)
public class DeserverItCoreAutoConfiguration {

    @Bean
    public static DynamicLogProxyConfigurator dynamicLogProxyConfigurator() {
        return new DynamicLogProxyConfigurator();
    }

}

