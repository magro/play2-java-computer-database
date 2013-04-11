package controllers;

import static controllers.CreateComputerIntegrationTest.PORT;
import static org.fest.assertions.Assertions.assertThat;

import java.util.Date;

import org.fluentlenium.core.FluentPage;
import org.fluentlenium.core.domain.FluentList;
import org.fluentlenium.core.domain.FluentWebElement;
import org.fluentlenium.core.filter.Filter;

/**
 * Extended {@link FluentPage} that adds some helper methods.
 */
public class ExtFluentPage extends FluentPage {

    protected String formatted(Date date) {
        return new java.text.SimpleDateFormat("yyyy-MM-dd").format(date);
    }

    protected void fill(String selector, String value) {
        final FluentWebElement input = findExisting(selector);
        input.text(value);
    }

    protected void fill(String selector, Date date) {
        fill(selector, formatted(date));
    }

    protected FluentWebElement findExisting(String selector, Filter... filters) {
        final FluentWebElement element = findFirst(selector, filters);
        assertThat(element).as(selector).isNotNull();
        return element;
    }

    public void selectOption(final String selectName, final String optionValue) {
        final String elementSelector = selectByName(selectName);
        final String optionSelect = elementSelector + " option";
        final FluentList<FluentWebElement> options = find(optionSelect, new Filter("value", optionValue));
        options.first().click();
    }

    protected String selectByName(final String name) {
        return "*[name='" + name + "']";
    }

    public void submit() {
        findExisting("input[type='submit']").click();
    }

    public static String urlFor(final play.api.mvc.Call call) {
        return urlFor(call.url());
    }

    public static String urlFor(final play.mvc.Call call) {
        return urlFor(call.url());
    }

    public static String urlFor(final String uriString) {
        return "http://localhost:" + PORT + uriString;
    }

}
