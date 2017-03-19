package ch.eike.springer.configuration;

import ch.eike.springer.domain.BookWatermark;
import ch.eike.springer.domain.DocumentWatermark;
import ch.eike.springer.domain.JournalWatermark;
import ch.eike.springer.mapping.BookWatermarkMixin;
import ch.eike.springer.mapping.DocumentWatermarkMixin;
import ch.eike.springer.mapping.JournalWatermarkMixin;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

/**
 * {@link Configuration} bean, which sets up our jackson mappings.
 * @author Tanemahuta
 */
@Configuration
public class JacksonConfiguration {

    @Bean
    public Jackson2ObjectMapperBuilder jacksonObjectMapperBuilder() {
        return Jackson2ObjectMapperBuilder.json().mixIn(DocumentWatermark.class, DocumentWatermarkMixin.class).mixIn(BookWatermark.class, BookWatermarkMixin.class).mixIn(JournalWatermark.class, JournalWatermarkMixin.class);
    }

}
