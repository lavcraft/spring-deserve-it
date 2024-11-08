package spring.deserve.it.api;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

@Aspect
@Component
public class LogAspect {
    @Around("@annotation(spring.deserve.it.api.Log)")
    public Object logAnnotatedMethods(ProceedingJoinPoint joinPoint) throws Throwable {
        // Получаем оригинальный метод
        Method method = getMethodFromJoinPoint(joinPoint);

        // Проверяем наличие аннотации @Sobacolog
            Log logAnnotation = method.getAnnotation(Log.class);
            Object target = joinPoint.getTarget();
            Class<?> targetClass = target.getClass();

            // Логика для аннотации @Sobacolog
            for (String fieldName : logAnnotation.value()) {
                try {
                    // Используем геттер для получения значений полей
                    Method getter = targetClass.getMethod("get" + capitalize(fieldName));
                    Object fieldValue = getter.invoke(target);
                    System.out.println("Логирование поля " + fieldName + ": " + fieldValue);
                } catch (NoSuchMethodException e) {
                    System.out.println("Геттер для поля " + fieldName + " не найден.");
                }
            }

        // Выполнение метода оригинального объекта
        return joinPoint.proceed();
    }

    // Метод для получения оригинального метода из JoinPoint
    private Method getMethodFromJoinPoint(ProceedingJoinPoint joinPoint) throws NoSuchMethodException {
        String methodName = joinPoint.getSignature().getName();
        Class<?>[] parameterTypes = ((MethodSignature) joinPoint.getSignature()).getParameterTypes();
        return joinPoint.getTarget().getClass().getMethod(methodName, parameterTypes);
    }

    // Метод для преобразования имени поля в формат "getFieldName"
    private String capitalize(String fieldName) {
        return Character.toUpperCase(fieldName.charAt(0)) + fieldName.substring(1);
    }
}
