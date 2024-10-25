package spring.deserve.it.infra.enhancer;

import lombok.Getter;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.system.CapturedOutput;
import org.springframework.boot.test.system.OutputCaptureExtension;
import spring.deserve.it.game.ApplicationContext;
import spring.deserve.it.game.Log;
import spring.deserve.it.infra.Singleton;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Тест в отдельном пакете для того чтобы создать classpath с котекстом для выполнения когда есть только
 * proxyConfigurator основанный на CGLIB.
 * Костыль из-за нашего механизма детектирования конфигураторов с помощью Reflections и отсутствия механизмов настройки
 */
@Getter
@Singleton
@ExtendWith(OutputCaptureExtension.class)
public class ApplicationContextEnhancerLogTest implements ApplicationContextEnhancerLogTestInterface { //Interface not needed for CGLIB Proxy
    private String testProp = "Test123";

    @Log("testProp")
    public void tryLog() {

    }


    @Test
    void should_get_bean_by_class() {
        //given
        var applicationContext = new ApplicationContext("spring.deserve.it.infra.enhancer");

        //when
        var object = applicationContext.getBean(ApplicationContextEnhancerLogTest.class);

        //then
        assertThat(object)
                .as("Should get not null bean by class")
                .isNotNull();
    }

    @Test
    void should_get_bean_by_interface() {
        //given
        var applicationContext = new ApplicationContext("spring.deserve.it.infra.enhancer");

        //when
        var object = applicationContext.getBean(ApplicationContextEnhancerLogTestInterface.class);

        //then
        assertThat(object)
                .as("Should get not null bean by interface")
                .isNotNull();
    }

    @Test
    void should_throw_exception_when_no_interface(CapturedOutput capturedOutput) {
        //given
        var applicationContext = new ApplicationContext("spring.deserve.it.infra.enhancer");

        //when
        ApplicationContextEnhancerLogTest object = applicationContext.getBean(ApplicationContextEnhancerLogTest.class);
        object.tryLog();

        //then
        assertThat(object)
                .as("Should inject and call try log")
                .isNotNull();
        assertThat(capturedOutput)
                .as("Should log value of testProp")
                .contains("Логирование поля testProp: Test123");
    }


    @Test
    void should_not_throw_exception_when_get_by_existed_interface(CapturedOutput capturedOutput) {
        //given
        var applicationContext = new ApplicationContext("spring.deserve.it.infra.enhancer");
        ApplicationContextEnhancerLogTest object = applicationContext.getBean(ApplicationContextEnhancerLogTest.class);

        //when
        object.tryLog();

        //then
        assertThat(object)
                .as("Should throw class cast exception because threre is no interface and configurator use Dynamic Proxy")
                .isNotNull();
        assertThat(capturedOutput)
                .as("Should log value of testProp")
                .contains("Логирование поля testProp: Test123");
    }
}
