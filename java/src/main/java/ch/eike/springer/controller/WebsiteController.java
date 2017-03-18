package ch.eike.springer.controller;

import ch.eike.spring.domain.DocumentWatermark;
import ch.eike.springer.watermark.Ticket;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;
import java.util.UUID;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

/**
 * Created by Tanemahuta on 18.03.17.
 */
@Controller
public class WebsiteController {

    private final WatermarkController watermarkController;

    @Autowired
    public WebsiteController(final WatermarkController watermarkController) {
        this.watermarkController = watermarkController;
    }

    @RequestMapping(method = {POST, GET}, path = "/")
    public String submitFromWebPage(Model model) {
        final Ticket ticket = this.watermarkController.submit(UUID.randomUUID().toString());
        model.addAttribute("id", ticket.getId());
        return "submitted";
    }

    @RequestMapping(method = GET, path = "/query")
    public String query(@RequestParam("id") final String id, Model model) {
        final DocumentWatermark watermark = watermarkController.retrieve(id);
        model.addAttribute("watermark", Optional.ofNullable(watermark.toString()).orElse("Not ready yet."));
        return "query";
    }
}
