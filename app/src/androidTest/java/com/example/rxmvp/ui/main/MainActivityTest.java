package com.example.rxmvp.ui.main;

import android.support.test.espresso.ViewInteraction;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import com.example.rxmvp.R;
import com.example.rxmvp.helpers.TestStateManager;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.swipeDown;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static com.example.rxmvp.helpers.AssertCurrentActivity.assertCurrentActivity;

@RunWith(AndroidJUnit4.class) public class MainActivityTest {

  private TestStateManager testStateManager = new TestStateManager();

  @Rule public ActivityTestRule<MainActivity> activityTestRule =
      new ActivityTestRule<>(MainActivity.class, true, false);

  @Before public void setUp() throws Exception {
    testStateManager.login();
    testStateManager.create();
    activityTestRule.launchActivity(null);
  }

  @After public void tearDown() throws Exception {
    testStateManager.clean();
  }

  @Test public void activityStarted() throws Exception {
    activityTestRule.getActivity();
    assertCurrentActivity(MainActivity.class);
  }

  @Test public void notificationsCounter() throws Exception {
    ViewInteraction onNotificationsCounter = onView(withId(R.id.main_notifications_counter));

    onNotificationsCounter.check(matches(withText("3")));
    refreshNotifications();
    onNotificationsCounter.check(matches(withText("0")));
  }

  private void refreshNotifications() {
    onView(withId(R.id.notifications_refresh)).perform(swipeDown());
  }
}
