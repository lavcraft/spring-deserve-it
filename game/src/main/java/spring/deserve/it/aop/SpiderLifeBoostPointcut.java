package spring.deserve.it.aop;

import org.springframework.aop.ClassFilter;
import org.springframework.aop.support.DynamicMethodMatcherPointcut;
import spring.deserve.it.game.AbstractSpider;

import java.lang.reflect.Method;
import java.util.List;

public class SpiderLifeBoostPointcut extends DynamicMethodMatcherPointcut {

    // Ключевые строки для фильтрации классов пауков
    private final List<String> targetClassKeywords = List.of("tone");

    // Проверяем, подходит ли класс
    @Override
    public ClassFilter getClassFilter() {
        return clazz -> AbstractSpider.class.isAssignableFrom(clazz) && targetClassKeywords.stream()
                                                                                           .anyMatch(keyword -> clazz.getSimpleName().contains(keyword));
    }

    // Проверяем, подходит ли метод
    @Override
    public boolean matches(Method method, Class<?> targetClass, Object... args) {
        return "fight".equals(method.getName()); // Применяется только к методу makeMove
    }
}
