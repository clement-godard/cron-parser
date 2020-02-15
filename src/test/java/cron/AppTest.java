/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package cron;

import org.junit.Test;
import static org.junit.Assert.*;

public class AppTest {
    @Test
    public void parserShouldBeNotNullWhenValidInput() {
        String valid = "1 1 1 1 1 /command";
        App classUnderTest = new App();
        assertNotNull(classUnderTest.parseAndValidateArguments(valid));
    }

    @Test
    public void parserShouldBeNullWhenInvalidInput() {
        String invalid = "1 1 1 1 1";
        App classUnderTest = new App();
        assertNull(classUnderTest.parseAndValidateArguments(invalid));
    }

    // Range
    @Test
    public void parseRangeShouldBeTrueWhenValidInput() {
        String invalid = "1 1 1 1 1";
        App classUnderTest = new App();
        String field = "1-5";
        String key = "minute";
        assertTrue(classUnderTest.parseRange(field, key, 0, 59));
    }

    @Test
    public void parseRangeShouldBeFalseWhenInvalidInput() {
        App classUnderTest = new App();
        String field = "10-9";
        String key = "minute";
        assertFalse(classUnderTest.parseRange(field, key, 0, 59));
    }

    @Test
    public void parseRangeShouldMatchRule() {
        App classUnderTest = new App();
        String field = "1-5";
        String key = "minute";
        String expected = "1 2 3 4 5";
        classUnderTest.parseRange(field, key, 0, 59);
        assertEquals(expected, classUnderTest.getResult().get(key));
    }

    // Sequence
    @Test
    public void parseSequenceShouldBeTrueWhenValidInput() {
        App classUnderTest = new App();
        String field = "1,5";
        String key = "minute";
        assertTrue(classUnderTest.parseSequence(field, key, 0, 59));
    }

    @Test
    public void parseSequenceShouldBeFalseWhenInvalidInput() {
        App classUnderTest = new App();
        String field = "60,500";
        String key = "minute";
        assertFalse(classUnderTest.parseSequence(field, key, 0, 59));
    }

    @Test
    public void parseSequenceShouldMatchRule() {
        App classUnderTest = new App();
        String field = "1,5,7";
        String key = "minute";
        String expected = "1 5 7";
        classUnderTest.parseSequence(field, key, 0, 59);
        assertEquals(expected, classUnderTest.getResult().get(key));
    }

    // Step
    @Test
    public void parseStepShouldBeTrueWhenValidInput() {
        App classUnderTest = new App();
        String field = "*/5";
        String key = "minute";
        assertTrue(classUnderTest.parseStep(field, key, 0, 59));
    }

    @Test
    public void parseStepShouldBeFalseWhenInvalidInput() {
        App classUnderTest = new App();
        String field = "*/100";
        String key = "minute";
        assertFalse(classUnderTest.parseStep(field, key, 0, 59));
    }

    @Test
    public void parseStepShouldMatchRule() {
        App classUnderTest = new App();
        String field = "*/10";
        String key = "minute";
        String expected = "0 10 20 30 40 50";
        classUnderTest.parseStep(field, key, 0, 59);
        assertEquals(expected, classUnderTest.getResult().get(key));
    }

    // Base
    @Test
    public void parseBaseShouldBeTrueWhenValidInput() {
        App classUnderTest = new App();
        String field = "6";
        String key = "minute";
        assertTrue(classUnderTest.parseBase(field, key, 0, 59));
    }

    @Test
    public void parseBaseShouldBeFalseWhenInvalidInput() {
        App classUnderTest = new App();
        String field = "60";
        String key = "minute";
        assertFalse(classUnderTest.parseBase(field, key, 0, 59));
    }

    @Test
    public void parseBaseShouldMatchRule() {
        App classUnderTest = new App();
        String field = "10";
        String key = "minute";
        String expected = "10";
        classUnderTest.parseBase(field, key, 0, 59);
        assertEquals(expected, classUnderTest.getResult().get(key));
    }

    // Star
    @Test
    public void parseStarShouldBeTrueWhenValidInput() {
        App classUnderTest = new App();
        String field = "*";
        String key = "minute";
        assertTrue(classUnderTest.parseStar(field, key, 0, 59));
    }

    @Test
    public void parseStarShouldMatchRule() {
        App classUnderTest = new App();
        String field = "*";
        String key = "month";
        String expected = "1 2 3 4 5 6 7 8 9 10 11 12";
        classUnderTest.parseStar(field, key, 1, 12);
        assertEquals(expected, classUnderTest.getResult().get(key));
    }

}