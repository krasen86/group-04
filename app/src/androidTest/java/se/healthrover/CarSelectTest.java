package se.healthrover;

import android.content.Context;
import android.view.View;

import androidx.test.espresso.intent.Intents;
import androidx.test.espresso.matcher.RootMatchers;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;

import se.healthrover.entities.HealthRoverCar;
import se.healthrover.stub_web_service.MockWebService;
import se.healthrover.ui_activity_controller.CarSelect;
import se.healthrover.ui_activity_controller.ManualControl;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.ComponentNameMatchers.hasClassName;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.anything;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class CarSelectTest  {

    private CarSelect carSelect;
    private static HealthRoverCar testHealthRover;

    @Rule
    public ActivityTestRule<CarSelect> carSelectActivityTestRule = new ActivityTestRule<CarSelect>(CarSelect.class);

    @BeforeClass
    public static void setCarSelect(){
        testHealthRover = HealthRoverCar.HEALTH_ROVER_CAR1;
    }

    @Before
    public void setUp() {

        carSelect = carSelectActivityTestRule.getActivity();
        carSelect.setHealthRoverWebService(new MockWebService());
        Intents.init();
    }
    @Test
    public void useAppContext() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        assertEquals("se.healthrover", appContext.getPackageName());
    }

    /* Test Case 1
     *   verify that all the elements are loaded by using IDs*/
    @Test
    public void testCase_1_verifyIfElementsAreLoadedTest(){
        //Buttons
        View view = carSelect.findViewById(R.id.infoButton);
        assertNotNull(view);
        view = carSelect.findViewById(R.id.connectToCarButton);
        assertNotNull(view);
        //SmartCar list
        view = carSelect.findViewById(R.id.smartCarList);
        assertNotNull(view);
        //Text field
        view = carSelect.findViewById(R.id.chooseCarText);
        assertNotNull(view);
    }
    /* Test Case 2
     *   Locate the SmartCar list
     *   Click an existing row in the SmartCar list
     *   Locate the Connect button by ID,
     *   click on the Connect button,
     *   verify that the correct activity is loaded
     *   verify that the correct car name has been passed to the activity*/
    @Test
    public void testCase_2_switchToManualControlTest() {
        onData(anything()).inAdapterView(withId(R.id.smartCarList)).atPosition(0).perform(click());
        onView(withId(R.id.connectToCarButton)).perform(click());
        intended(hasComponent(hasClassName(ManualControl.class.getName())));
        onView(withId(R.id.manualControlHeaderText)).check(matches(isDisplayed()));
        onView(withId(R.id.manualControlHeaderText)).check(matches(withText(testHealthRover.getCarName())));
    }
    /* Test Case 3
     *   check that the activity is loaded,
     *   click the info button,
     *   verify that the info popup is loaded,
     *   verify the text in the popup.
    @Test
    public void testCase_3_infoButtonDisplaysInfoPopup(){
        assertNotNull(carSelect);
        onView(withId(R.id.infoButton)).perform(click());
        onView(withId(R.id.infoPopup)).inRoot(RootMatchers.isPlatformPopup()).check(matches(isDisplayed()));
        onView(withText(R.string.info_text)).check(matches(isDisplayed()));
    }
    Commented in order to see if this causes the CI build to fail
*/
    @After
    public void tearDown(){
        carSelect = null;
        Intents.release();
    }
}