package spring.deserve.starter.core;

import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.env.Environment;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.filter.AnnotationTypeFilter;

import java.util.Set;

public class SingletonImportRegistrar implements ImportBeanDefinitionRegistrar, EnvironmentAware {

    private Environment environment;

    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }

    @Override
    @SneakyThrows
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
        // Проверяем наличие property ImportSingletons
        String packageToScan = environment.getProperty("ImportSingletons");

        // Если property отсутствует, ищем класс с аннотацией @SpringBootApplication
        if (packageToScan == null) {
            packageToScan = findSpringBootApplicationPackage(registry);
        }

        // Если пакет найден, сканируем и регистрируем бины с аннотацией @AddSingleton
        if (packageToScan != null) {
            registerSingletons(packageToScan, registry);
        } else {
            throw new IllegalStateException("Не удалось найти пакет для сканирования.");
        }
    }

    @SneakyThrows
    private String findSpringBootApplicationPackage(BeanDefinitionRegistry registry) {
        // Поиск класса с аннотацией @SpringBootApplication
        for (String beanName : registry.getBeanDefinitionNames()) {
            BeanDefinition beanDefinition = registry.getBeanDefinition(beanName);
            String beanClassName = beanDefinition.getBeanClassName();

            if (beanClassName != null) {
                Class<?> beanClass = Class.forName(beanClassName);
                if (beanClass.isAnnotationPresent(SpringBootApplication.class)) {
                    return beanClass.getPackage().getName();
                }
            }
        }
        return null;
    }

    private void registerSingletons(String packageToScan, BeanDefinitionRegistry registry) {
        // Создаем сканер и добавляем фильтр для поиска аннотации @AddSingleton
        ClassPathScanningCandidateComponentProvider scanner = new ClassPathScanningCandidateComponentProvider(false);
        scanner.addIncludeFilter(new AnnotationTypeFilter(Singleton.class));

        // Сканируем пакет и регистрируем найденные классы как синглтоны
        Set<BeanDefinition> candidates = scanner.findCandidateComponents(packageToScan);
        for (BeanDefinition candidate : candidates) {
            GenericBeanDefinition beanDefinition = new GenericBeanDefinition();
            beanDefinition.setBeanClassName(candidate.getBeanClassName());
            beanDefinition.setScope("singleton");
            registry.registerBeanDefinition(candidate.getBeanClassName(), beanDefinition);
        }
    }
}
