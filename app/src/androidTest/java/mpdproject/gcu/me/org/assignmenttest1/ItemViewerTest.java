package mpdproject.gcu.me.org.assignmenttest1;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;


import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.Matchers.hasToString;
import static org.hamcrest.Matchers.startsWith;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Created by Fraser on 25/03/2018.
 */

@RunWith(AndroidJUnit4.class)
public class ItemViewerTest
{
    @Rule
    public ActivityTestRule<ItemViewer> itemViewerTestRule = new ActivityTestRule<>(ItemViewer.class);

    @Test
    public void testItemClick()
    {

    }
}
