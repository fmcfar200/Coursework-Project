/*
    Fraser McFarlane S1434566
*/

package mpdproject.gcu.frasermcfarlane.org.courseworkassignment;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static junit.framework.Assert.assertEquals;
import static org.hamcrest.Matchers.anything;
import static org.hamcrest.Matchers.hasToString;


@RunWith(AndroidJUnit4.class)
public class HomeActivityTest
{
    @Rule
    public ActivityTestRule<HomeActivity> homeTestRule = new ActivityTestRule<>(HomeActivity.class);
    public ActivityTestRule<ItemViewer> itemViewTestRule = new ActivityTestRule<>(ItemViewer.class);

//HOME ACTIVITY TESTS
    @Test
    public void button1IntentCheck() throws Exception
    {
        onView(withId(R.id.incidentsButton)).perform(click());
    }
    @Test
    public void button2IntentCheck() throws Exception
    {
        onView(withId(R.id.plannedRWButton)).perform(click());
    }

//ITEM VIEWER TESTS
    @Test
    public void currentIncidentsListClickTest() throws Exception
    {
        onView(withId(R.id.incidentsButton)).perform(click());

        onData(anything())
                .inAdapterView(withId(R.id.listView)).atPosition(0)
                .perform(click());

    }
    @Test
    public void plannedRWListClickTest() throws Exception
    {
        onView(withId(R.id.plannedRWButton)).perform(click());
        onData(anything())
                .inAdapterView(withId(R.id.listView)).atPosition(5)
                .perform(click());

        //onView(withId(R.id.incidentsButton)).perform(click());
    }

    @Test
    public void searchTest() throws Exception
    {
        onView(withId(R.id.plannedRWButton)).perform(click());

        onView(withId(R.id.calendarIcon)).perform(click());
        onView(withText("OK")).perform(click());

        onData(anything())
                .inAdapterView(withId(R.id.listView)).atPosition(0)
                .perform(click());

    }


    //INFORMATION ACTIVITY CHECK
    @Test
    public void InfoActivityTextCheck() throws Exception
    {
        onView(withId(R.id.incidentsButton)).perform(click());

        onData(anything())
                .inAdapterView(withId(R.id.listView)).atPosition(0)
                .perform(click());

        String theText = "A77 A714 Girvan(S)- B7044 Ballantrae - Closure";
        onView(withId(R.id.titleID)).check(matches(withText(theText)));
    }




}
