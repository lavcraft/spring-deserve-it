package spring.deserve.it.api;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.core.type.StandardMethodMetadata;
import org.springframework.stereotype.Component;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Arrays;

@Component
public class PlayerQualifierPostProcessor implements BeanPostProcessor, BeanFactoryPostProcessor {
    private ConfigurableListableBeanFactory beanFactory;

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) {
        if (bean instanceof Spider spider) {
            BeanDefinition beanDefinition = beanFactory.getMergedBeanDefinition(beanName);
            if (beanDefinition.getSource() instanceof StandardMethodMetadata source) {
                Method       introspectedMethod  = source.getIntrospectedMethod();
                Annotation[] declaredAnnotations = introspectedMethod.getDeclaredAnnotations();

                String playerName = Arrays.stream(declaredAnnotations)
                                          .filter(annotation ->
                                                          annotation.annotationType().equals(PlayerQualifier.class)
                                          )
                                          .findAny()
                                          .map(ignored ->
                                                       introspectedMethod.getAnnotation(PlayerQualifier.class)
                                                                         .playerName()
                                          )
                                          .orElse("unknown");

                //TODO это может быть factoryMethod java конфигурации
                //  String s = bean.getClass().getAnnotation(PlayerQualifier.class).playerName();
                spider.setOwner(playerName);
            }

        }
        return BeanPostProcessor.super.postProcessBeforeInitialization(bean, beanName);
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
    }
}
