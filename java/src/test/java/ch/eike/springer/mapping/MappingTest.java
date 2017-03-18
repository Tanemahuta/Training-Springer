package ch.eike.springer.mapping;

import ch.eike.spring.domain.DocumentWatermark;
import ch.eike.springer.configuration.JacksonConfiguration;
import ch.eike.springer.watermark.Ticket;
import com.fasterxml.jackson.databind.ObjectMapper;
import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.rules.SpringClassRule;
import org.springframework.test.context.junit4.rules.SpringMethodRule;

import static org.junit.Assert.assertThat;

/**
 * Integration test for the Mixins.
 *
 * @author Tanemahuta
 */
@RunWith(JUnitParamsRunner.class)
@ContextConfiguration(classes = JacksonConfiguration.class)
public class MappingTest {

    @ClassRule
    public static final SpringClassRule SCR = new SpringClassRule();

    @Rule
    public final SpringMethodRule springMethodRule = new SpringMethodRule();

    @Autowired
    public Jackson2ObjectMapperBuilder mapperBuilder;

    private ObjectMapper mapper;

    @Before
    public void createMapper() {
        this.mapper = mapperBuilder.build();
    }

    @Test
    @Parameters
    public void domainObjectsCanBeSerialized(final String resource) throws Exception {
        // when: 'deserializing a resource'
        final DocumentWatermark doc = mapper.readValue(getClass().getResource(resource), DocumentWatermark.class);
        // then: 'the serialized object matches the representation in the resource'
        assertThat(doc, JsonMatcher.fromResource(resource, mapper));
    }

    @Test
    public void ticketSerializationIsSymmetric() throws Exception {
        // when: 'reading the ticket'
        final Ticket ticket = mapper.readValue(JsonMatcher.readResource("ticket.json"), Ticket.class);
        // then: 'the serialization of the ticket is the same as the source'
        assertThat(ticket, JsonMatcher.fromResource("ticket.json", mapper));
    }

    public Object[] parametersForDomainObjectsCanBeSerialized() {
        return new Object[]{
                new Object[]{"book1.json"},
                new Object[]{"book2.json"},
                new Object[]{"journal1.json"}

        };
    }


}
