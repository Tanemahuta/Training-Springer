package ch.eike.springer.services.watermark;

import ch.eike.springer.domain.DocumentWatermark;
import ch.eike.springer.configuration.ServiceConfiguration;
import ch.eike.springer.services.DefaultTaskExecutor;
import ch.eike.springer.watermark.Ticket;
import ch.eike.springer.watermark.WatermarkService;
import lombok.extern.slf4j.Slf4j;
import org.hamcrest.CoreMatchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

import static org.hamcrest.CoreMatchers.everyItem;
import static org.junit.Assert.assertThat;

/**
 * Unit test for the {@link WatermarkService}.
 *
 * @author Tanemahuta
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = ServiceConfiguration.class)
@Slf4j
public class SimpleWatermarkServiceTest {

    @Autowired
    private WatermarkService watermarkService;

    private ExecutorService executor;

    private AtomicLong scheduleTime = new AtomicLong(0), retrievalTime = new AtomicLong(0);

    @Before
    public void createExecutor() {
        // setup: 'create an executor'
        // NOTE I intentionally use more a multiple of clients here (at last to annoy your machine :P)
        this.executor = Executors.newFixedThreadPool(10 * DefaultTaskExecutor.POOL_SIZE);
    }

    // NOTE: If your one of your unit tests take more than 2 seconds, you're probably doing something wrong
    @Test(timeout = 2000l)
    public void retrievesWatermarksAsynchronically() throws Exception {
        // setup: 'the amount of watermarks to process'
        final int watermarkCount = 100; // Feel free to try out some more here

        // when:
        final Collection<Future<DocumentWatermark>> watermarkFutures = scheduleWatermarks(watermarkCount);
        // and: 'shutting down the executor and await the termination of all tasks'
        stopExecutor(watermarkCount);

        // then: 'All watermarks have been retrieved'
        final List<DocumentWatermark> watermarks = watermarkFutures.stream().map(SimpleWatermarkServiceTest::safeGetFuture).collect(Collectors.toList());
        // and: 'they are not null'
        assertThat(watermarks, everyItem(CoreMatchers. notNullValue(DocumentWatermark.class)));
        log.info("Scheduling duration (excl. thread): sum={} ms, avg= {} ms", scheduleTime.get(), scheduleTime.get() / watermarkCount);
        log.info("Retrieval duration (excl. thread): sum={} ms, avg={} ms", retrievalTime.get(), retrievalTime.get() / watermarkCount);
    }

    /**
     * Stop the {@link #executor}, waiting for a sufficient amount of time.
     *
     * @param watermarkCount the amount of watermarks scheduled
     * @throws InterruptedException
     */
    private void stopExecutor(final int watermarkCount) throws InterruptedException {
        executor.shutdown();
        // NOTE The timeout should be sufficient (double the maximum delay)
        executor.awaitTermination(watermarkCount * 2 * SimpleWatermarkService.MAX_DELAY, TimeUnit.MILLISECONDS);
    }

    /**
     * Schedule an amount of water marks to be created and return
     *
     * @param watermarkCount the amount to be scheduled
     * @return a stream to with futures for the retrieval of the watermark
     */
    @Nonnull
    private Collection<Future<DocumentWatermark>> scheduleWatermarks(final int watermarkCount) {
        // Safe the n futures we've scheduled
        final List<Future<Ticket>> scheduleFutures = new LinkedList<>();
        // Schedule loop
        for (int i = 0; i < watermarkCount; i++) {
            scheduleFutures.add(executor.submit(new ScheduleWatermark(i)));
        }
        // First we have to schedule all the futures, then we can retrieve them
        return Collections.unmodifiableCollection(scheduleFutures.stream() // Stream the futures
                .map(SimpleWatermarkServiceTest::safeGetFuture) // Map to the ticket from each one
                .map(RetrieveWatermark::new) // Create a new operation retrieving the watermark
                .map(executor::submit) // Submit the operation to the executor creating a future
                .collect(Collectors.toList())); // Make sure all the operations are scheduled
    }

    /**
     * Helper method to safely retrieve a future's value.
     *
     * @param f the future
     * @return the result from the future
     */
    private static <T> T safeGetFuture(@Nonnull final Future<T> f) {
        try {
            return f.get();
        } catch (final InterruptedException | ExecutionException ex) {
            throw new IllegalStateException("Future could not be retrieved", ex);
        }
    }

    /**
     * A task which schedules the creation of a watermark and returns a future to retrieve it.
     *
     * @author Tanemahuta
     */
    private class ScheduleWatermark implements Callable<Ticket> {

        private final int id;

        private ScheduleWatermark(final int id) {
            this.id = id;
        }

        @Override
        public Ticket call() throws Exception {
            // Schedule a watermark creation and retrieve the ticket
            final long start = System.currentTimeMillis();
            final Ticket ticket = watermarkService.process("Process #" + id);
            retrievalTime.addAndGet(System.currentTimeMillis()-start);
            log.info("Submitted ticket {}: {}", id, ticket);
            return ticket;
        }
    }

    /**
     * Callable which constantly tries to retrieve a {@link DocumentWatermark} for a {@link Ticket} from {@link #watermarkService}.
     *
     * @author Tanemahuta
     */
    private class RetrieveWatermark implements Callable<DocumentWatermark> {

        private final Ticket ticket;

        /**
         * Create an operation for the provided {@link Ticket}.
         *
         * @param ticket the ticket
         */
        public RetrieveWatermark(@Nonnull final Ticket ticket) {
            this.ticket = ticket;
        }

        @Override
        public DocumentWatermark call() throws Exception {
            DocumentWatermark result;
            final long start = System.currentTimeMillis();
            // NOTE this is your polling
            while ((result = watermarkService.retrieveWatermark(ticket)) == null) {
                Thread.sleep(1);
            }
            retrievalTime.addAndGet(System.currentTimeMillis()-start);
            log.info("Watermark retrieved for ticket {}: {}", ticket, result);
            return result;
        }
    }
}