package spring.deserve.it.api;

import org.springframework.boot.diagnostics.AbstractFailureAnalyzer;
import org.springframework.boot.diagnostics.FailureAnalysis;

class MyFailureAnalyzer extends AbstractFailureAnalyzer<FailException> {
    public MyFailureAnalyzer() {
        System.out.println("dffds");
    }

    @Override
    protected FailureAnalysis analyze(Throwable rootFailure, FailException cause) {
        return new FailureAnalysis("Pre destroy is bad", "Pleasre remove this annotation", cause);
    }
}
