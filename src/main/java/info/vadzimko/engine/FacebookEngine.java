package info.vadzimko.engine;

import com.google.gson.Gson;

import javax.servlet.ServletException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

public class FacebookEngine {
    private static final Long APP_ID = 0L; // FB APP ID
    private static final String CLIENT_SECRET = ""; // APP Secret code
    private static final String REDIRECT_URI = "https://digitario.vadzimko.info/FBlogin/callback/";
    private static final String PERMISSIONS = "public_profile";
    public static final String AUTH_URI = "https://www.facebook.com/dialog/oauth?"
            + "client_id=" + APP_ID
            + "&redirect_uri=" + REDIRECT_URI
            + "&scope=" + PERMISSIONS;

    private static String accessToken = null;

    public String getFBGraphUrl(String code) {
        try {
            return "https://graph.facebook.com/oauth/access_token?"
                    + "client_id=" + APP_ID
                    + "&redirect_uri=" + URLEncoder.encode(REDIRECT_URI, "UTF-8")
                    + "&client_secret=" + CLIENT_SECRET
                    + "&code=" + code;
        } catch (UnsupportedEncodingException e) {
            //
        }
        return null;
    }

    public String getAccessToken(String code) throws ServletException {
        URL fbGraphURL;
        try {
            fbGraphURL = new URL(getFBGraphUrl(code));
        } catch (MalformedURLException e) {
            throw new ServletException("Invalid code received " + e);
        }
        URLConnection fbConnection;
        StringBuffer b = null;
        try {
            fbConnection = fbGraphURL.openConnection();
            BufferedReader in;
            in = new BufferedReader(new InputStreamReader(
                    fbConnection.getInputStream()));
            String inputLine;
            b = new StringBuffer();
            while ((inputLine = in.readLine()) != null)
                b.append(inputLine);
            in.close();
        } catch (IOException e) {
            throw new ServletException("Unable to connect with Facebook " + e);
        }
        Gson gson = new Gson();
        accessToken = b.toString();
        Map<String, String> data = gson.fromJson(b.toString(), HashMap.class);
        accessToken = data.get("access_token");
        return accessToken;
    }
}
