package ru.netology.card;

import com.codeborne.selenide.Condition;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.openqa.selenium.Keys;

import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;

public class CardDeliveryTest {
    @BeforeEach
    void setUp() {
        open("http://localhost:9999");
    }

    public String generateDate(long addDays, String pattern) {
        return LocalDate.now().plusDays(addDays).format(DateTimeFormatter.ofPattern(pattern));
    }

    public void setMeetingInCalendarTool() {
        String planningDay = generateDate(7, "d");
        if (!generateDate(7, "MM").equals(generateDate(0, "MM"))) {
            $$(".calendar__arrow_direction_right").findBy(attribute("data-step", "1")).click();
            $$(".popup .calendar__layout .calendar__day").find(exactText(planningDay)).click();
        } else {
            $$(".popup .calendar__layout .calendar__day").find(exactText(planningDay)).click();
        }
    }

    @Test
    void shouldSubmitForm() {
        String planningDate = generateDate(3, "dd.MM.yyyy");
        $("[data-test-id=city] input").setValue("Воронеж");
        $("[data-test-id=date] input").sendKeys(Keys.chord(Keys.CONTROL, "a"), Keys.DELETE);
        $("[data-test-id=date] input").setValue(planningDate);
        $("[data-test-id=name] input").setValue("Иванов Иван-Алексей");
        $("[data-test-id=phone] input").setValue("+71234567890");
        $("[data-test-id=agreement]").click();
        $$("button").find(exactText("Забронировать")).click();
        $("[data-test-id=notification]").shouldBe(visible, Duration.ofSeconds(15));
        $(".notification__content").shouldHave(Condition.text("Встреча успешно забронирована на " + planningDate), Duration.ofSeconds(15))
                .shouldBe(Condition.visible);
    }

    @Test
    void shouldSubmitFormWithClickOnTools() {
        String planningDate = generateDate(7, "dd.MM.yyyy");
        $("[data-test-id=city] input").setValue("Во");
        $$(".popup .menu-item__control").find(exactText("Воронеж")).click();
        $("[data-test-id=date] .icon").click();
        setMeetingInCalendarTool();
        $("[data-test-id=name] input").setValue("Иванов Иван-Алексей");
        $("[data-test-id=phone] input").setValue("+71234567890");
        $("[data-test-id=agreement]").click();
        $$("button").find(exactText("Забронировать")).click();
        $("[data-test-id=notification]").shouldBe(visible, Duration.ofSeconds(15));
        $(".notification__content").shouldHave(Condition.text("Встреча успешно забронирована на " + planningDate), Duration.ofSeconds(15))
                .shouldBe(Condition.visible);
    }
}
