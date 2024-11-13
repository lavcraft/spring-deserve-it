package spring.deserve.it.game;

import jakarta.annotation.PreDestroy;
import org.reflections.ReflectionUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;

import java.util.ArrayList;
import java.util.List;

public class PreDestroyBFPP implements BeanFactoryPostProcessor, BeanPostProcessor {
    private List<String> proptypes = new ArrayList<>();

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        if (proptypes.contains(beanName)) {
            ReflectionUtils.getAllMethods(bean.getClass(), method -> method.isAnnotationPresent(PreDestroy.class))
                           .stream()
                           .findAny()
                           .ifPresent(v -> System.exit(1));

        }
        return bean;
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {

        for (var beanDefinitionName : beanFactory.getBeanDefinitionNames()) {
            var beanDefinition = beanFactory.getBeanDefinition(beanDefinitionName);

            if (beanDefinition.isPrototype()) {
                proptypes.add(beanDefinitionName);
            }
        }
    }
}
