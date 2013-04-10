package controllers;

import static org.fest.assertions.Assertions.assertThat;

/**
 * Test object for the list computers page.
 */
public class ListComputersPage extends ExtFluentPage {

    @Override
    public String getUrl() {
        return urlFor(routes.Application.list(0, "name", "asc", ""));
    }

    @Override
    public void isAt() {
        assertThat(title()).contains("List Computers");
    }

    public void displaysMessage(String name) {
        assertThat(find(".alert-message").contains(name));
    }

}
