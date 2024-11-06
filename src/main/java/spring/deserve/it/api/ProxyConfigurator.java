package spring.deserve.it.api;

public interface ProxyConfigurator {
    Object wrapWithProxy(Object target, Class<?> originalClass);
}
