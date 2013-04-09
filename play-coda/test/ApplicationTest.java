import static org.fest.assertions.Assertions.assertThat;
import static play.data.Form.form;
import static play.test.Helpers.contentAsString;
import static play.test.Helpers.contentType;
import models.Computer;

import org.junit.Test;

import play.mvc.Content;

/**
 * 
 * Simple (JUnit) tests that can call all parts of a play app. If you are interested in mocking a whole application, see
 * the wiki for more details.
 * 
 */
public class ApplicationTest {

    @Test
    public void renderCreateFormTemplate() {
        Content html = views.html.createForm.render(form(Computer.class));
        assertThat(contentType(html)).isEqualTo("text/html");
        assertThat(contentAsString(html)).contains("<form");
    }

}
