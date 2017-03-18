package ch.eike.springer.watermark;

import ch.eike.spring.domain.DocumentWatermark;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Created by Tanemahuta on 18.03.17.
 */
public interface WatermarkService {

    /**
     * Retrieve a watermark for the provided ticket.
     * @param ticket the ticket
     * @return the {@link DocumentWatermark}
     */
    @Nullable
    DocumentWatermark retrieveWatermark(@Nonnull final Ticket ticket);

    /**
     * Watermark the provided dummy.
     * NOTE you did not provide the type of the document, so I am using a dummy here.
     * @param dummy the dummy (this could be anything according to the spec)
     * @return the ticket
     */
    @Nonnull
    Ticket process(@Nonnull final String dummy);

}
