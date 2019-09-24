package TestRunners;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
        glue = "stepDefinitions",
        features = {"src/test/resources/features/"},
        plugin = {"pretty","io.qameta.allure.cucumber4jvm.AllureCucumber4Jvm", "json:target/cucumber-reports/file.json"},
        tags = {"@testBrowser"}
)
public class TestCucumber2 {
}
