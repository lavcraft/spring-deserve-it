package spring.deserve.it.api;

import lombok.SneakyThrows;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.context.annotation.ConfigurationCondition;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.core.type.AnnotatedTypeMetadata;
import org.springframework.core.type.StandardAnnotationMetadata;

import java.util.Arrays;

public class DefaultSpiderCondition implements ConfigurationCondition {
    @Override
    @SneakyThrows
    public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
        var targetPlayerName = metadata.getAnnotations().get(PlayerQualifier.class).getString("playerName");
        if (!(metadata instanceof StandardAnnotationMetadata)) {
            return true;
        }
        var originalClass = ((StandardAnnotationMetadata) metadata).getClassName();
        var beanFactory   = context.getBeanFactory();

        return !Arrays.stream(beanFactory.getBeanDefinitionNames())
                      .anyMatch(beanName -> {
                          try {
                              var beanDefinition = beanFactory.getBeanDefinition(beanName);
                              var beanClassName  = beanDefinition.getBeanClassName();
                              if (beanClassName.equals(originalClass)) {
                                  return false;
                              }
                              Class<?> aClass = Class.forName(beanClassName);

                              if (AnnotatedElementUtils.isAnnotated(aClass, PlayerQualifier.class)) {
                                  var mergedAnnotation = AnnotatedElementUtils.findMergedAnnotation(
                                          aClass,
                                          PlayerQualifier.class
                                  );
                                  var player = mergedAnnotation.playerName();
                                  return player.equals(targetPlayerName);
                              }

                          } catch (ClassNotFoundException e) {
                              e.printStackTrace();
                              return false;
                          }

                          return false;
                      });
    }


    @Override
    public ConfigurationPhase getConfigurationPhase() {
        return ConfigurationPhase.REGISTER_BEAN;
    }
}
