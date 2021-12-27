package io.cucumber.shouty;

import io.cucumber.datatable.DataTable;
import io.cucumber.java.Before;
import io.cucumber.java.DataTableType;
import io.cucumber.java.Transpose;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class StepDefinitions {

    // Example of custom parameter
    // / -> One or other word
    // () -> is optional
    // person is defined in ParameterTypes.java
    // Person class should have a constructor that receives only the name
    // Ex: Lucy is located 1 meter from Sean -> Two person objects with names Lucy and Sean
//    @Given("{person} is located/standing {int} meter(s) from {person}")
//    public void lucy_is_meters_from_sean(Person lucy, Integer distance, Person sean) {}

    private static final int DEFAULT_RANGE = 100;
    private String messageFromSean;
    private Network network = new Network(DEFAULT_RANGE);
    private Map<String, Person> people;

    static class Whereabouts {
        public String name;
        public Integer location;

        public Whereabouts(String name, int location) {
            this.name = name;
            this.location = location;
        }
    }

    @DataTableType
    public Whereabouts defineWhereabouts(Map<String, String> entry) {
        return new Whereabouts(entry.get("name"), Integer.parseInt(entry.get("location")));
    }

    @Before
    public void createNetwork() {
        people = new HashMap<String, Person>();
    }

    @Given("the range is {int}")
    public void the_range_is(int range) throws Throwable {
        network = new Network(range);
    }

    @Given("a person named {word}")
    public void a_person_named(String name) throws Throwable {
        people.put(name, new Person(network, 0));
    }

    @Given("people are located at")
    public void people_are_located_at(@Transpose List<Whereabouts> whereabouts) {
        for (Whereabouts whereabout : whereabouts) {
            people.put(whereabout.name, new Person(network, whereabout.location));
        }
    }

    @When("Sean shouts")
    public void sean_shouts() throws Throwable {
        people.get("Sean").shout("Hello, world");
    }

    @When("Sean shouts {string}")
    public void sean_shouts_message(String message) throws Throwable {
        people.get("Sean").shout(message);
        messageFromSean = message;
    }

    @When("Sean shouts the following message")
    public void sean_shouts_the_following_message(String message) throws Throwable {
        people.get("Sean").shout(message);
        messageFromSean = message;
        System.out.println(message);
    }

    @Then("Lucy should hear Sean's message")
    public void lucy_hears_Sean_s_message() throws Throwable {
        assertEquals(Collections.singletonList(messageFromSean), people.get("Lucy").getMessagesHeard());
    }

    @Then("Lucy should hear a shout")
    public void lucy_should_hear_a_shout() throws Throwable {
        assertEquals(1, people.get("Lucy").getMessagesHeard().size());
    }

    @Then("{word} should not hear a shout")
    public void larry_should_not_hear_a_shout(String name) throws Throwable {
        assertEquals(0, people.get(name).getMessagesHeard().size());
    }

    @Then("Lucy hears the following messages:")
    public void lucy_hears_the_following_messages(io.cucumber.datatable.DataTable expectedMessages) {
        List<List<String>> actualMessages = new ArrayList<List<String>>();
        List<String> heard = people.get("Lucy").getMessagesHeard();
        for (String message: heard) {
            actualMessages.add(Collections.singletonList(message));
        }
        expectedMessages.diff(DataTable.create(actualMessages));
    }
}
