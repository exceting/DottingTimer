package dotting.timer.core.builder;

import java.util.Objects;

/**
 * Create by 18073 on 2018/10/29.
 */
public class DottingReference {

    private final DottingSpanContext context;
    private final String referenceType;

    public DottingReference(DottingSpanContext context, String referenceType) {
        this.context = context;
        this.referenceType = referenceType;
    }

    public DottingSpanContext getContext() {
        return context;
    }

    public String getDottingReferenceType() {
        return referenceType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        DottingReference reference = (DottingReference) o;
        return Objects.equals(context, reference.context) &&
                Objects.equals(referenceType, reference.referenceType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(context, referenceType);
    }

}
