package com.devrajs.practice.oyo;


import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by devraj.singh on 9/26/15.
 */
public class HttpHelper {

    public static CustomResponse httpget(String uri) {
        try {
            HttpClient httpClient = HttpClientBuilder.create().build();
            HttpGet httpGet = new HttpGet(uri);
            HttpResponse response = httpClient.execute(httpGet);
            CustomResponse customResponse = new CustomResponse(response);
            return customResponse;
        }
        catch (IOException e)
        {
            e.printStackTrace();
            return null;
        }
    }
}

class CustomResponse
{
    private int responseCode;
    private String content;

    CustomResponse(HttpResponse response)
    {
        StatusLine statusLine = response.getStatusLine();
        HttpEntity httpEntity = response.getEntity();
        responseCode = statusLine.getStatusCode();
        content = getContent(httpEntity);
    }

    private String getContent(HttpEntity httpEntity)
    {
        try {
            InputStream inputStream = httpEntity.getContent();
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String content="";
            String read = bufferedReader.readLine();
            while (read!=null)
            {
                content+=read;
                read = bufferedReader.readLine();
            }
            return content;
        }
        catch (IOException e)
        {
            e.printStackTrace();
            return null;
        }
    }

    public int getResponseCode()
    {
        return responseCode;
    }
    public String getContent()
    {
        return content;
    }
}
