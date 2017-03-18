package ch.eike.springer.controller;

import ch.eike.spring.domain.DocumentWatermark;
import ch.eike.spring.domain.JournalWatermark;
import ch.eike.springer.configuration.JacksonConfiguration;
import ch.eike.springer.watermark.Ticket;
import ch.eike.springer.watermark.WatermarkService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Unit test for {@link WatermarkController}.
 *
 * @author Tanemahuta
 */
@RunWith(SpringRunner.class)
@WebMvcTest(WatermarkController.class)
@ContextConfiguration(classes = {JacksonConfiguration.class, WatermarkController.class})
public class WatermarkControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private Jackson2ObjectMapperBuilder jackson2ObjectMapperBuilder;
    @MockBean
    private WatermarkService watermarkService;


    private ObjectMapper mapper;

    @Before
    public void createMapper() {
        this.mapper = jackson2ObjectMapperBuilder.build();
    }

    @Test
    public void submit() throws Exception {
        // setup:
        final Ticket ticket = new Ticket(UUID.randomUUID());
        final String submission = "All your base are belong to us!";
        given(watermarkService.process(submission)).willReturn(ticket);

        // expect:
        mvc.perform(post("/watermark/").accept(MediaType.APPLICATION_JSON).content(submission))
                .andExpect(status().isOk()).andExpect(content().json(mapper.writeValueAsString(ticket)));
    }

    @Test
    public void retrieve() throws Exception {
        // setup:
        final Ticket ticket = new Ticket(UUID.randomUUID());
        final DocumentWatermark watermark = new JournalWatermark("AYBABTU", "Zero Wing");
        given(watermarkService.retrieveWatermark(ticket)).willReturn(watermark);

        // expect:
        mvc.perform(get("/watermark/"+ticket.getId()).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andExpect(content().json(mapper.writeValueAsString(watermark)));

    }


}