package io.github.amayaframework.cors;

import com.github.romanqed.jconv.SyncTask;
import com.github.romanqed.jconv.Task;
import io.github.amayaframework.context.HttpContext;
import io.github.amayaframework.context.HttpRequest;
import io.github.amayaframework.context.HttpResponse;
import io.github.amayaframework.http.HttpCode;
import io.github.amayaframework.http.HttpMethod;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public final class CorsTaskTests {

    @Test
    public void stringAccessBuilderBuildReturnsLowercaseSet() {
        var builder = new StringAccessBuilder();
        builder.allow("AAA", "bBb");
        var result = builder.build();
        assertNotNull(result);
        assertTrue(result.contains("aaa"));
        assertTrue(result.contains("bbb"));
        assertEquals(2, result.size());
        assertThrows(UnsupportedOperationException.class, () -> result.add("ccc"));
    }

    @Test
    public void compileOriginBuilderBuildRegexesAndStrict() {
        var builder = new CompileOriginBuilder();
        builder.addRegex("^https?://example\\.com$");
        builder.allow("http://foo.com", "https://bar.com");

        var regexes = builder.buildRegexes();
        assertNotNull(regexes);
        assertEquals(1, regexes.size());
        assertTrue(regexes.get(0).matcher("http://example.com").matches());

        var strict = builder.buildStrict();
        assertNotNull(strict);
        assertEquals(2, strict.size());
        assertTrue(strict.contains("http://foo.com"));
        assertTrue(strict.contains("https://bar.com"));

        assertNull(builder.allowedRegexes());
        assertNull(builder.allowed());
    }

    @Test
    public void corsTaskCheckOriginAndRenderOrigin() {
        var config = new CorsConfig();
        config.setAllowedOrigins(Set.of("http://allowed.com"));
        config.setAllowedRegexes(List.of(Pattern.compile(".*\\.example\\.org")));
        var task = new CorsTask(config);

        assertTrue(task.checkOrigin("http://allowed.com"));
        assertTrue(task.checkOrigin("sub.example.org"));
        assertFalse(task.checkOrigin("http://forbidden.com"));

        config.setAllowedOrigins(null);
        config.setAllowedRegexes(null);
        var task2 = new CorsTask(config);
        assertEquals("*", task2.renderOrigin("any"));
    }

    @Test
    public void corsTaskHandlePreflightSetsHeaders() {
        var req = mock(HttpRequest.class);
        var res = mock(HttpResponse.class);

        when(req.getHeader(CorsHeaders.ORIGIN)).thenReturn("http://a.com");
        when(req.getHeader(CorsHeaders.ACCESS_CONTROL_REQUEST_HEADERS)).thenReturn("X-Test");
        when(req.getMethod()).thenReturn(HttpMethod.OPTIONS);

        var config = new CorsConfig();
        config.setAllowedOrigins(Set.of("http://a.com"));
        config.setAllowedMethods(Set.of(HttpMethod.GET));
        config.setAllowedHeaders(Set.of("x-test"));
        config.setAllowCredentials(true);
        config.setMaxAge(100);

        var task = new CorsTask(config);
        task.handlePreflight(req, res, "http://a.com", "GET");

        verify(res).setStatus(HttpCode.NO_CONTENT);
        verify(res).setHeader(CorsHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, "http://a.com");
        verify(res).setHeader(CorsHeaders.ACCESS_CONTROL_ALLOW_METHODS, "get");
        verify(res).setHeader(CorsHeaders.ACCESS_CONTROL_ALLOW_HEADERS, "x-test");
        verify(res).setHeader(CorsHeaders.ACCESS_CONTROL_ALLOW_CREDENTIALS, "true");
        verify(res).setHeader(CorsHeaders.ACCESS_CONTROL_MAX_AGE, "100");
        verify(res).setHeader(ProxyHeaders.VARY, ProxyHeaders.CREDENTIALS_PREFLIGHT_VALUE);
    }

    @Test
    public void corsTaskHandlePlainRequestWithoutCredentials() {
        var res = mock(HttpResponse.class);
        var config = new CorsConfig();
        config.setAllowedOrigins(null);
        config.setAllowCredentials(false);
        var task = new CorsTask(config);

        task.handlePlainRequest(res, "http://any.com");

        verify(res).setHeader(CorsHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, "*");
        verify(res).setHeader(CorsHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, "*");
        verify(res).setHeader(ProxyHeaders.VARY, ProxyHeaders.ORIGIN_VALUE);
    }

    @Test
    public void corsTaskRunCallsNextWhenNoOrigin() throws Throwable {
        var context = mock(HttpContext.class);
        var req = mock(HttpRequest.class);
        var res = mock(HttpResponse.class);
        var next = new TestTask();

        when(context.request()).thenReturn(req);
        when(context.response()).thenReturn(res);
        when(req.getHeader(CorsHeaders.ORIGIN)).thenReturn(null);

        var config = new CorsConfig();
        var task = new CorsTask(config);

        task.run(context, next);
        assertTrue(next.isRan());
    }

    @Test
    @SuppressWarnings("unchecked")
    public void corsTaskRunHandlesPreflightWithDeniedMethod() throws Throwable {
        var context = mock(HttpContext.class);
        var req = mock(HttpRequest.class);
        var res = mock(HttpResponse.class);

        when(context.request()).thenReturn(req);
        when(context.response()).thenReturn(res);
        when(req.getHeader(CorsHeaders.ORIGIN)).thenReturn("http://a.com");
        when(req.getHeader(CorsHeaders.ACCESS_CONTROL_REQUEST_METHOD)).thenReturn("POST");
        when(req.getMethod()).thenReturn(HttpMethod.OPTIONS);

        var config = new CorsConfig();
        config.setAllowedOrigins(Set.of("http://a.com"));
        config.setAllowedMethods(Set.of(HttpMethod.GET)); // POST не разрешён
        var task = new CorsTask(config);

        task.run(context, SyncTask.EMPTY);
        verify(res).setStatus(HttpCode.NO_CONTENT);
        verify(res, never()).setHeader(eq(CorsHeaders.ACCESS_CONTROL_ALLOW_METHODS), anyString());
    }

    @Test
    public void corsTaskRunAsyncCompletesNormally() throws Exception {
        var context = mock(HttpContext.class);
        var req = mock(HttpRequest.class);
        var res = mock(HttpResponse.class);
        var next = new TestTask();

        when(context.request()).thenReturn(req);
        when(context.response()).thenReturn(res);
        when(req.getHeader(CorsHeaders.ORIGIN)).thenReturn(null);

        var config = new CorsConfig();
        var task = new CorsTask(config);

        var future = task.runAsync(context, next);

        assertTrue(future.isDone());
        assertTrue(next.isRan());
    }

    static final class TestTask implements Task<HttpContext> {
        private final AtomicBoolean ran = new AtomicBoolean();

        boolean isRan() {
            return ran.get();
        }

        @Override
        public void run(HttpContext context) throws Throwable {
            ran.set(true);
        }

        @Override
        public CompletableFuture<Void> runAsync(HttpContext context) {
            ran.set(true);
            return CompletableFuture.completedFuture(null);
        }
    }
}
