package com.memorynotfound.service;

import com.memorynotfound.config.DtSource;
import com.memorynotfound.model.Meeting;
import com.memorynotfound.model.Message;
import com.memorynotfound.model.MessagingData;
import com.memorynotfound.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class MessageService implements MesService {
    private FirebaseService fb = new FirebaseService();
    private DataSource dataSource = DtSource.getDts();
    private final Logger LOG = LoggerFactory.getLogger(MessageService.class);


    @Override
    public void sendToMeeting(Message message, Meeting meeting) throws Exception {
        LOG.info("start sending....");
        if (meeting != null) {
            List<User> users = meeting.getUsers();
            addToBase(message);
            if (!users.isEmpty()) {
                for (int i =0;i<users.size();i++) {
                    message.setTo(users.get(i).getId().replaceAll("([\"])", ""));
                    fb.sendMessage(message);
                }
                LOG.info("....end");
            }else{
                LOG.info("no users!");
            }
        }else{
            LOG.info("MEETING NOT FOUND");
        }

    }

    private void addToBase(Message message) {
        int id = 0;
        try {
            LOG.info("add message to base");
            Connection c = dataSource.getConnection();
            PreparedStatement preparedStatement = c.prepareStatement("insert into message (message,f,send_to) values (?,?,?) returning id;");
            preparedStatement.setString(1, message.getData().getMessage());
            preparedStatement.setString(2, message.getData().getF());
            preparedStatement.setString(3, message.getTo());
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                id = rs.getInt("id");
            }
            if (id != 0) {
                preparedStatement = c.prepareStatement("insert into messages values (?,?);");
                preparedStatement.setInt(1, id);
                preparedStatement.setInt(2, message.getData().getMid());
                preparedStatement.execute();
            }
            rs.close();
            preparedStatement.close();
            c.close();
        } catch (Exception e) {

        }
    }

    @Override
    public List<MessagingData> getMessages(String uid,int mid) {
        List<MessagingData> messageData= new ArrayList<>();
        MeetingService mservice = new MeetingService();
        if (mservice.getMettById(uid,mid)!=null) {
            try {
                Connection c = dataSource.getConnection();
                PreparedStatement prmes = c.prepareStatement("select message.* from messages inner join message on messages.mesid = message.id where messages.mid = ?;");
                prmes.setInt(1, mid);
                ResultSet rs = prmes.executeQuery();
                while (rs.next()) {
                    MessagingData data = new MessagingData();
                    data.setF(rs.getString("f"));
                    data.setMessage(rs.getString("message"));
                    data.setId(rs.getInt("id"));
                    messageData.add(data);
                }
                rs.close();
                prmes.close();
                c.close();
            } catch (Exception e) {
                LOG.error(String.valueOf(e));
            }
        }else{
            LOG.info("Haven't got "+ uid +" on "+mid);
        }
        return messageData;
    }
}
