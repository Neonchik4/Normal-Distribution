package com.example.normal_distribution

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.hamcrest.text.MatchesPattern.matchesPattern
import org.hamcrest.Matchers.*
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MainActivityInstrumentedTest {

    @get:Rule
    val activityRule = ActivityScenarioRule(MainActivity::class.java)

    @Test
    fun testLogNormalGeneration_PositiveOutput() {
        onView(withId(R.id.mean_val)).perform(typeText("1.0"), closeSoftKeyboard())
        onView(withId(R.id.variance_value)).perform(typeText("0.25"), closeSoftKeyboard())
        onView(withId(R.id.get_random_num)).perform(click())
        onView(withId(R.id.random_number_result)).check(matches(allOf(
            isDisplayed(),
            not(withText("Ошибка ввода")),
            withText(matchesPattern("^\\d*\\.?\\d+\$")) // Числовой формат
        )))
    }

    @Test
    fun testLogNormalGeneration_WithZeroVariance() {
        onView(withId(R.id.mean_val)).perform(replaceText("2.0"), closeSoftKeyboard())
        onView(withId(R.id.variance_value)).perform(replaceText("0.0"), closeSoftKeyboard())
        onView(withId(R.id.get_random_num)).perform(click())
        onView(withId(R.id.random_number_result)).check(matches(withText("7.4"))) // exp(2.0) ≈ 7.389 → округляется до 7.4
    }

    @Test
    fun testLogNormalGeneration_MultipleValuesDifferentOutput() {
        onView(withId(R.id.mean_val)).perform(replaceText("0.0"), closeSoftKeyboard())
        onView(withId(R.id.variance_value)).perform(replaceText("1.0"), closeSoftKeyboard())
        onView(withId(R.id.get_random_num)).perform(click())
        var firstValue: String? = null
        onView(withId(R.id.random_number_result)).check { view, _ ->
            firstValue = (view as? android.widget.TextView)?.text.toString()
        }

        // Снова нажимаем, чтобы сравнить результат
        Thread.sleep(500) // Небольшая задержка перед генерацией нового числа
        onView(withId(R.id.get_random_num)).perform(click())
        onView(withId(R.id.random_number_result)).check(matches(not(withText(firstValue))))
    }

    @Test
    fun testInvalidInput_ShowsError() {
        onView(withId(R.id.mean_val)).perform(replaceText("abc"), closeSoftKeyboard())
        onView(withId(R.id.variance_value)).perform(replaceText("0.1"), closeSoftKeyboard())
        onView(withId(R.id.get_random_num)).perform(click())
        onView(withId(R.id.random_number_result)).check(matches(withText("Ошибка ввода")))
    }

    @Test
    fun testNegativeVariance_ShowsError() {
        onView(withId(R.id.mean_val)).perform(replaceText("1.0"), closeSoftKeyboard())
        onView(withId(R.id.variance_value)).perform(replaceText("-1.0"), closeSoftKeyboard())
        onView(withId(R.id.get_random_num)).perform(click())
        onView(withId(R.id.random_number_result)).check(matches(withText("Ошибка ввода")))
    }

    @Test
    fun testLargeMeanAndVariance() {
        onView(withId(R.id.mean_val)).perform(replaceText("10.0"), closeSoftKeyboard())
        onView(withId(R.id.variance_value)).perform(replaceText("4.0"), closeSoftKeyboard())
        onView(withId(R.id.get_random_num)).perform(click())
        onView(withId(R.id.random_number_result)).check(matches(allOf(
            isDisplayed(),
            not(withText("Ошибка ввода")),
            withText(matchesPattern("^\\d*\\.?\\d+\$"))
        )))
    }
}
