package cn.addenda.footprints.client.spring.aop;

import java.lang.annotation.Annotation;

import org.springframework.context.annotation.ImportSelector;
import org.springframework.core.GenericTypeResolver;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;

public abstract class AbstractFootprintsAopModeImportSelector<A extends Annotation> implements ImportSelector {

    public static final String FOOTPRINTS_AOP_MODE_ATTRIBUTE_NAME = "footprintsAopMode";

    protected String getFootprintsAopModeAttributeName() {
        return FOOTPRINTS_AOP_MODE_ATTRIBUTE_NAME;
    }

    @Override
    public final String[] selectImports(AnnotationMetadata importingClassMetadata) {
        Class<?> annType = GenericTypeResolver.resolveTypeArgument(getClass(), AbstractFootprintsAopModeImportSelector.class);
        Assert.state(annType != null, "Unresolvable type argument for FootprintsAopModeImportSelector");
        AnnotationAttributes attributes = AnnotationAttributes.fromMap(importingClassMetadata.getAnnotationAttributes(annType.getName(), false));
        if (attributes == null) {
            throw new IllegalArgumentException(String.format(
                    "@%s is not present on importing class '%s' as expected",
                    annType.getSimpleName(), importingClassMetadata.getClassName()));
        }

        FootprintsAopMode footprintsAopMode = attributes.getEnum(getFootprintsAopModeAttributeName());
        String[] imports = selectImports(footprintsAopMode);
        if (imports == null) {
            throw new IllegalArgumentException("Unknown FootprintsAopMode: " + footprintsAopMode);
        }
        return imports;
    }

    @Nullable
    protected abstract String[] selectImports(FootprintsAopMode footprintsAopMode);

}
