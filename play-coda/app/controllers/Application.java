package controllers;

import static play.data.Form.form;

import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import models.Computer;
import play.Play;
import play.Routes;
import play.cache.Cached;
import play.core.NamedThreadFactory;
import play.data.Form;
import play.i18n.Messages;
import play.libs.Akka;
import play.libs.F.Function;
import play.libs.F.Promise;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security;
import scala.concurrent.ExecutionContext;
import akka.dispatch.ExecutionContexts;
import akka.dispatch.Futures;

import com.avaje.ebean.Page;
import com.google.common.collect.ImmutableMap;

public class Application extends Controller {

    /**
     * This result directly redirect to application home.
     */
    public static Result GO_HOME = redirect(routes.Application.list(0, "name", "asc", ""));

    private static final int partitionCount = Play.application().configuration().getInt("db.default.partitionCount");
    private static final int maxConnections = partitionCount
            * Play.application().configuration().getInt("db.default.maxConnectionsPerPartition");
    private static final int minConnections = partitionCount
            * Play.application().configuration().getInt("db.default.minConnectionsPerPartition");

    private static final ThreadPoolExecutor tpe = new ThreadPoolExecutor(minConnections, maxConnections, 0L,
            TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>(), new NamedThreadFactory("dbEc"));
    private static final ExecutionContext dbEc = ExecutionContexts.fromExecutorService(tpe);

    public static Result index() {
        return ok(views.html.index.render("Your new application is ready."));
    }

    /**
     * Display the list of computers.
     */
    public static Result list(int page, String sortBy, String order, String filter) {
        return ok(views.html.list.render(Computer.page(page, 10, sortBy, order, filter), sortBy, order, filter));
    }

    /**
     * Display the list of computers.
     */
    public static Result listAsync(final int page, final String sortBy, final String order, final String filter) {
        Promise<Page<Computer>> promise = Akka.future(listCallable(page, sortBy, order, filter));
        return async(promise.map(new Function<Page<Computer>, Result>() {

            @Override
            public Result apply(Page<Computer> items) throws Throwable {
                return ok(views.html.list.render(items, sortBy, order, filter));
            }
        }));
    }

    /**
     * Display the list of computers.
     */
    public static Result listAsync2(final int page, final String sortBy, final String order, final String filter) {
        Promise<Page<Computer>> promise = Akka.asPromise(Futures
                .future(listCallable(page, sortBy, order, filter), dbEc));
        return async(promise.map(new Function<Page<Computer>, Result>() {
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

    /**
     * Filters computers and returns the html to update encapsulated in json.
     * 
     * @return json with an object named "htmlBySelector" that contains selector -&gt; html mappings
     */
    public static Result liveSearch(int page, String sortBy, String order, String filter) {
        final Page<Computer> items = Computer.page(page, 10, sortBy, order, filter);
        // @formatter:off
        final Map<String, String> htmlBySelector = ImmutableMap.of(
                "#homeTitle.text", Messages.get("list.content.title", items.getTotalRowCount()),
                "#content", views.html.listComponent.render(items, sortBy, order, filter).body());
        // @formatter:on
        return ok(JsonUtil.successMessageWithHtml(htmlBySelector));
    }

    @Security.Authenticated(Secured.class)
    public static Result create() {
        return ok(views.html.createForm.render(form(Computer.class)));
    }

    /**
     * Handle the 'new computer form' submission
     */
    @Security.Authenticated(Secured.class)
    public static Result save() {
        Form<Computer> computerForm = form(Computer.class).bindFromRequest();
        if (computerForm.hasErrors()) {
            return badRequest(views.html.createForm.render(computerForm));
        }
        computerForm.get().save();
        flash("success", "Computer " + computerForm.get().name + " has been created");
        return GO_HOME;
    }

    public static Result edit(Long id) {
        return TODO;
    }

    public static Result update(Long id) {
        return TODO;
    }

    public static Result delete(Long id) {
        return TODO;
    }

    // -- Javascript routing

    @Cached(key = "jsroutes", duration = 3600)
    public static Result javascriptRoutes() {
        return ok(Routes.javascriptRouter("jsRoutes", controllers.routes.javascript.Application.liveSearch())).as(
                "text/javascript");
    }

    // -- Authentication

    public static class Login {

        public String email;
        public String password;

        public String validate() {
            if (!"play@example.com".equals(email) || !"secret".equals(password)) {
                return "Invalid user or password";
            }
            return null;
        }

    }

    /**
     * Login page.
     */
    public static Result login() {
        return ok(views.html.login.render(form(Login.class)));
    }

    /**
     * Handle login form submission.
     */
    public static Result authenticate() {
        Form<Login> loginForm = form(Login.class).bindFromRequest();
        if (loginForm.hasErrors()) {
            return badRequest(views.html.login.render(loginForm));
        } else {
            session("email", loginForm.get().email);
            flash("success", "You've been logged in");
            return Secured.getOrigUrl() != null ? redirect(Secured.getOrigUrl()) : GO_HOME;
        }
    }

    /**
     * Logout and clean the session.
     */
    public static Result logout() {
        session().clear();
        flash("success", "You've been logged out");
        return GO_HOME;
    }

}
