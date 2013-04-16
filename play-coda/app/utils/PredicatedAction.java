package utils;

/**
 * A predicated action is one where a condition must be satisfied in order to proceed with the request. If the
 * condition is not satisfied then a supplied status result is yielded.
 */

import java.util.concurrent.Callable;

import play.mvc.Action;
import play.mvc.Http;
import play.mvc.Result;

public class PredicatedAction extends Action<Predicated> {
    @Override
    public Result call(final Http.Context ctx) throws Throwable {
        final Callable<Boolean> p = configuration.predicate().newInstance();
        if (p.call()) {
            return delegate.call(ctx);
        } else {
            return status(configuration.failed());
        }
    }
}