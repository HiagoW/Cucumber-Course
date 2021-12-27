package io.cucumber.shouty;

import org.junit.platform.suite.api.ConfigurationParameter;
import org.junit.platform.suite.api.IncludeEngines;
import org.junit.platform.suite.api.SelectClasspathResource;
import org.junit.platform.suite.api.Suite;

import static io.cucumber.junit.platform.engine.Constants.*;

@Suite
@IncludeEngines("cucumber")
@SelectClasspathResource("io/cucumber/shouty")
// Disable pretty to generate html report, was not working when I runned it
@ConfigurationParameter(key = PLUGIN_PROPERTY_NAME, value = "pretty")
// Need to set correct package to work
@ConfigurationParameter(key = GLUE_PROPERTY_NAME, value = "io.cucumber.shouty")
//@ConfigurationParameter(key = FILTER_TAGS_PROPERTY_NAME, value="@focus")
//@ConfigurationParameter(key = FILTER_TAGS_PROPERTY_NAME, value="not @slow")
public class RunCucumberTest {
}
