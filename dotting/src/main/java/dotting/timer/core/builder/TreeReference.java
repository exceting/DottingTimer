package dotting.timer.core.builder;

import java.util.Objects;

/**
 * Create by 18073 on 2018/10/29.
 */
public class TreeReference {

    private final TreeSpanContext context;
    private final String referenceType;

    public TreeReference(TreeSpanContext context, String referenceType) {
        this.context = context;
        this.referenceType = referenceType;
    }

    public TreeSpanContext getContext() {
        return context;
    }

    public String getTreeReferenceType() {
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
        dotting.timer.core.builder.TreeReference reference = (dotting.timer.core.builder.TreeReference) o;
        return Objects.equals(context, reference.context) &&
                Objects.equals(referenceType, reference.referenceType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(context, referenceType);
    }

}
