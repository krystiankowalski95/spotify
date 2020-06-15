package pl.lodz.p.it.zzpj.spotify;

import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class UtilsToRandTest {
    @Test
    void getRandomFromRangeUnreapeatedTest() {
        final int maxToTest = 1000;
        final int countToTest = 50;

        List<Integer> testList = UtilsToRand.getRandomFromRangeUnreapeated(maxToTest, countToTest);

        Assert.assertEquals(countToTest, testList.size());
        testList.stream()
                .forEach(x -> {
            if(x > maxToTest) Assert.fail();
        });
    }
}
