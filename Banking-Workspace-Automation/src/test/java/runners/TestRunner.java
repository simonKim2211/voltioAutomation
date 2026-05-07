package runners;

import org.junit.runner.RunWith;
import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;

@RunWith(Cucumber.class)
@CucumberOptions(
        features = "src/test/resources/features/F2_registration.feature",
        glue = {"stepdefinitions"},
        plugin = {
                "pretty",
                "html:target/cucumber-reports.html"
        },
        monochrome = true,
        publish = true
)
public class TestRunner {
}
