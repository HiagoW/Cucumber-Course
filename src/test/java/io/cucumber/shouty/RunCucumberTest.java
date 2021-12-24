package io.cucumber.shouty;

import org.junit.platform.suite.api.ConfigurationParameter;
import org.junit.platform.suite.api.IncludeEngines;
import org.junit.platform.suite.api.SelectClasspathResource;
import org.junit.platform.suite.api.Suite;

import static io.cucumber.junit.platform.engine.Constants.PLUGIN_PROPERTY_NAME;
import static io.cucumber.junit.platform.engine.Constants.GLUE_PROPERTY_NAME;

@Suite
@IncludeEngines("cucumber")
@SelectClasspathResource("io/cucumber/shouty")
@ConfigurationParameter(key = PLUGIN_PROPERTY_NAME, value = "pretty")
// Need to set correct package to work
@ConfigurationParameter(key = GLUE_PROPERTY_NAME, value = "io.cucumber.shouty")
public class RunCucumberTest {
}
