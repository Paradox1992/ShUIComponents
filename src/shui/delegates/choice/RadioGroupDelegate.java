package shui.delegates.choice;

import java.lang.ref.WeakReference;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;

/**
 * Registro simple para exclusividad de radio buttons por nombre de grupo.
 *
 * @param <T> tipo de radio registrado
 */
public class RadioGroupDelegate<T extends RadioGroupDelegate.RadioMember> {

    public interface RadioMember {

        String getGroupName();

        boolean isSelected();

        void setSelectedFromGroup(boolean selected);
    }

    private static final Map<String, Set<WeakReference<RadioMember>>> GROUPS = new WeakHashMap<>();

    private final T owner;

    public RadioGroupDelegate(T owner) {
        this.owner = owner;
    }

    public void register(String groupName) {
        if (groupName == null || groupName.isBlank()) {
            return;
        }
        GROUPS.computeIfAbsent(groupName, key -> java.util.Collections.newSetFromMap(new WeakHashMap<>()))
                .add(new WeakReference<>(owner));
    }

    public void selectOnlyCurrent() {
        String groupName = owner.getGroupName();
        if (groupName == null || groupName.isBlank()) {
            return;
        }
        Set<WeakReference<RadioMember>> members = GROUPS.get(groupName);
        if (members == null) {
            return;
        }
        Iterator<WeakReference<RadioMember>> iterator = members.iterator();
        while (iterator.hasNext()) {
            RadioMember member = iterator.next().get();
            if (member == null) {
                iterator.remove();
            } else if (member != owner && member.isSelected()) {
                member.setSelectedFromGroup(false);
            }
        }
    }
}
