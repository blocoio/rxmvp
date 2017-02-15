package com.example.rxmvp.ui.notifications;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import com.example.rxmvp.helpers.TestStateManager;
import com.example.rxmvp.ui.main.MainActivity;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class) public class NotificationsViewTest {

  private static final String[] NOTIFICATIONS = { "P0055", "P0503", "B1236" };

  @Rule public ActivityTestRule<MainActivity> activityTestRule =
      new ActivityTestRule<>(MainActivity.class, true, false);

  private TestStateManager testStateManager = new TestStateManager();

  @Before public void setUp() throws Exception {
    testStateManager.login();
    testStateManager.create();
    activityTestRule.launchActivity(null);
  }

  @After public void tearDown() throws Exception {
    testStateManager.clean();
  }

  @Test public void troubles() throws Exception {
    for (String notification : NOTIFICATIONS) {
      onView(withText(notification)).check(matches(isDisplayed()));
    }
  }
}
