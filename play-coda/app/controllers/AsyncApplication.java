package controllers;

import static utils.JsonUtil.MAPPER;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.Callable;

import javax.annotation.Nullable;

import models.Computer;
import models.PurchasableComputer;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;

import play.libs.Akka;
import play.libs.F.Function;
import play.libs.F.Promise;
import play.libs.WS;
import play.libs.WS.Response;
import play.libs.WS.WSRequestHolder;
import play.mvc.Controller;
import play.mvc.Result;
import utils.PageImpl;

import com.avaje.ebean.Page;
import com.google.common.collect.Lists;

public class AsyncApplication extends Controller {

    // ====================================== Sync stuff ==========================================

    /**
     * Display the list of computers.
     */
    public static Result list(final int page, final String sortBy, final String order, final String filter) {
        final Page<Computer> items = Computer.page(page, 10, sortBy, order, filter);
        List<PurchasableComputer> purchasables = getPurchasableComputers(items);
        final Page<Computer> resultPage = PageImpl.of(purchasables, items);
        return ok(views.html.list.render(resultPage, sortBy, order, filter));
    }

    private static List<PurchasableComputer> getPurchasableComputers(final Page<Computer> items) {
        return Lists.transform(items.getList(), new com.google.common.base.Function<Computer, PurchasableComputer>() {
            @Override
            @Nullable
            public PurchasableComputer apply(@Nullable final Computer computer) {
                try {
                    return getOrderInfo(computer);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

    private static PurchasableComputer getOrderInfo(final Computer computer) throws JsonParseException,
            JsonMappingException, IOException {
        final Response response = orderInfoWs(computer).get().get();
        final JsonNode json = MAPPER.readValue(response.getBodyAsStream(), JsonNode.class);
        int price = json.get("price").getIntValue();
        int availability = json.get("availability").getIntValue();
        return PurchasableComputer.create(computer, price, availability);
    }

    private static WSRequestHolder orderInfoWs(final Computer computer) {
        return WS.url("http://localhost:9001/orderinfo").setQueryParameter("name", computer.name)
                .setQueryParameter("company", computer.getCompanyName());
    }

    // ====================================== Async stuff ==========================================

    /**
     * Display the list of computers.
     */
    public static Result listAsync(final int page, final String sortBy, final String order, final String filter) {
        Promise<Page<Computer>> items = Akka.future(listCallable(page, sortBy, order, filter));
        Promise<Page<Computer>> purchasable = items.flatMap(getPurchasableComputers);
        return async(purchasable.map(new Function<Page<Computer>, Result>() {
            @Override
            public Result apply(Page<Computer> items) throws Throwable {
                return ok(views.html.list.render(items, sortBy, order, filter));
            }
        }));
    }

    private static Callable<Page<Computer>> listCallable(final int page, final String sortBy, final String order,
            final String filter) {
        return new Callable<Page<Computer>>() {
            @Override
            public Page<Computer> call() throws Exception {
                return Computer.page(page, 10, sortBy, order, filter);
            }
        };
    }

    private static Function<Page<Computer>, Promise<Page<Computer>>> getPurchasableComputers = new Function<Page<Computer>, Promise<Page<Computer>>>() {
        @Override
        public Promise<Page<Computer>> apply(final Page<Computer> items) throws Throwable {
            final List<Promise<? extends PurchasableComputer>> purchasablePromises = Lists.transform(items.getList(),
                    getOrderInfoAsync);
            final Promise<List<PurchasableComputer>> purchasablesPromise = Promise.sequence(purchasablePromises);
            return purchasablesPromise.map(new Function<List<PurchasableComputer>, Page<Computer>>() {
                @Override
                public Page<Computer> apply(List<PurchasableComputer> a) throws Throwable {
                    return PageImpl.of(a, items);
                }
            });
        }
    };

    private static final com.google.common.base.Function<Computer, Promise<? extends PurchasableComputer>> getOrderInfoAsync = new com.google.common.base.Function<Computer, Promise<? extends PurchasableComputer>>() {
        @Override
        @Nullable
        public Promise<? extends PurchasableComputer> apply(final @Nullable Computer computer) {
            return orderInfoWs(computer).get().map(new Function<WS.Response, PurchasableComputer>() {
                @Override
                public PurchasableComputer apply(Response a) throws Throwable {
                    final JsonNode json = MAPPER.readValue(a.getBodyAsStream(), JsonNode.class);
                    int price = json.get("price").getIntValue();
                    int availability = json.get("availability").getIntValue();
                    return PurchasableComputer.create(computer, price, availability);
                }
            });
        }
    };

    // =================================== Pure WebService sync/async test
    // ===================================================

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
