import com.codeborne.selenide.CollectionCondition;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Keys;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Calendar;
import java.util.Date;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;

public class CardDeliveryTest {
    @BeforeEach
    void setUp() {
        open("http://localhost:9999");
    }

    public String setMeetingInFieldDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, 3);
        DateFormat df = new SimpleDateFormat("dd.MM.yyyy");
        String newDate = df.format(calendar.getTime());
        return newDate;
    }

    public void setMeetingInCalendarTool() {
        // вычисляем сегодняшнюю дату
        Calendar calendar = Calendar.getInstance();

        //записываем сегодняшнюю дату
        Date dateToday = calendar.getTime();
        System.out.println(dateToday);

        // меняем и записываем дату на неделю вперёд
        calendar.add(Calendar.DAY_OF_MONTH, 20);
        Date dateMeeting = calendar.getTime();
        System.out.println(dateMeeting);

        // получаем день в строковом значении, на который нужно переключить в календаре
        String dayMeeting = String.valueOf(calendar.get(Calendar.DAY_OF_MONTH));
        System.out.println(dayMeeting);

        // получаем месяц, на котором сейчас стоит календарь
        SimpleDateFormat formatter = new SimpleDateFormat("MMMM");
        String monthNow = formatter.format(dateToday);
        System.out.println(monthNow);

        // получаем месяц, на который должна быть запись
        String monthMeeting = formatter.format(dateMeeting);
        System.out.println(monthMeeting);

        // сравнение месяцев, узнаем нужно ли переключать на следующий месяц
        if (monthMeeting.equals(monthNow)) {
            $$(".popup .calendar__layout .calendar__day").find(exactText(dayMeeting)).click();
        } else {
            $$(".calendar__arrow_direction_right").findBy(attribute("data-step", "1")).click();
            $$(".popup .calendar__layout .calendar__day").find(exactText(dayMeeting)).click();
        }
    }

    @Test
    void shouldSubmitForm() {
        $("[data-test-id=city] input").setValue("Воронеж");
        $("[data-test-id=date] input").sendKeys(Keys.chord(Keys.CONTROL, "a"), Keys.DELETE);
        $("[data-test-id=date] input").setValue(setMeetingInFieldDate());
        $("[data-test-id=name] input").setValue("Иванов Иван-Алексей");
        $("[data-test-id=phone] input").setValue("+71234567890");
        $("[data-test-id=agreement]").click();
        $$("button").find(exactText("Забронировать")).click();
        $("[data-test-id=notification]").shouldBe(visible, Duration.ofSeconds(15));
    }

    @Test
    void shouldSubmitFormWithClickOnTools() {
        $("[data-test-id=city] input").setValue("Во");
        $$(".popup .menu-item__control").find(exactText("Воронеж")).click();
        //клик на календарь
        $("[data-test-id=date] .icon").click();
        setMeetingInCalendarTool();
        $("[data-test-id=name] input").setValue("Иванов Иван-Алексей");
        $("[data-test-id=phone] input").setValue("+71234567890");
        $("[data-test-id=agreement]").click();
        $$("button").find(exactText("Забронировать")).click();
        $("[data-test-id=notification]").shouldBe(visible, Duration.ofSeconds(15));
    }
}
