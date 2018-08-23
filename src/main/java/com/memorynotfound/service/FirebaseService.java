package com.memorynotfound.service;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class FirebaseService implements FbService {
    private static String url= "https://gcm-http.googleapis.com/gcm/send";
    private static String serverKey="AAAA_nYrORc:APA91bHh1UqedOuiEzIFUfG6yAdLUNBEKQOUuTdmTxXDHgseZ6yEnGPq5UQibQza9bPZKqRyRVfYvQFTX7EbS7kXGbCUjGEZy1dWek7Y31_3OaKHIUncz5wosMvFHaXNMVZCjllbGW9Y3Vm-mTnlxtcavz7rnaY55Q";
    @Override
    public String sendMessage(String message) throws Exception{
        HttpURLConnection conn = null;
        try{
        URL obj =new URL(url);
        conn =(HttpURLConnection) obj.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type","application/json");
        conn.setRequestProperty("Authorization","key="+serverKey);
        conn.setDoInput(true);
        conn.setDoOutput(true);

        DataOutputStream wr = new DataOutputStream(
                conn.getOutputStream ());
        wr.write(message.getBytes("UTF-8"));
        wr.flush ();
        wr.close ();

        InputStream inputStream = conn.getInputStream();
        BufferedReader br= new BufferedReader(new InputStreamReader(inputStream));
        String responseData;
        StringBuffer response = new StringBuffer();
        while((responseData = br.readLine()) != null) {
            response.append(responseData);
            response.append('\r');
        }
        br.close();
        return response.toString();

    } catch (Exception e) {

        e.printStackTrace();
        return null;

    } finally {

        if(conn != null) {
            conn.disconnect();
        }
    }
    }
}
