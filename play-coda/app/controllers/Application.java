package controllers;

import play.mvc.Controller;
import play.mvc.Result;
import views.html.index;

public class Application extends Controller {

    public static Result index() {
        return ok(index.render("Your cool application is ready."));
    }

    /**
     * Display the paginated list of computers.
     * 
     * @param page
     *            Current page number (starts from 0)
     * @param sortBy
     *            Column to be sorted
     * @param order
     *            Sort order (either asc or desc)
     * @param filter
     *            Filter applied on computer names
     */
    public static Result list(int page, String sortBy, String order, String filter) {
        return TODO;
    }

    public static Result create() {
        return TODO;
    }

    public static Result save() {
        return TODO;
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

}
