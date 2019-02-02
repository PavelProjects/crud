package com.memorynotfound.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.memorynotfound.config.DtSource;
import com.memorynotfound.controller.UserController;
import com.memorynotfound.model.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;

public class FirebaseService implements FbService {
    private static String url= "https://gcm-http.googleapis.com/gcm/send";
    private static String serverKey="AAAA_nYrORc:APA91bHh1UqedOuiEzIFUfG6yAdLUNBEKQOUuTdmTxXDHgseZ6yEnGPq5UQibQza9bPZKqRyRVfYvQFTX7EbS7kXGbCUjGEZy1dWek7Y31_3OaKHIUncz5wosMvFHaXNMVZCjllbGW9Y3Vm-mTnlxtcavz7rnaY55Q";
    private DataSource dataSource = DtSource.getDts();
    private final Logger LOG = LoggerFactory.getLogger(FirebaseService.class);



    @Override
    public String sendMessage(Message value) throws Exception{
        LOG.info("send message to: "+value.getTo());
        ObjectMapper objectMapper = new ObjectMapper();
        String message = objectMapper.writeValueAsString(value);
        LOG.info("MESSAGE: "+message);
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
            LOG.info(response.toString());
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

    @Override
    public void TokenChanged(String token,String mail) {
        LOG.info("new token: "+token+", for user: "+mail);
        try{
            Connection c = dataSource.getConnection();
            PreparedStatement pr= c.prepareStatement("updateName users set id = ? where mail = ?;");
            pr.setString(1,token);
            pr.setString(2,mail);
            pr.execute();
            pr.close();
            c.close();
        }catch (Exception e){
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }
    }
}
