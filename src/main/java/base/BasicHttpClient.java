package base;

import config.TestProperties;
import org.apache.http.NameValuePair;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.io.IOException;
import java.util.List;

public class BasicHttpClient {

    private static BasicHttpClient instance;
    private static CloseableHttpClient httpClient;

    private static final String DEFAULT_USER = TestProperties.get("user");
    private static final String DEFAULT_PASS = TestProperties.get("password");


    private BasicHttpClient(CloseableHttpClient httpClient) {
        CredentialsProvider provider = new BasicCredentialsProvider();
        provider.setCredentials(AuthScope.ANY, new UsernamePasswordCredentials(DEFAULT_USER, DEFAULT_PASS));
        this.httpClient = HttpClients.custom().setDefaultCredentialsProvider(provider).build();
    }

    public static BasicHttpClient getInstance(){
        if (instance == null) {
            instance = new BasicHttpClient(httpClient);
        }
        return instance;
    }


    public CloseableHttpResponse executePostRequest(String url, List<NameValuePair> parameters) throws IOException {
        HttpPost httpPost = new HttpPost(url);
        httpPost.setEntity(new UrlEncodedFormEntity(parameters));
        CloseableHttpResponse response = (CloseableHttpResponse) httpClient
                .execute((HttpUriRequest) httpPost);
        return response;
    }

    public void close() throws Exception {

    }

}
