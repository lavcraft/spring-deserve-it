package spring.deserve.starter.core;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.ImportSelector;
import org.springframework.core.env.Environment;
import org.springframework.core.type.AnnotationMetadata;

@RequiredArgsConstructor
public class SingletonImportSelector implements ImportSelector {
    private final Environment environment;

    @Override
    public String[] selectImports(AnnotationMetadata importingClassMetadata) {

        return new String[]{};
    }

}
