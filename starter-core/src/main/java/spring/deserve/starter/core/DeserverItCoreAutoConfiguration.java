package spring.deserve.starter.core;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Bean;

@AutoConfiguration
public class DeserverItCoreAutoConfiguration {

    @Bean
    public DynamicLogProxyConfigurator dynamicLogProxyConfigurator() {
        return new DynamicLogProxyConfigurator();
    }

}

