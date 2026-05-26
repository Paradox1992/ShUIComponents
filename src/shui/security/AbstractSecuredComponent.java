package shui.security;

public abstract class AbstractSecuredComponent implements Contexable {

    private ContextInfo context;

    @Override
    public void setContext(ContextInfo contextInfo) {
        this.context = contextInfo;
    }

    @Override
    @java.beans.Transient
    public ContextInfo getContext() {
        return this.context;
    }

    protected final boolean hasContext() {
        return this.context != null;
    }
}
