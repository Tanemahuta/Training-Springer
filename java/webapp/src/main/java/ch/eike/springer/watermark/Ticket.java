package ch.eike.springer.watermark;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import javax.annotation.Nonnull;
import java.util.UUID;

/**
 * Ticket for a watermarking operation.
 * @author Tanemahuta
 */
@EqualsAndHashCode
@ToString
public class Ticket {

    @Getter(onMethod = @__(@Nonnull))
    private final UUID id;

    public Ticket(@JsonProperty(value = "id", required = true) @Nonnull final UUID id) {
        this.id = id;
    }

}
