package spring.deserve.it.game.web;

import org.springframework.web.bind.annotation.RestController;

import lombok.*;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import spring.deserve.it.api.Spider;
import spring.deserve.it.game.GameMaster;
import spring.deserve.it.game.dynamic.SpiderClassLoader;

import java.util.Base64;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class DynamicSpiderController {
    final SpiderClassLoader         classLoader;
    final GenericApplicationContext context;
    final GameMaster                gameMaster;

    @PostMapping(value = "/spiders", consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity<SpiderResponseBody> registerBean(@RequestBody SpiderRequestBody body) {
        byte[] decode = Base64.getDecoder().decode(body.byteCode);

        try {
            Class<?>               loadedClass = classLoader.registerClassByBytes(decode);
            String                 beanName    = "dynamic." + loadedClass.getCanonicalName();
            BeanDefinitionRegistry registry    = (BeanDefinitionRegistry) context.getBeanFactory();

            GenericBeanDefinition genericBeanDefinition = new GenericBeanDefinition();
            genericBeanDefinition.setBeanClass(loadedClass);
            genericBeanDefinition.setScope(BeanDefinition.SCOPE_SINGLETON);
            registry.registerBeanDefinition(beanName, genericBeanDefinition);

            //check bean is valid
            Object bean = context.getBean(beanName);
            gameMaster.getSpiders().add((Spider) bean);

            return ResponseEntity.ok(SpiderResponseBody.builder()
                                                       .beanName(beanName)
                                                       .build());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(SpiderResponseBody.builder()
                                                                      .build());
        }

    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SpiderRequestBody {
        String byteCode;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SpiderResponseBody {
        private String beanName;
    }
}
