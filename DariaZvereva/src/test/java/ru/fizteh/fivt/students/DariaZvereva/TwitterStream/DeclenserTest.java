package ru.fizteh.fivt.students.DariaZvereva.TwitterStream;

import junit.framework.TestCase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

/**
 * Created by Dasha on 05.11.2015.
 */
@RunWith(MockitoJUnitRunner.class)
public class DeclenserTest extends TestCase {

    public static final int NUMBER_OF_TESTS = 30;
    public static final Integer DIFFERENCE = 100;

    @Test
    public void declenserTest() throws Exception{
        Map<Integer, Integer> answers = new HashMap<>();
        answers.put(1, 0);
        answers.put(2, 1);
        answers.put(5, 2);
        answers.put(9, 2);
        answers.put(12, 2);
        answers.put(111, 2);
        answers.put(51, 0);
        answers.put(123, 1);
        answers.put(102, 1);

        for(Integer number : answers.keySet()) {
            for (int i = 0; i < NUMBER_OF_TESTS; ++i) {
                assertThat(Declenser.strForm(number + i * DIFFERENCE), is(answers.get(number)));
            }
        }
    }
}
