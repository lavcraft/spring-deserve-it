package spring.deserve.it.infra.dynamic;

import lombok.Getter;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.system.CapturedOutput;
import org.springframework.boot.test.system.OutputCaptureExtension;
import spring.deserve.it.game.ApplicationContext;
import spring.deserve.it.game.Log;

import static org.assertj.core.api.Assertions.assertThat;


/**
 * Тест в отдельном пакете для того чтобы создать classpath с котекстом для выполнения когда есть только
 * proxyConfigurator основанный на DynamicProxy.
 * Костыль из-за нашего механизма детектирования конфигураторов с помощью Reflections и отсутствия механизмов настройки
 */
@Getter
@ExtendWith(OutputCaptureExtension.class)
public class ApplicationContextDynamicProxyLogTest implements ApplicationContextDynamicProxyLogTestInterface {
    private String someProp = "someProp";

    @Log("someProp")
    @Override
    public void tryLog() {
    }

    @Test
    void should_get_bean_by_interface() {
        //given
        ApplicationContext context = new ApplicationContext("spring.deserve.it.infra.dynamic");
        ApplicationContextDynamicProxyLogTestInterface bean = context.getBean(ApplicationContextDynamicProxyLogTestInterface.class);

        //expect
        assertThat(bean)
                .as("Should get bean by interface")
                .isNotNull();
    }

    @Test
    void should_not_work_without_interface() {
        //given
        ApplicationContext context = new ApplicationContext("spring.deserve.it.infra.dynamic");

        //expect
        Assertions.assertThatThrownBy(() -> {
            ApplicationContextDynamicProxyLogTest bean = context.getBean(ApplicationContextDynamicProxyLogTest.class);
        });
    }

    @Test
    void should_log_some_field_by_interface(CapturedOutput capturedOutput) {
        //given
        ApplicationContext context = new ApplicationContext("spring.deserve.it.infra.dynamic");
        ApplicationContextDynamicProxyLogTestInterface bean = context.getBean(ApplicationContextDynamicProxyLogTestInterface.class);

        //when
        bean.tryLog();

        assertThat(capturedOutput.toString())
                .as("Should log valid data")
                .contains("someProp = someProp");
    }

}
