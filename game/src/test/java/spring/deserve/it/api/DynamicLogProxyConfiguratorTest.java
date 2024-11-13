package spring.deserve.it.api;

import lombok.Getter;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.system.CapturedOutput;
import org.springframework.boot.test.system.OutputCaptureExtension;
import spring.deserve.starter.core.DynamicLogProxyConfigurator;
import spring.deserve.starter.core.Log;

import java.lang.reflect.Proxy;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith({MockitoExtension.class, OutputCaptureExtension.class})
class DynamicLogProxyConfiguratorTest {
    @InjectMocks DynamicLogProxyConfigurator dynamicLogProxyConfigurator;


    public static interface MyClassWithLogAnnotationInterface {
        void tryLog();

    }

    public static class MyClassWithLogAnnotation implements MyClassWithLogAnnotationInterface {
        @Getter
        String myfield = "123124";

        @Override
        @Log("myfield")
        public void tryLog() {

        }
    }


    @Test
    void should_return_javalangproxy() {
        //given
        var myClassWithLogAnnotation = new MyClassWithLogAnnotation();

        //when
        var o = dynamicLogProxyConfigurator.wrapWithProxy(
                myClassWithLogAnnotation,
                myClassWithLogAnnotation.getClass()
        );


        //then
        assertThat(Proxy.isProxyClass(o.getClass()))
                .isTrue();
    }


    @Test
    void should_log_to_out_myfield_because_log_annotation(CapturedOutput   capturedOutput) {
        //given
        var myClassWithLogAnnotation = new MyClassWithLogAnnotation();

        MyClassWithLogAnnotationInterface o = (MyClassWithLogAnnotationInterface) dynamicLogProxyConfigurator.wrapWithProxy(
                myClassWithLogAnnotation,
                myClassWithLogAnnotation.getClass()
        );

        //when
        o.tryLog();

        //then
        assertThat(capturedOutput.toString())
                .as("Should print field name with value because @Log('myfield')")
                .contains("Логирование поля myfield: 123124");
    }
}