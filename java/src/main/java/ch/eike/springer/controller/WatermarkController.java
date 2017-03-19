package ch.eike.springer.controller;

import ch.eike.springer.domain.DocumentWatermark;
import ch.eike.springer.watermark.Ticket;
import ch.eike.springer.watermark.WatermarkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Nonnull;
import java.util.Optional;
import java.util.UUID;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

/**
 * The REST controller for {@link ch.eike.springer.watermark.WatermarkService}.
 * NOTE I know it wasn't ask, but you want to take a glimpse of my capabilities, right?
 *
 * @author Tanemahuta
 */
@RestController
@RequestMapping(path = "/watermark")
public class WatermarkController {

    private final WatermarkService watermarkService;

    @Autowired
    public WatermarkController(@Nonnull final WatermarkService watermarkService) {
        this.watermarkService = watermarkService;
    }

    @RequestMapping(method = POST)
    @ResponseBody
    public Ticket submit(@RequestBody final String dummy) {
        return this.watermarkService.process(dummy);
    }

    @RequestMapping(method = GET, path = "/{id}")
    @ResponseBody
    public DocumentWatermark retrieve(@PathVariable("id") final String id) {
        final Ticket ticket = Optional.ofNullable(id).map(UUID::fromString).map(Ticket::new).orElseThrow(() -> new IllegalStateException("Id may not be null."));
        return this.watermarkService.retrieveWatermark(ticket);
    }


}
