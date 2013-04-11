package controllers;

import static play.data.Form.form;
import models.Computer;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security;

public class Application extends Controller {

    /**
     * This result directly redirect to application home.
     */
    public static Result GO_HOME = redirect(routes.Application.list(0, "name", "asc", ""));

    public static Result index() {
        return ok(views.html.index.render("Your new application is ready."));
    }

    /**
     * Display the list of computers.
     */
    public static Result list(int page, String sortBy, String order, String filter) {
        return ok(views.html.list.render(Computer.page(page, 10, sortBy, order, filter), sortBy, order, filter));
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
