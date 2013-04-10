import static org.fest.assertions.Assertions.assertThat;
import static play.test.Helpers.fakeApplication;
import static play.test.Helpers.inMemoryDatabase;
import static play.test.Helpers.start;
import static play.test.Helpers.stop;

import java.util.Date;

import models.Company;
import models.Computer;

import org.junit.Before;
import org.junit.Test;

import play.test.FakeApplication;

public class ModelTest {

    private FakeApplication app;

    @Before
    public void setup() {
        app = fakeApplication(inMemoryDatabase());
        start(app);
    }

    public void teardown() {
        stop(app);
    }

    @Test
    public void createAndLoad() {
        final Company company = Company.find.all().get(0);
        Computer computer = new Computer("c64", new Date(), company);
        computer.save();
        assertThat(computer.id).isNotNull();
        Computer c64 = Computer.find.byId(computer.id);
        assertThat(c64.name).isEqualTo(computer.name);
    }

}
