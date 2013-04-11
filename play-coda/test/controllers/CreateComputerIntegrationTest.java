/**
 * 
 */
package controllers;

import static org.fest.assertions.Assertions.assertThat;
import static play.test.Helpers.fakeApplication;
import static play.test.Helpers.inMemoryDatabase;

import java.util.Date;

import models.Company;
import models.Computer;

import org.fluentlenium.adapter.FluentTest;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;

import play.test.FakeApplication;
import play.test.TestServer;

/**
 * @author Martin Grotzke
 * 
 */
public class CreateComputerIntegrationTest extends FluentTest {

    public static final int PORT = 3333;
    public static FakeApplication app;
    private static TestServer server;
    private String name;
    private CreateComputerPage page;

    @BeforeClass
    public static void startApp() {
        app = fakeApplication(inMemoryDatabase());
        server = new TestServer(PORT, app);
        server.start();
    }

    @Before
    public void setup() {
        // Guard assertion
        name = "the first one";
        assertThat(Computer.find.where().eq("name", name).findRowCount()).isEqualTo(0);

        // Common arrangement, login and go to create computer page
        login();

        page = createPage(CreateComputerPage.class);
        page.go();
        page.isAt();
    }

    private void login() {
        LoginPage loginPage = createPage(LoginPage.class);
        loginPage.go();
        loginPage.fillEmail("play@example.com");
        loginPage.fillPassword("secret");
        loginPage.submit();
    }

    @AfterClass
    public static void stopApp() {
        server.stop();
    }

    @Override
    public WebDriver getDefaultDriver() {
        return new HtmlUnitDriver();
    }

    @Test
    public void createComputer() throws Exception {
        // Arrange
        page.fillName(name);
        page.fillIntroduced(new Date());
        final Company company = Company.find.setMaxRows(1).findUnique();
        page.selectCompany(company.id);

        // Act
        page.submit();

        // Assert
        final ListComputersPage resultPage = createPage(ListComputersPage.class);
        resultPage.isAt();
        resultPage.displaysMessage(name);

        final Computer computer = Computer.find.where().eq("name", name).findUnique();
        assertThat(computer).isNotNull();
        assertThat(computer.company.id).isEqualTo(company.id);
    }

}
