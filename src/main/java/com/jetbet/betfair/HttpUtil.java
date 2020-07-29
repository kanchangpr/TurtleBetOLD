package com.jetbet.betfair;


import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.Duration;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jetbet.betfair.exceptions.APINGException;
import com.jetbet.betfair.util.RescriptResponseHandler;
import com.jetbet.dto.SessionDetails;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

public class HttpUtil {

    private final String HTTP_HEADER_X_APPLICATION = "X-Application";
    private final String HTTP_HEADER_X_AUTHENTICATION = "X-Authentication";
    private final String HTTP_HEADER_CONTENT_TYPE = "Content-Type";
    private final String HTTP_HEADER_ACCEPT = "Accept";
    private final String HTTP_HEADER_ACCEPT_CHARSET = "Accept-Charset";
    

    public HttpUtil() {
        super();
    }

    private String sendPostRequest(String param, String operation, String appKey, String ssoToken, String URL, ResponseHandler<String> reqHandler){
        String jsonRequest = param;

        HttpPost post = new HttpPost(URL);
        String resp = null;
        try {
            post.setHeader(HTTP_HEADER_CONTENT_TYPE, ApiNGDemo.getProp().getProperty("APPLICATION_JSON"));
            post.setHeader(HTTP_HEADER_ACCEPT, ApiNGDemo.getProp().getProperty("APPLICATION_JSON"));
            post.setHeader(HTTP_HEADER_ACCEPT_CHARSET, ApiNGDemo.getProp().getProperty("ENCODING_UTF8"));
            post.setHeader(HTTP_HEADER_X_APPLICATION, appKey);
            post.setHeader(HTTP_HEADER_X_AUTHENTICATION, ssoToken);

            post.setEntity(new StringEntity(jsonRequest, ApiNGDemo.getProp().getProperty("ENCODING_UTF8")));

            HttpClient httpClient = new DefaultHttpClient();

            HttpParams httpParams = httpClient.getParams();
            HttpConnectionParams.setConnectionTimeout(httpParams, new Integer(ApiNGDemo.getProp().getProperty("TIMEOUT")).intValue());
            HttpConnectionParams.setSoTimeout(httpParams, new Integer(ApiNGDemo.getProp().getProperty("TIMEOUT")).intValue());

            resp = httpClient.execute(post, reqHandler);

        } catch (UnsupportedEncodingException e1) {
            //Do something

        } catch (ClientProtocolException e) {
            //Do something

        } catch (IOException ioE){
            //Do something

        }

        return resp;

    }
    private SessionDetails sendPostRequestForLogin(String param, String operation, String URL, ResponseHandler<String> reqHandler){
        String jsonRequest = param;
        SessionDetails sessionDetails=null;
        ObjectMapper mapper = new ObjectMapper();
        HttpPost post = new HttpPost(URL);
        String resp = null;
        try {
        	String uri = String.format("https://%s/api/login?username=%s&password=%s",
                    "identitysso.betfair.com",
                    URLEncoder.encode("shiltonpereira@live.com", StandardCharsets.UTF_8.name()),
                    URLEncoder.encode("Wsxedc@123", StandardCharsets.UTF_8.name()));
        	
        	 Client client = Client.create();
             //client.setConnectTimeout((int) (getTimeout.getSeconds() * 1000));
             WebResource webResource = client.resource(uri);

             ClientResponse clientResponse = webResource
                     .accept("application/json")
                     .header("X-Application", "5tsF8QHfEw3n4Kp8")
                     .header("Content-Type", "application/x-www-form-urlencoded")
                     .post(ClientResponse.class);

             mapper = new ObjectMapper();
             sessionDetails = mapper.readValue(clientResponse.getEntityInputStream(), SessionDetails.class);
             System.out.println("{}: Response: {}"+  sessionDetails);
         
        } catch (UnsupportedEncodingException e1) {
            //Do something

        } catch (ClientProtocolException e) {
            //Do something

        } catch (IOException ioE){
            //Do something

        }

        return sessionDetails;

    }
    
    public String sendPostRequestRescript(String param, String operation, String appKey, String ssoToken) throws APINGException{
        String apiNgURL = ApiNGDemo.getProp().getProperty("APING_URL") + ApiNGDemo.getProp().getProperty("RESCRIPT_SUFFIX")+operation+"/";

        return  sendPostRequest(param, operation, appKey, ssoToken, apiNgURL, new RescriptResponseHandler());

    }
    
    public SessionDetails sendPostRequestRescriptForLogin(String param, String operation) throws APINGException{
        String apiNgURL = ApiNGDemo.getProp().getProperty("LOGIN_URL") +operation+"/";

        return  sendPostRequestForLogin(param, operation, apiNgURL, new RescriptResponseHandler());

    }


}
