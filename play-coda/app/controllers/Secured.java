package controllers;

import play.mvc.Http.Context;
import play.mvc.Result;
import play.mvc.Security;

public class Secured extends Security.Authenticator {

    private static final String ORIG_URL = "sec.requrl";

    @Override
    public String getUsername(Context ctx) {
        return doGetUsername(ctx);
    }

    private static String doGetUsername(Context ctx) {
        return ctx.session().get("email");
    }

    @Override
    public Result onUnauthorized(Context ctx) {
        ctx.session().put(ORIG_URL, ctx.request().uri());
        return redirect(routes.Application.login());
    }

    public static String getOrigUrl() {
        return Context.current().session().get(ORIG_URL);
    }

    public static String getUsername() {
        return doGetUsername(Context.current());
    }

}