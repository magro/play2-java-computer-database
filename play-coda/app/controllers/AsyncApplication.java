package controllers;

import java.util.concurrent.Callable;

import models.Computer;
import play.libs.Akka;
import play.libs.F.Function;
import play.libs.F.Promise;
import play.libs.WS;
import play.libs.WS.Response;
import play.mvc.Controller;
import play.mvc.Result;

import com.avaje.ebean.Page;

public class AsyncApplication extends Controller {

    // ====================================== Sync/Async slow list ====================================

    /**
     * Display the list of computers, simulates a longer running database call in a synchronous, blocking manner.
     * <p>
     * A test with apache bench using 200 concurrent requests (and 2000 requests in total) results in ~150
     * requests/second, ~1340 ms/req: <code><pre>
     * $ ab -c 200 -n 2000 http://localhost:9000/slowList
     * Requests per second: 149.36 [#/sec] (mean)
     * Time per request: 1339.015 [ms] (mean)
     * </pre></code>
     * </p>
     */
    public static Result slowList(final int page, final String sortBy, final String order, final String filter)
            throws InterruptedException {
        final Page<Computer> items = Computer.page(page, 10, sortBy, order, filter);
        // Ouch, slow backend
        Thread.sleep(50);
        return ok(views.html.list.render(items, sortBy, order, filter));
    }

    /**
     * Display the list of computers, simulates a longer running database call in an <em>asynchronous</em> manner. The
     * db call is still performed using blocking I/O, thus the thread running the db call is reserved (but idle) during
     * I/O.
     * <p>
     * A test with apache bench using 200 concurrent requests (and 2000 requests in total) results in ~150
     * requests/second, ~1260 ms/req: <code><pre>
     * $ ab -c 200 -n 2000 http://localhost:9000/asyncSlowList
     * Requests per second: 158.50 [#/sec] (mean)
     * Time per request: 1261.863 [ms] (mean)
     * </pre></code>
     * </p>
     * This shows that running blocking calls in the background doesn't help much out of the box. In this situation the
     * used thread pool has to be tuned to achieve improvements. Still, in terms of scalability/efficiency blocking I/O
     * remains blocking I/O and consumes threads. NIO is needed (see {@link #nonBlockingWs()}).
     */
    public static Result asyncSlowList(final int page, final String sortBy, final String order, final String filter)
            throws InterruptedException {
        Promise<Page<Computer>> promise = Akka.future(new Callable<Page<Computer>>() {
            @Override
            public Page<Computer> call() throws Exception {
                // Ouch, slow backend
                Thread.sleep(50);
                return Computer.page(page, 10, sortBy, order, filter);
            }
        });
        return async(promise.map(new Function<Page<Computer>, Result>() {
            @Override
            public Result apply(Page<Computer> a) throws Throwable {
                return ok(views.html.list.render(a, sortBy, order, filter));
            }
        }));
    }

    // ============================ Pure WebService sync/async test =======================

    /*
     * The following actions perform calls to some sample backend, that needs at least 50 msec for each call. To start
     * the backend go to "../coda-backend" and start the play app on port 9001: $ cd ../coda-backend $ play
     * -Dhttp.port=9001 start
     */

    /**
     * Simulates synchronous, blocking WebService call.
     * <p>
     * The play WS library is used but waits for the promise/future result to simulate blocking I/O. As WS
     * (async-http-client behind the scenes) in fact performs NIO in the background, performance will still be better
     * than real blocking I/O.
     * </p>
     * ab test results: <code><pre>
     * $ ab -c 1000 -n 20000 http://localhost:9000/blockingWs
     * Requests per second:    1206.12 [#/sec] (mean)
     * Time per request:       829.105 [ms] (mean)
     * </pre></code>
     */
    public static Result blockingWs() {
        return ok(WS.url("http://localhost:9001/orderinfo").setQueryParameter("name", "ACE").get().get().getBody());
    }

    /**
     * Simulates an asynchronous, non-blocking WebService call.
     * <p>
     * The play WS library (async-http-client behind the scenes) performs NIO, thus we can directly work with the
     * returned {@link Promise}.
     * </p>
     * ab test results: <code><pre>
     * $ ab -c 1000 -n 20000 http://localhost:9000/nonBlockingWs
     * Requests per second:    4251.45 [#/sec] (mean)
     * Time per request:       235.214 [ms] (mean)
     * </pre></code>
     */
    public static Result nonBlockingWs() {
        return async(WS.url("http://localhost:9001/orderinfo").setQueryParameter("name", "ACE").get()
                .map(new Function<WS.Response, Result>() {
                    @Override
                    public Result apply(Response a) throws Throwable {
                        return ok(a.getBody());
                    }
                }));
    }

}
