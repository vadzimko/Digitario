package info.vadzimko.engine;

import com.vk.api.sdk.client.TransportClient;
import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.UserActor;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.api.sdk.httpclient.HttpTransportClient;
import com.vk.api.sdk.objects.UserAuthResponse;

import javax.servlet.ServletException;

public class VKEngine {
    private static final Integer APP_ID = 0; // VK APP ID
    private static final String CLIENT_SECRET = ""; // APP secret code
    private static final String REDIRECT_URI = "https://digitario.vadzimko.info/VKlogin/callback";
    private static final String PERMISSIONS = "friends";
    public static final String OAUTH_URI = "https://oauth.vk.com/authorize?client_id=" + APP_ID
            + "&redirect_uri=" + REDIRECT_URI
            + "&scope=" + PERMISSIONS
            + "&response_type=code&v=5.85";

    private static TransportClient transportClient = HttpTransportClient.getInstance();
    public static VkApiClient vk = new VkApiClient(transportClient);

    public static UserActor getActor(String code) throws ServletException {
        try {
            UserAuthResponse authResponse = vk.oauth()
                    .userAuthorizationCodeFlow(APP_ID, CLIENT_SECRET, REDIRECT_URI, code)
                    .execute();

            return new UserActor(authResponse.getUserId(), authResponse.getAccessToken());
        } catch (ApiException | ClientException e) {
            throw new ServletException(e.getMessage());
        }
    }
}
