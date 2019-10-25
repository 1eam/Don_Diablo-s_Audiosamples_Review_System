import com.diabolo.api.PathHandler;
import com.diabolo.util.RegexUtil;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("Test params method in PathHandler class")
class ParamsTest {
    /**
     * Test using reflection to access the private method
     */
    @Test
    @DisplayName("Should return empty string for \"path/even/more/arg\"")
    void paramsNone() {
        try {
            Method method = PathHandler.class.getDeclaredMethod("params", String.class);
            method.setAccessible(true);
            String test = (String)method.invoke(PathHandler.class.getDeclaredConstructor().newInstance(),
                    "path/even/more/arg");
            assertEquals("", test);
        } catch (Exception ex) {}
    }

    @Test
    @DisplayName("Should return \"arg\" for \"path/even/more/{arg}\"")
    void paramsOne() {
        try {
            Method method = PathHandler.class.getDeclaredMethod("params", String.class);
            method.setAccessible(true);
            String test = (String)method.invoke(PathHandler.class.getDeclaredConstructor().newInstance(),
                    "path/even/more/{arg}");
            assertEquals("arg", test);
        } catch (Exception ex) {}
    }

    /*
    @Test
    @DisplayName("Should return true for \"test@test.com\"")
    void regexEmail() {
        try {
            Method method = RegexUtil.class.getDeclaredMethod("hasValidTest", String.class);
            method.setAccessible(true);
            boolean test = (boolean)method.invoke(PathHandler.class.getDeclaredConstructor().newInstance(),
                    "test@test.com");
            assertTrue(test);
        } catch (Exception ex) {}
    }*/
}
