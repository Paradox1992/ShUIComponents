import com.ShDateSelectors.ShDateSelector;
import java.time.LocalDate;

public final class ShDateSelectorDateStringTest {

    private ShDateSelectorDateStringTest() {
    }

    public static void main(String[] args) {
        returnsNullWithoutSelectedDate();
        returnsSelectedDateInYearMonthDayFormat();
        ignoresDisplayPattern();
    }

    private static void returnsNullWithoutSelectedDate() {
        ShDateSelector selector = new ShDateSelector();

        assertEquals(null, selector.getDateString(), "fecha vacia");
    }

    private static void returnsSelectedDateInYearMonthDayFormat() {
        ShDateSelector selector = new ShDateSelector();
        selector.setSelectedDate(LocalDate.of(2026, 9, 1));

        assertEquals("2026-09-01", selector.getDateString(), "formato yyyy-MM-dd");
    }

    private static void ignoresDisplayPattern() {
        ShDateSelector selector = new ShDateSelector();
        selector.setDatePattern("dd/MM/yyyy");
        selector.setSelectedDate(LocalDate.of(2026, 1, 5));

        assertEquals("2026-01-05", selector.getDateString(), "formato independiente del texto visible");
    }

    private static void assertEquals(Object expected, Object actual, String message) {
        if (expected == null ? actual != null : !expected.equals(actual)) {
            throw new AssertionError(message + ": esperado " + expected + ", obtenido " + actual);
        }
    }
}
