package controllers;

import static org.fest.assertions.Assertions.assertThat;

/**
 * Test object for the login page.
 */
public class LoginPage extends ExtFluentPage {

    @Override
    public String getUrl() {
        return urlFor(routes.Application.login());
    }

    @Override
    public void isAt() {
        assertThat(title()).contains("Login");
    }

    public void fillEmail(String email) {
        fill("#email", email);
    }

    public void fillPassword(String password) {
        fill("#password", password);
    }
}
