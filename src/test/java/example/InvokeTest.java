package example;

import com.amazonaws.services.lambda.runtime.Context;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.assertTrue;

class InvokeTest {
    private static final Logger logger = LoggerFactory.getLogger(InvokeTest.class);

    @Test
    void invokeTest() {
        logger.info("Invoke TEST");
//        HashMap<String, Object> event = new HashMap<String, Object>();
//        Context context = new TestContext();
//        Handler handler = new Handler();
//        String result = handler.handleRequest(event, context);
//        logger.info("Result " + result);
//        assertTrue(result.contains("200"));
    }

}
