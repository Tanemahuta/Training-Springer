package ch.eike.spring.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import javax.annotation.Nonnull;

/**
 * Representation of the meta data for a document.
 *
 * @author Tanemahuta
 */
@EqualsAndHashCode
@ToString
public abstract class DocumentWatermark {

    /**
     * The title of the document
     */
    @Getter(onMethod = @__(@Nonnull))
    private final String title;

    /**
     * The author of the document
     */
    @Getter(onMethod = @__(@Nonnull))
    private final String author;

    /**
     * Create a new immutable document with the provided properties.
     *
     * @param title     the title of the document
     * @param author    the author of the document
     */
    protected DocumentWatermark(@Nonnull final String title,
                                @Nonnull final String author) {
        this.title = title;
        this.author = author;
    }
}
