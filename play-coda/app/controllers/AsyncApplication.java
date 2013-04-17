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
     * Display the list of computers.
     * 
     * @throws InterruptedException
     */
    // $ ab -c 200 -n 2000 http://localhost:9000/slowList
    // Requests per second: 149.36 [#/sec] (mean)
    // Time per request: 1339.015 [ms] (mean)
    public static Result slowList(final int page, final String sortBy, final String order, final String filter)
            throws InterruptedException {
        final Page<Computer> items = Computer.page(page, 10, sortBy, order, filter);
        // Ouch, slow backend
        Thread.sleep(50);
        return ok(views.html.list.render(items, sortBy, order, filter));
    }

    // $ ab -c 200 -n 2000 http://localhost:9000/asyncSlowList
    // Requests per second: 158.50 [#/sec] (mean)
    // Time per request: 1261.863 [ms] (mean)
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

    public static Result blockingWs() {
        return ok(WS.url("http://localhost:9001/orderinfo").setQueryParameter("name", "ACE").get().get().getBody());
    }

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
