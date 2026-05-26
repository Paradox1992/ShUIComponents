package shui.security;

public interface Contexable {

    void setContext(ContextInfo contextInfo);

    @java.beans.Transient
    ContextInfo getContext();

}
