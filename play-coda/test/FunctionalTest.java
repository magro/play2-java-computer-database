import static org.fest.assertions.Assertions.assertThat;
import static play.mvc.Http.Status.OK;
import static play.test.Helpers.callAction;
import static play.test.Helpers.contentAsString;
import static play.test.Helpers.contentType;
import static play.test.Helpers.fakeApplication;
import static play.test.Helpers.fakeRequest;
import static play.test.Helpers.running;
import static play.test.Helpers.status;

import org.junit.Test;

import play.mvc.Result;

public class FunctionalTest {

    @Test
    public void createResult() {
        running(fakeApplication(), new Runnable() {
            @Override
            public void run() {
                Result result = callAction(controllers.routes.ref.Application.create(),
                        fakeRequest().withSession("email", "play@example.com"));
                assertThat(status(result)).isEqualTo(OK);
                assertThat(contentType(result)).isEqualTo("text/html");
                assertThat(contentAsString(result)).contains("Add a computer");
            }
        });
    }

}
