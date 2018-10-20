package info.vadzimko.web.Exception;

public class RedirectException extends RuntimeException {
    private final String url;
    private final String action;

    public RedirectException(String url) {
        this(url, null);
    }

    public RedirectException(String url, String action) {
        this.url = url;
        this.action = action;
    }

    public String getUrl() {
        return url;
    }

    public String getAction() {
        return action;
    }
}
