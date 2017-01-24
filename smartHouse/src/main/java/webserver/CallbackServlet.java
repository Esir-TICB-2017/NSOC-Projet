package webserver;

import com.google.common.collect.ImmutableMap;
import com.google.gson.Gson;
import jdk.nashorn.internal.parser.JSONParser;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.conn.ssl.X509HostnameVerifier;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.SingleClientConnManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSession;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CallbackServlet extends HttpServlet {

    String clientId = "299325628592-hqru0vumh16bp0hhhvj9qr35lglm8gqu.apps.googleusercontent.com";
    String clientSecret = "ejQF1ZgyRgHIrExGZ3Yvj0e7";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (req.getParameter("error") != null) {
            resp.getWriter().println(req.getParameter("error"));
            return;
        }
        // google returns a code that can be exchanged for a access token
        String code = req.getParameter("code");


        // get the access token by post to Google
        String body = post("https://accounts.google.com/o/oauth2/token", ImmutableMap.<String, String>builder()
                .put("code", code)
                .put("client_id", clientId)
                .put("client_secret", clientSecret)
                .put("redirect_uri", "http://localhost:8080/callback")
                .put("grant_type", "authorization_code").build());

        System.out.println(body);

        JSONObject jsonObj = null;

        // get the access token from json and request info from Google
        try {
            jsonObj = new JSONObject(body);
        } catch (Exception e) {
            throw new RuntimeException("Unable to parse json " + body);
        }

        // google tokens expire after an hour, but since we requested offline access we can get a new token without user involvement via the refresh token
        String accessToken = (String) jsonObj.get("access_token");

        // you may want to store the access token in session
        req.getSession().setAttribute("access_token", accessToken);

        // get some info about the user with the access token
        String json = get(new StringBuilder("https://www.googleapis.com/oauth2/v1/userinfo?access_token=").append(accessToken).toString());


        // now we could store the email address in session

        // return the json of the user's basic info
        resp.getWriter().println(json);
    }


    // makes a GET request to url and returns body as a string
    public String get(String url) throws ClientProtocolException, IOException {
        return execute(new HttpGet(url));
    }

    // makes a POST request to url with form parameters and returns body as a string
    public String post(String url, Map<String, String> formParameters) throws ClientProtocolException, IOException {
        HttpPost request = new HttpPost(url);

        List<NameValuePair> nvps = new ArrayList<NameValuePair>();

        for (String key : formParameters.keySet()) {
            nvps.add(new BasicNameValuePair(key, formParameters.get(key)));
        }

        request.setEntity(new UrlEncodedFormEntity(nvps));

        return execute(request);
    }

    // makes request and checks response code for 200
    private String execute(HttpRequestBase request) throws ClientProtocolException, IOException {
        HostnameVerifier hostnameVerifier = org.apache.http.conn.ssl.SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER;

        DefaultHttpClient client = new DefaultHttpClient();

        SchemeRegistry registry = new SchemeRegistry();
        SSLSocketFactory socketFactory = SSLSocketFactory.getSocketFactory();
        socketFactory.setHostnameVerifier((X509HostnameVerifier) hostnameVerifier);
        registry.register(new Scheme("https", socketFactory, 443));
        SingleClientConnManager mgr = new SingleClientConnManager(client.getParams(), registry);
        DefaultHttpClient httpClient = new DefaultHttpClient(mgr, client.getParams());

// Set verifier
        HttpsURLConnection.setDefaultHostnameVerifier(hostnameVerifier);
        HttpResponse response = httpClient.execute(request);

        HttpEntity entity = response.getEntity();
        String body = EntityUtils.toString(entity);

        if (response.getStatusLine().getStatusCode() != 200) {
            throw new RuntimeException("Expected 200 but got " + response.getStatusLine().getStatusCode() + ", with body " + body);
        }

        return body;
    }
}