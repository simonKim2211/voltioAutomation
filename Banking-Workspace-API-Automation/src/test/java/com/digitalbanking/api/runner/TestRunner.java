package com.digitalbanking.api.runner;

import org.junit.runner.RunWith;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;

@RunWith(Cucumber.class)
@CucumberOptions(
        features = "src/test/resources/features/Sprint1/",
        glue = {
                "com.digitalbanking.api.stepdefs",
                "com.digitalbanking.api.hooks"
        },
        tags = "@F1_TC_001",
        plugin = {
                "pretty",
                "html:target/cucumber-reports/cucumber.html",
                "json:target/cucumber-reports/cucumber.json"
        },
        publish = true
)
public class TestRunner {
}