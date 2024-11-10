package spring.deserve.it.game;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.system.CapturedOutput;
import org.springframework.boot.test.system.OutputCaptureExtension;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import spring.deserve.it.api.Log;
import spring.deserve.it.api.LogAspect;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith({SpringExtension.class, OutputCaptureExtension.class})
@RequiredArgsConstructor
public class LogAnnotationTest {
    private final ApplicationContext applicationContext;

    @TestConfiguration
    @EnableAspectJAutoProxy
    public static class Config {

        @Bean
        public MyTestClassWithLog myTestClassWithLog() {
            return new MyTestClassWithLog();
        }

        @Bean
        public MyTestClassWithLogWithGetter myTestClassWithLogWithGetter() {
            return new MyTestClassWithLogWithGetter();
        }

        @Bean
        public LogAspect logAspect() {
            return new LogAspect();
        }
    }

    @Getter
    public static class MyTestClassWithLogWithGetter {
        private String fieldToLog = "fieldToLogValue";

        @Log("fieldToLog")
        public void tryLog() {

        }
    }

    public static class MyTestClassWithLog {
        private String fieldToLog = "fieldToLogValue";

        @Log("fieldToLog")
        public void tryLog() {

        }
    }

    @Test
    void should_log_field_when_call_method_with_log_annotation(CapturedOutput capturedOutput) {
        //given
        var bean = applicationContext.getBean(MyTestClassWithLogWithGetter.class);

        //when
        bean.tryLog();

        //then
        assertThat(capturedOutput.toString())
                .as("Should print field name with value because @Log('myfield')")
                .contains("Логирование поля fieldToLog: fieldToLogValue");
    }

    @Test
    void should_log_error_when_no_getter_for_field_provided(CapturedOutput capturedOutput) {
        //given
        var bean = applicationContext.getBean(MyTestClassWithLog.class);

        //when
        bean.tryLog();

        //then
        assertThat(capturedOutput.toString())
                .as("Should print field name with value because @Log('myfield')")
                .contains("Геттер для поля fieldToLog не найден");
    }
}
