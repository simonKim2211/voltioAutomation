package stepdefinitions;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import utils.DriverUtilities;


public class Hooks {

    @Before
    public void setUp() {
        DriverUtilities.getInstance().getDriver();
    }

    @After
    public void tearDown() {
        DriverUtilities.getInstance().quitDriver();
    }
}
