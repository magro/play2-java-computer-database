package controllers;

import static play.data.Form.form;
import models.Computer;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.index;

public class Application extends Controller {

    public static Result index() {
        return ok(index.render("Your new application is ready."));
    }

    /**
     * Display the list of computers.
     */
    public static Result list() {
        return TODO;
    }

    public static Result create() {
        return ok(views.html.createForm.render(form(Computer.class)));
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
