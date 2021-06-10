import com.siberteam.edu.dict.UrlToSetReader;
import org.junit.*;

import java.util.HashSet;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.CountDownLatch;

public class UrlToSetReaderTest {
    Set<String> actualDictionary;
    Set<String> expectedDictionary;

    @Before
    public void setUp() {
        actualDictionary = new HashSet<>();
        expectedDictionary = new HashSet<>();

        Queue<String> urlFiles = new PriorityQueue<>();
        urlFiles.add("file:///home/retw/IdeaProjects" +
                "/url-dictionary/src/main/resources/test.txt");

        UrlToSetReader urlToSetReader = new UrlToSetReader(actualDictionary,
                urlFiles, new CountDownLatch(1));

        urlToSetReader.run();

        expectedDictionary.add("что");
        expectedDictionary.add("либо");
        expectedDictionary.add("зачем");
        expectedDictionary.add("когда");
        expectedDictionary.add("смотреть");
        expectedDictionary.add("кливленд");
        expectedDictionary.add("крым");
        expectedDictionary.add("привет");
        expectedDictionary.add("как");
        expectedDictionary.add("нога");
    }

    @Test
    public void parseString() {
        Assert.assertEquals(expectedDictionary, actualDictionary);
    }
}
