package spring.deserve.it.game;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import spring.deserve.it.api.Log;

@ExtendWith(SpringExtension.class)
public class LogAnnotationTest {

    public static class MyTestClassWithLog {
        private String fieldToLog = "fieldToLogValue";

        @Log("fieldToLog")
        void tryLog() {

        }
    }

    @Test
    void should_log_field_when_call_method_with_log_annotation() {

    }
}
