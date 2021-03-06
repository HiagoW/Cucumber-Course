package io.cucumber.shouty;

import io.cucumber.java.Before;
import io.cucumber.java.DataTableType;
import io.cucumber.java.Transpose;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.cucumber.datatable.DataTable;

import java.util.*;

import java.util.HashMap;

import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.core.IsCollectionContaining.hasItem;
import static org.hamcrest.MatcherAssert.assertThat;
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
    private Network network = new Network(DEFAULT_RANGE);
    private Map<String, Person> people;
    private Map<String, List<String>> messagesShoutedBy;


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
        messagesShoutedBy = new HashMap<String, List<String>>();
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
        for (Whereabouts whereabout : whereabouts ) {
            people.put(whereabout.name, new Person(network, whereabout.location));
        }
    }

    @Given("Sean has bought {int} credits")
    public void sean_has_bought_credits(int credits) {
        people.get("Sean").setCredits(credits);
    }

    @When("Sean shouts")
    public void sean_shouts() throws Throwable {
        shout("Hello, world");
    }

    @When("Sean shouts {string}")
    public void sean_shouts_message(String message) throws Throwable {
        shout(message);
    }

    @When("Sean shouts {int} messages containing the word {string}")
    public void sean_shouts_messages_containing_the_word(int count, String word) throws Throwable {
        String message = "a message containing the word " + word;
        for (int i = 0; i < count; i++) {
            shout(message);
        }
    }

    @When("Sean shouts the following message")
    public void sean_shouts_the_following_message(String message) throws Throwable {
        shout(message);
    }

    @When("Sean shouts a message")
    public void sean_shouts_a_message() throws Throwable {
        shout("here is a message");
    }

    @When("Sean shouts a long message")
    public void sean_shouts_a_long_message() throws Throwable {
        String longMessage = String.join(
                "\n",
                "A message from Sean",
                "that spans multiple lines");
        shout(longMessage);
    }

    @When("Sean shouts {int} over-long messages")
    public void sean_shouts_some_over_long_messages(int count) throws Throwable {
        String baseMessage = "A message from Sean that is 181 characters long ";
        String padding = "x";
        String overlongMessage = baseMessage + padding.repeat(181 - baseMessage.length());

        for (int i = 0; i < count; i++) {
            shout(overlongMessage);
        }
    }

    private void shout(String message) {
        people.get("Sean").shout(message);
        List<String> messages = messagesShoutedBy.get("Sean");
        if (messages == null) {
            messages = new ArrayList<String>();
            messagesShoutedBy.put("Sean", messages);
        }
        messages.add(message);
    }

    @Then("Lucy should hear Sean's message")
    public void lucy_hears_Sean_s_message() throws Throwable {
        List<String> messages = messagesShoutedBy.get("Sean");
        assertEquals(messages, people.get("Lucy").getMessagesHeard());
    }

    @Then("Lucy should hear a shout")
    public void lucy_should_hear_a_shout() throws Throwable {
        assertEquals(1, people.get("Lucy").getMessagesHeard().size());
    }

    @Then("{word} should not hear a shout")
    public void person_should_not_hear_a_shout(String name) throws Throwable {
        assertEquals(0, people.get(name).getMessagesHeard().size());
    }

    @Then("Lucy hears the following messages:")
    public void lucy_hears_the_following_messages(DataTable expectedMessages) {
        List<List<String>> actualMessages = new ArrayList<List<String>>();
        List<String> heard = people.get("Lucy").getMessagesHeard();
        for (String message : heard) {
            actualMessages.add(Collections.singletonList(message));
        }
        expectedMessages.diff(DataTable.create(actualMessages));
    }

    @Then("Lucy hears all Sean's messages")
    public void lucy_hears_all_Sean_s_messages() throws Throwable {
        List<String> heardByLucy = people.get("Lucy").getMessagesHeard();
        List<String> messagesFromSean = messagesShoutedBy.get("Sean");

        // Hamcrest's hasItems matcher wants an Array, not a List.
        String[] messagesFromSeanArray = messagesFromSean.toArray(new String[messagesFromSean.size()]);
        assertThat(heardByLucy, hasItems(messagesFromSeanArray));
    }

    @Then("Sean should have {int} credits")
    public void sean_should_have_credits(int expectedCredits) {
        assertEquals(expectedCredits, people.get("Sean").getCredits());
    }
}