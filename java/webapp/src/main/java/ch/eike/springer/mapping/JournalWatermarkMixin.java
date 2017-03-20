package ch.eike.springer.mapping;

import ch.eike.springer.domain.JournalWatermark;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Jackson Mixin for {@link JournalWatermark}
 */
public abstract class JournalWatermarkMixin extends JournalWatermark {

    public JournalWatermarkMixin(@JsonProperty("title") String $0,
                                 @JsonProperty("author") String $1) {
        super($0, $1);
    }

}
