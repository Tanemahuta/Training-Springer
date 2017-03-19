package ch.eike.springer.mapping;

import ch.eike.springer.domain.BookWatermark;
import ch.eike.springer.domain.DocumentWatermark;
import ch.eike.springer.domain.JournalWatermark;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

/**
 * Jackson mixin class for mapping documents.
 * <p>
 * NOTE Separate the domain from jackson, so we can provided the library without dependencies)
 *
 * @author Tanemahuta
 */
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "content",
        visible = true)
@JsonSubTypes({
        @JsonSubTypes.Type(value = BookWatermark.class, name = "book"),
        @JsonSubTypes.Type(value = JournalWatermark.class, name = "journal")
})
// NOTE I am inheriting here to provoke compiler errors on domain definition changes
public abstract class DocumentWatermarkMixin extends DocumentWatermark {

    protected DocumentWatermarkMixin(@JsonProperty(value = "title", required = true) String $0,
                                     @JsonProperty(value = "author", required = true) String $1) {
        super($0, $1);
    }

}
