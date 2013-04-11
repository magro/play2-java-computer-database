package controllers;

import static org.fest.assertions.Assertions.assertThat;

import java.util.Date;

/**
 * Test object for the add computer page.
 */
public class CreateComputerPage extends ExtFluentPage {

    @Override
    public String getUrl() {
        return urlFor(routes.Application.create());
    }

    @Override
    public void isAt() {
        assertThat(title()).contains("Add Computer");
    }

    public void displaysMessage(String name) {
        assertThat(find(".alert-message").contains(name));
    }

    public void fillName(String name) {
        fill("#name", name);
    }

    public void fillIntroduced(Date introduced) {
        fill("#introduced", introduced);
    }

    public void fillDiscontinued(Date discontinued) {
        fill("#discontinued", discontinued);
    }

    public void selectCompany(Long companyId) {
        selectOption("company.id", companyId.toString());
    }
}
