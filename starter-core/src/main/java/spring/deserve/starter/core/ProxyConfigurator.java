package spring.deserve.starter.core;

public interface ProxyConfigurator {
    Object wrapWithProxy(Object target, Class<?> originalClass);
}
