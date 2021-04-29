package example;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestStreamHandler;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.nimbusds.oauth2.sdk.TokenRequest;
import no.nav.security.mock.oauth2.MockOAuth2Server;
import no.nav.security.mock.oauth2.token.OAuth2TokenCallback;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

// Handler value: example.Handler
public class Handler implements RequestStreamHandler {
    Gson gson = new GsonBuilder().setPrettyPrinting().create();

    /**
     * Can be anything you choose - should uniquely identify your issuer if you have several
     */
    public static final String ISSUER_ID = "https://myproject.auth0.com/";
    public static final String CLIENT_ID = "ims2syNBwkHvwEARxSzKzEPe15PVhDWlx7";

    /**
     * The handleRequest interface, as below,
     * with InputStream, OutputStream
     *
     * when Lambda is used with the API Gateway - is required.
     *
     * https://www.baeldung.com/aws-lambda-api-gateway
     *
     * @param inputStream
     * @param outputStream
     * @param context
     * @throws IOException
     */
    public void handleRequest(InputStream inputStream, OutputStream outputStream, Context context) throws IOException {
        JSONParser parser = new JSONParser();
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        JSONObject responseJson = new JSONObject();

        JSONObject event = null;
        try {
            event = (JSONObject) parser.parse(reader);

            handleEvent(event,
                    responseJson,
                    inputStream,
                    outputStream,
                    context);

        } catch (ParseException e) {
            responseJson.put("statusCode", 400);
            responseJson.put("exception", e);
        }

        // writing the result into the
        OutputStreamWriter writer = new OutputStreamWriter(outputStream, "UTF-8");
        writer.write(responseJson.toString());
        writer.close();
    }

    private void handleEvent(JSONObject event, JSONObject responseJson, InputStream inputStream, OutputStream outputStream, Context context) throws IOException {

        LambdaLogger logger = context.getLogger();

        OutputStreamWriter writer = new OutputStreamWriter(outputStream, "UTF-8");
        writer.write(responseJson.toString());
        writer.close();


        // log execution details
        logger.log("ENVIRONMENT VARIABLES: " + gson.toJson(System.getenv()));
        logger.log("CONTEXT: " + gson.toJson(context));

        // process event
        logger.log("EVENT: " + gson.toJson(event));
        logger.log("EVENT TYPE: " + event.getClass());

        // oauth
//        logger.log("OAUTH: starting server");
//        try {
//            final MockOAuth2Server mockOAuth2Server = startOauthServer(context);
//
//            // ......
//            // Setup your app with metadata from wellKnownUrl and do your testing here
//            // ......
//            useOauthServer(mockOAuth2Server, event, context);
//
//            // done
//            mockOAuth2Server.shutdown();
//
//        } catch (IOException e) {
//            logger.log("EXCEPTION: " + e.getMessage());
//        }
//        logger.log("OAUTH: stop server");

        JSONObject obj2 = new JSONObject();
        obj2.put("Content-Type", "application/json");
        responseJson.put("statusCode", 200);
        responseJson.put("headers", obj2);
        responseJson.put("body", "hello world");
    }


    private void useOauthServer(MockOAuth2Server mockOAuth2Server, Map<String, String> event, Context context) {
        // TODO: retrieve the token values

        /*
            create a mocked answer
            derive claims from the
         */
        mockOAuth2Server.enqueueCallback(
                new OAuth2TokenCallback() {
                    @NotNull
                    @Override
                    public String issuerId() {
                        return ISSUER_ID;
                    }

                    @Nullable
                    @Override
                    public String subject(@NotNull TokenRequest tokenRequest) {
                        // TODO: replace hardcoded id
                        return "oauth2|main-tenant-oidc|samlp|MyProject|Z003XBAP";
                    }

                    @NotNull
                    @Override
                    public List<String> audience(@NotNull TokenRequest tokenRequest) {
                        return Arrays.asList(CLIENT_ID);
                    }

                    @NotNull
                    @Override
                    public Map<String, Object> addClaims(@NotNull TokenRequest tokenRequest) {
//                        return Collections.singletonMap("navvisgroups", new String[]{"Noob", "Publisher"});
                        return Collections.singletonMap("navvisgroups", new String[]{"Publisher"});
                    }

                    @Override
                    public long tokenExpiry() {
                        // + 8 hours
                        return System.currentTimeMillis() + TimeUnit.HOURS.toMillis(8);
                    }
                });

    }


    private MockOAuth2Server startOauthServer(Context context) throws IOException {
        LambdaLogger logger = context.getLogger();

        MockOAuth2Server server = new MockOAuth2Server();

        server.start();

        /*
         Discovery url to authorization server metadata
         This endpoint will contain the JWK used to sign all issued JWTs

         A request to http://localhost:8080/default/.well-known/openid-configuration will yield an issuerId of default with the following configuration:
         {
           "issuer":"http://localhost:8080/default",
           "authorization_endpoint":"http://localhost:8080/default/authorize",
           "token_endpoint":"http://localhost:8080/default/token",
           "jwks_uri":"http://localhost:8080/default/jwks",
           "response_types_supported":[
              "query",
              "fragment",
              "form_post"
           ],
           "subject_types_supported":[
              "public"
           ],
           "id_token_signing_alg_values_supported":[
              "RS256"
           ]
        }
        */
        String wellKnownUrl = server.wellKnownUrl(ISSUER_ID).toString();
        logger.log("wellKnownUrl: " + wellKnownUrl);

        return server;
    }


//    static class PortRedirector extends AbstractHandler {
//
//        int to;
//
//        PortRedirector(int to) {
//            this.to = to;
//        }
//
//        public void handle(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response)
//                throws IOException, ServletException {
//            String uri = request.getScheme() + "://" +
//                    request.getServerName() +
//                    ":" + to +
//                    request.getRequestURI() +
//                    (request.getQueryString() != null ? "?" + request.getQueryString() : "");
//            response.sendRedirect(uri);
//        }
//    }
}
