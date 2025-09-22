package spring.deserve.it.game;

import jakarta.annotation.PreDestroy;
import org.reflections.ReflectionUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.core.type.StandardMethodMetadata;
import spring.deserve.it.api.FailException;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

//TODO - напишите версию которая будет работать с бинами созданными с помощью java Config
// @Configuration public class Config { @Bean Spider customSpider() { return new AbstractSpider() { ... } } }
public class PreDestroyBeanFactoryPostProcessor implements BeanFactoryPostProcessor, BeanPostProcessor {
    private List<String> prototypes = new ArrayList<>();

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        System.out.println("beanName = " + beanName);

        if (prototypes.contains(beanName)) {
            ReflectionUtils.getAllMethods(bean.getClass(), method -> method.isAnnotationPresent(PreDestroy.class))
                           .stream()
                           .findAny()
                           .ifPresent(v -> {
                               throw new FailException();
                           });

        }
        return bean;
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {

        for (var beanDefinitionName : beanFactory.getBeanDefinitionNames()) {
            var beanDefinition = beanFactory.getBeanDefinition(beanDefinitionName);

            if (beanDefinition.isPrototype()) {
                prototypes.add(beanDefinitionName);
                Object source = beanDefinition.getSource();
                if(source instanceof StandardMethodMetadata s) {
                    Class<? extends StandardMethodMetadata> aClass = s.getClass();
                    System.out.println("aClass = " + aClass);

                }
            }
        }
    }
}
