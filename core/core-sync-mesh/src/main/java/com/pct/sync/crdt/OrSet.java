package com.pct.sync.crdt;

import java.io.Serializable;
import java.util.*;

/**
 * OrSet (Observed-Remove Set)
 * Conjunto CRDT que soporta adiciones y eliminaciones concurrentes sin falsos positivos ni conflictos.
 * Cada adición se asocia a un identificador único (tag). La eliminación sólo retira los tags observados.
 *
 * @param <E> Tipo de los elementos en el conjunto
 */
public final class OrSet<E> implements Serializable {

    public record ElementTag<E>(E element, String tag) implements Serializable {}

    private final Set<ElementTag<E>> addSet;
    private final Set<ElementTag<E>> removeSet;

    public OrSet() {
        this.addSet = new HashSet<>();
        this.removeSet = new HashSet<>();
    }

    private OrSet(Set<ElementTag<E>> addSet, Set<ElementTag<E>> removeSet) {
        this.addSet = new HashSet<>(addSet);
        this.removeSet = new HashSet<>(removeSet);
    }

    public OrSet<E> add(E element) {
        Objects.requireNonNull(element, "element requerido");
        String tag = UUID.randomUUID().toString();
        Set<ElementTag<E>> newAdds = new HashSet<>(this.addSet);
        newAdds.add(new ElementTag<>(element, tag));
        return new OrSet<>(newAdds, this.removeSet);
    }

    public OrSet<E> remove(E element) {
        Objects.requireNonNull(element, "element requerido");
        Set<ElementTag<E>> newRemoves = new HashSet<>(this.removeSet);
        for (ElementTag<E> tag : this.addSet) {
            if (tag.element().equals(element)) {
                newRemoves.add(tag);
            }
        }
        return new OrSet<>(this.addSet, newRemoves);
    }

    public Set<E> read() {
        Set<E> active = new HashSet<>();
        for (ElementTag<E> tag : this.addSet) {
            if (!this.removeSet.contains(tag)) {
                active.add(tag.element());
            }
        }
        return Collections.unmodifiableSet(active);
    }

    public boolean contains(E element) {
        return read().contains(element);
    }

    /**
     * Merge determinista (Unión de addSet y removeSet).
     */
    public OrSet<E> merge(OrSet<E> other) {
        if (other == null) return this;

        Set<ElementTag<E>> mergedAdds = new HashSet<>(this.addSet);
        mergedAdds.addAll(other.addSet);

        Set<ElementTag<E>> mergedRemoves = new HashSet<>(this.removeSet);
        mergedRemoves.addAll(other.removeSet);

        return new OrSet<>(mergedAdds, mergedRemoves);
    }
}
