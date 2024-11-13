package spring.deserve.it.aop;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import spring.deserve.it.game.AbstractSpider;

public class SpiderLifeBoostAdvice implements MethodInterceptor {

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        // Получаем паука и выполняем метод makeMove
        Object target = invocation.getThis();
        Object result = invocation.proceed();

        // Если паук — наследник AbstractSpider, проверяем и восстанавливаем жизни
        if (target instanceof AbstractSpider spider && spider.getLives() == 1) {
            spider.setLives(10); // Восстанавливаем до 10 жизней
            System.out.println("Жизни восстановлены до 10 для паука: " + spider.getClass().getSimpleName());
        }

        return result;
    }
}
