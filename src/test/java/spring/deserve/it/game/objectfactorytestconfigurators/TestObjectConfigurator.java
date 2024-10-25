package spring.deserve.it.game.objectfactorytestconfigurators;


import spring.deserve.it.game.ApplicationContext;
import spring.deserve.it.game.ObjectConfigurator;

public class TestObjectConfigurator implements ObjectConfigurator {
    public static int calls;
    public static ApplicationContext context;

    @Override
    public void configure(Object object) {
        this.calls++;
    }

    public int calls() {
        return calls;
    }

    @Override
    public void setApplicationContext(ApplicationContext context) {
        this.context = context;

    }
}
