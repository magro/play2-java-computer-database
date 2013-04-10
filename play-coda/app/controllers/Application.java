package controllers;

import static play.data.Form.form;
import models.Computer;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;

public class Application extends Controller {

    public static Result index() {
        return ok(views.html.index.render("Your new application is ready."));
    }

    /**
     * Display the list of computers.
     */
    public static Result list(int page, String sortBy, String order, String filter) {
        return ok(views.html.list.render(
                Computer.page(page, 10, sortBy, order, filter),
                sortBy, order, filter));
    }

    public static Result create() {
        return ok(views.html.createForm.render(form(Computer.class)));
    }

    /**
     * Handle the 'new computer form' submission
     */
    public static Result save() {
        Form<Computer> computerForm = form(Computer.class).bindFromRequest();
        if (computerForm.hasErrors()) {
            return badRequest(views.html.createForm.render(computerForm));
        }
        computerForm.get().save();
        flash("success", "Computer " + computerForm.get().name + " has been created");
        return redirect(routes.Application.list(0, "name", "asc", ""));
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
