package ch.eike.springer.services.watermark;

import ch.eike.spring.domain.BookWatermark;
import ch.eike.spring.domain.DocumentWatermark;
import ch.eike.spring.domain.JournalWatermark;
import ch.eike.spring.domain.Topic;
import ch.eike.springer.watermark.Ticket;
import ch.eike.springer.watermark.WatermarkService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.Charset;
import java.security.SecureRandom;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * A simple implementation of the {@link WatermarkService}.
 * @author Tanemahuta
 */
@Component
@Slf4j
public class SimpleWatermarkService implements WatermarkService {

    public static final int MAX_DELAY = 20;
    private SecureRandom secureRandom = new SecureRandom(); // No seed ;)

    private final Map<Ticket, DocumentWatermark> watermarks = new ConcurrentHashMap<>();
    private final List<String> authors = readTextFile("authors.txt");
    private final List<String> titles = readTextFile("titles.txt");
    private final List<Topic> topics = Collections.unmodifiableList(readTextFile("topics.txt").stream().map(String::trim)
            .map(s -> !StringUtils.isEmpty(s.trim()) ? Topic.valueOf(s) : null)
            .collect(Collectors.toList()));

    private final TaskExecutor scheduler;

    @Autowired
    public SimpleWatermarkService(@Nonnull final TaskExecutor scheduler) {
        this.scheduler = scheduler;
        if (authors.isEmpty() || titles.isEmpty() || topics.isEmpty()) {
            throw new IllegalStateException("Something went wrong, could not load the demo files.");
        }
    }

    @Override
    @Nullable
    public DocumentWatermark retrieveWatermark(@Nonnull final Ticket ticket) {
        return watermarks.get(ticket);
    }

    @Override
    @Nonnull
    public Ticket process(@Nonnull final String dummy) {
        final Ticket ticket = new Ticket(UUID.randomUUID());
        // Schedule the processing with a fixed delay.
        scheduler.execute(new CreateWatermarkTask(ticket));
        log.debug("Scheduled watermark task for ticket: {}", ticket);
        return ticket;
    }

    private class CreateWatermarkTask implements Runnable {

        private final Ticket ticket;

        public CreateWatermarkTask(@Nonnull final Ticket ticket) {
            this.ticket = ticket;
        }

        @Override
        public void run() {
            final String author = nextRandom(authors);
            final String title = nextRandom(titles);
            final Topic topic = nextRandom(topics);
            try {
                Thread.sleep(1+secureRandom.nextInt(MAX_DELAY-1));
            } catch (final InterruptedException ex) {
                log.warn("Couldn't delay the processing", ex);
            }

            final DocumentWatermark watermark = topic != null ? new BookWatermark(author, title, topic) : new JournalWatermark(author, title);
            watermarks.put(ticket, watermark);
            log.info("Watermark for ticket {} processed: {}", ticket, watermark);
        }
    }

    private static List<String> readTextFile(@Nonnull final String name) {
        try (Reader r = Optional.ofNullable(SimpleWatermarkService.class.getResourceAsStream(name)).map(is -> new InputStreamReader(is, Charset.forName("UTF-8"))).orElse(null)) {
            if (r == null) {
                throw new IllegalArgumentException("Classpath resource not found: " + name);
            }
            final List<String> list = IOUtils.readLines(r);
            log.info("Loaded {} items from: {}", list.size(), name);
            return Collections.unmodifiableList(list);
        } catch (final IOException ex) {
            throw new IllegalArgumentException("Could not read classpath resource: " + name);
        }
    }

    private <T> T nextRandom(final List<T> source) {
        return source.get(secureRandom.nextInt(source.size()));
    }

}
