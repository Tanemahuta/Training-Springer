package ch.eike.springer.mapping;

import ch.eike.springer.domain.BookWatermark;
import ch.eike.springer.domain.Topic;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Jackson Mixin for {@link BookWatermark}.
 *
 * @author Tanemahuta
 */
public abstract class BookWatermarkMixin extends BookWatermark {

    public BookWatermarkMixin(@JsonProperty(value = "title", required = true) String $0,
                              @JsonProperty(value = "author", required = true) String $1,
                              @JsonProperty(value = "topic", required = true) Topic $3) {
        super($0, $1, $3);
    }

}
