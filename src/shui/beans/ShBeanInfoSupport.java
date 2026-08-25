package shui.beans;

import java.awt.Image;
import java.beans.BeanDescriptor;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.beans.SimpleBeanInfo;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import javax.swing.ImageIcon;
import shui.components.base.BaseContainer;

/**
 * Base comun para metadatos JavaBeans usados por la paleta de NetBeans.
 */
public abstract class ShBeanInfoSupport extends SimpleBeanInfo {

    private static final String DEFAULT_ICON = "/shui/assets/recreacion.png";
    private static final Set<String> BASE_CONTAINER_PREFERRED_PROPERTIES = Set.of(
            "subscribedComponent"
    );

    private final Class<?> beanClass;
    private final String displayName;
    private final String description;
    private final String iconPath;
    private final String[] preferredProperties;
    private final Set<String> hiddenProperties;

    protected ShBeanInfoSupport(Class<?> beanClass, String displayName, String description,
            String[] preferredProperties, String... hiddenProperties) {
        this(beanClass, displayName, description, null, preferredProperties, hiddenProperties);
    }

    protected ShBeanInfoSupport(Class<?> beanClass, String displayName, String description, String iconPath,
            String[] preferredProperties, String... hiddenProperties) {
        this.beanClass = beanClass;
        this.displayName = displayName;
        this.description = description;
        this.iconPath = iconPath;
        this.preferredProperties = preferredProperties != null ? preferredProperties.clone() : new String[0];
        this.hiddenProperties = new HashSet<>();
        if (hiddenProperties != null) {
            this.hiddenProperties.addAll(Arrays.asList(hiddenProperties));
        }
    }

    @Override
    public BeanDescriptor getBeanDescriptor() {
        BeanDescriptor descriptor = new BeanDescriptor(beanClass);
        descriptor.setDisplayName(displayName);
        descriptor.setShortDescription(description);
        return descriptor;
    }

    @Override
    public PropertyDescriptor[] getPropertyDescriptors() {
        try {
            Set<String> preferred = new HashSet<>(Arrays.asList(preferredProperties));
            PropertyDescriptor[] descriptors = Introspector
                    .getBeanInfo(beanClass, Introspector.IGNORE_ALL_BEANINFO)
                    .getPropertyDescriptors();

            for (PropertyDescriptor descriptor : descriptors) {
                String name = descriptor.getName();
                descriptor.setPreferred(preferred.contains(name) || isBaseContainerPreferredProperty(name));
                descriptor.setHidden(isHiddenProperty(name));
                configureCommonProperty(descriptor);
            }
            return descriptors;
        } catch (IntrospectionException ex) {
            return new PropertyDescriptor[0];
        }
    }

    @Override
    public int getDefaultPropertyIndex() {
        return preferredProperties.length > 0 ? 0 : -1;
    }

    @Override
    public Image getIcon(int iconKind) {
        java.net.URL url = iconPath != null ? ShBeanInfoSupport.class.getResource(iconPath) : null;
        if (url == null) {
            url = ShBeanInfoSupport.class.getResource(DEFAULT_ICON);
        }
        return url != null ? new ImageIcon(url).getImage() : null;
    }

    protected boolean isHiddenProperty(String property) {
        return hiddenProperties.contains(property);
    }

    private boolean isBaseContainerPreferredProperty(String property) {
        return BaseContainer.class.isAssignableFrom(beanClass)
                && BASE_CONTAINER_PREFERRED_PROPERTIES.contains(property);
    }

    private void configureCommonProperty(PropertyDescriptor descriptor) {
        String name = descriptor.getName();
        if ("subscribedComponent".equals(name)) {
            descriptor.setShortDescription("Componente Shui que funcionara como grupo de suscripcion.");
        } else if ("subscribedComponents".equals(name)) {
            descriptor.setShortDescription("Lista inmodificable de componentes Shui suscritos.");
        }
    }
}
