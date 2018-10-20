package info.vadzimko.engine;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;

public class FBGraph {
    private String accessToken;

    public FBGraph(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getFBGraph() {
        String graph = null;
        try {
            String g = "https://graph.facebook.com/me?access_token=" + accessToken;
            URL u = new URL(g);
            URLConnection c = u.openConnection();
            BufferedReader in = new BufferedReader(new InputStreamReader(
                    c.getInputStream()));
            String inputLine;
            StringBuilder b = new StringBuilder();
            while ((inputLine = in.readLine()) != null)
                b.append(inputLine).append("\n");
            in.close();
            graph = b.toString();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("ERROR in getting FB graph data. " + e);
        }
        return graph;
    }

    public Map<String, String> getGraphData(String fbGraph) {
        Map<String, String> fbProfile;
        Gson gson = new Gson();
        fbProfile = gson.fromJson(fbGraph, HashMap.class);
        return fbProfile;
    }
}