package example;

import no.nav.security.mock.oauth2.MockOAuth2Server;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class OauthTest {

    private static final Logger logger = LoggerFactory.getLogger(OauthTest.class);


    @Test
    void invokeTest() {
        try {
            MockOAuth2Server server = new MockOAuth2Server();

            server.start();

            // Can be anything you choose - should uniquely identify your issuer if you have several
            String issuerId = "default";

            // Discovery url to authorization server metadata
            String wellKnownUrl = server.wellKnownUrl(issuerId).toString();
            logger.debug("wellKnownUrl: " + wellKnownUrl);

            // ......
            // Setup your app with metadata from wellKnownUrl and do your testing here
            // ......
            server.shutdown();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
