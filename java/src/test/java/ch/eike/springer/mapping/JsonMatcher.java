package ch.eike.springer.mapping;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.IOUtils;
import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.json.JSONException;
import org.skyscreamer.jsonassert.JSONCompare;
import org.skyscreamer.jsonassert.JSONCompareMode;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.nio.charset.Charset;
import java.util.Optional;

/**
 * A {@link org.hamcrest.Matcher} to be used with JSON data.
 *
 * @author Tanemahuta
 */
public class JsonMatcher<T> extends BaseMatcher<T> {

    private final ObjectMapper mapper;
    private final String expected;

    public JsonMatcher(@Nonnull final ObjectMapper mapper, @Nonnull final String expected) {
        this.mapper = mapper;
        this.expected = expected;
    }

    @Override
    public boolean matches(final Object item) {
        try {
            final String actual = serialize(item);
            return !JSONCompare.compareJSON(expected, actual, JSONCompareMode.NON_EXTENSIBLE).failed();
        } catch (final JSONException ex) {
            throw new IllegalStateException("Could not compare: " + item, ex);
        }
    }

    private String serialize(final Object item) {
        try {
            return mapper.writeValueAsString(item);
        } catch (final JsonProcessingException ex) {
            throw new IllegalStateException("Could not serialize: +item", ex);
        }
    }

    @Override
    public void describeTo(final Description description) {
        // NOTE: Not using appendValue for a reason: does not work well with IntelliJ comparison ;)
        description.appendText(expected);
    }

    @Override
    public void describeMismatch(final Object item, final Description description) {
        // NOTE: Not using appendValue for a reason: does not work well with IntelliJ comparison ;)
        description.appendText("was ").appendText(serialize(item));
    }

    /**
     * Create a new {@link Matcher} which uses the provided source to match.
     * @param resourcePath the resource path
     * @param mapper the mapper to be used
     * @param <T> the type of the object to be matched
     * @return the matcher
     */
    public static <T> Matcher<T> fromResource(@Nonnull final String resourcePath, @Nonnull final ObjectMapper mapper) {
        return new JsonMatcher<>(mapper, readResource(resourcePath));
    }

    /**
     * Read the provided class path resource and return it's contents as {@link String}.
     * @param resourcePath the resource path
     * @return the contents as string
     */
    public static String readResource(@Nonnull String resourcePath) {
        try (Reader r = Optional.ofNullable(JsonMatcher.class.getResourceAsStream(resourcePath)).map(is -> new InputStreamReader(is, Charset.forName("UTF-8"))).orElse(null)) {
            if (r == null) {
                throw new IllegalArgumentException("Could not find resource: "+resourcePath);
            }
            final StringWriter sw = new StringWriter();
            IOUtils.copy(r, sw);
            return sw.toString();
        } catch (final IOException ex) {
            throw new IllegalArgumentException("Could not read resource: "+resourcePath);
        }
    }
}
