package com.digitalbanking.api.hooks;

import com.digitalbanking.api.context.TestContext;
import io.cucumber.java.After;
import io.cucumber.java.Before;

public class Hooks {

    private final TestContext testContext;

    public Hooks(TestContext testContext) {
        this.testContext = testContext;
    }

    @Before
    public void beforeScenario() {
        testContext.clear();
    }

    @After
    public void afterScenario() {
        // Keep this empty for now.
        // Later we can add logging or cleanup if needed.
    }
}