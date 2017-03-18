package ch.eike.spring.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import javax.annotation.Nonnull;

/**
 * A {@link DocumentWatermark} representing a book with enhanced properties.
 * @author Tanemahuta
 */
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class BookWatermark extends DocumentWatermark {

    /**
     * The topic for the book
     * FIXME The spec did not provide the fact if the topic is an optional property, so I assume it is not.
     */
    @Getter(onMethod = @__(@Nonnull))
    private final Topic topic;


    /**
     * Create a new immutable book with the provided properties.
     *
     * @param title     the title of the document
     * @param author    the author of the document
     * @param topic     the topic for the document
     */
    public BookWatermark(@Nonnull final String title, @Nonnull final String author, @Nonnull final Topic topic) {
        super(title, author);
        this.topic = topic;
    }
}
