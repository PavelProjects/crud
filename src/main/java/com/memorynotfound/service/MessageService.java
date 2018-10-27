package com.memorynotfound.service;

import com.memorynotfound.config.DtSource;
import com.memorynotfound.controller.UserController;
import com.memorynotfound.model.Meeting;
import com.memorynotfound.model.Message;
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
    private MeetingService mservice = new MeetingService();
    private DataSource dataSource = DtSource.getDts();
    private final Logger LOG = LoggerFactory.getLogger(UserController.class);


    @Override
    public void sendToMeeting(Message message, Meeting meeting) throws Exception {
        LOG.info("start sending....");
        if (mservice.getMettById(meeting.getAdmin(), meeting.getId()) != null) {
            List<User> users = meeting.getUsers();
            for (User user : users) {
                message.setTo(user.getId());
                addToBase(message);
                fb.sendMessage(message);
            }
        }
        LOG.info("....end");
    }

    private void addToBase(Message message) {
        int id = 0;
        try {
            LOG.info("add message");
            Connection c = dataSource.getConnection();
            PreparedStatement preparedStatement = c.prepareStatement("insert into message (message,f,send_to) values (?,?,?) returning id;");
            preparedStatement.setString(1, message.getData().getMessage());
            preparedStatement.setString(2, message.getData().getF());
            preparedStatement.setString(3, message.getTo());
            LOG.info(preparedStatement.toString());
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                id = rs.getInt("id");
            }
            LOG.info("Message id:" + String.valueOf(id));
            if (id != 0) {
                preparedStatement = c.prepareStatement("insert into messages values (?,?);");
                preparedStatement.setInt(1, id);
                preparedStatement.setInt(2, message.getData().getMeeting().getId());
                preparedStatement.execute();
            }
            preparedStatement.close();
            c.close();
        } catch (Exception e) {

        }
    }
}
