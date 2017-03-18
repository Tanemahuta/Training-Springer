package ch.eike.spring.domain;

import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.annotation.Nonnull;

/**
 * A {@link DocumentWatermark} which represents a journal.
 * FIXME I know, we could replace document with journal in the inheritance chain right now, but actually both are different and it would be bad style in terms of the domain spec.
 * @author Tanemahuta
 */
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class JournalWatermark extends DocumentWatermark {
    /**
     * Create a new immutable journal with the provided properties.
     *
     * @param title     the title of the document
     * @param author    the author of the document
     */
    public JournalWatermark(@Nonnull final String title, @Nonnull final String author) {
        super(title, author);
    }
}
