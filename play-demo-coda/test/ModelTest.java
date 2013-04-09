import static org.fest.assertions.Assertions.assertThat;
import static play.test.Helpers.fakeApplication;
import static play.test.Helpers.start;
import static play.test.Helpers.stop;

import java.util.Date;

import models.Company;
import models.Computer;

import org.junit.Before;
import org.junit.Test;

import play.test.FakeApplication;

import com.avaje.ebean.Page;

public class ModelTest {
    
    private FakeApplication app;

    private String formatted(Date date) {
        return new java.text.SimpleDateFormat("yyyy-MM-dd").format(date);
    }
    
    @Before
    public void setup() {
        app = fakeApplication();
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

    @Test
    public void findById() {
        Computer macintosh = Computer.find.byId(21l);
        assertThat(macintosh.name).isEqualTo("Macintosh");
        assertThat(formatted(macintosh.introduced)).isEqualTo("1984-01-24");
    }
    
    @Test
    public void pagination() {
        Page<Computer> computers = Computer.page(1, 20, "name", "ASC", "");
        assertThat(computers.getTotalRowCount()).isEqualTo(574);
        assertThat(computers.getList().size()).isEqualTo(20);
    }
    
}
