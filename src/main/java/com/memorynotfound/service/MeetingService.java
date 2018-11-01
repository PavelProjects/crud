package com.memorynotfound.service;


import com.memorynotfound.config.DtSource;
import com.memorynotfound.controller.ProfileCotroller;
import com.memorynotfound.model.Meeting;
import com.memorynotfound.model.Message;
import com.memorynotfound.model.User;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MeetingService implements Mservice {

    Uservice uservice = new UserService();
    DataSource dataSource = DtSource.getDts();
    FirebaseService fb=new FirebaseService();

    @Override
    public Meeting createMeeting(Meeting meeting) {
        try{
            Connection c = dataSource.getConnection();
            PreparedStatement pr = c.prepareStatement("insert into meeting (name, admin_mail, date, time, adress) values (?,?,?,?,?) returning *;");
            pr.setString(1,meeting.getName());
            pr.setString(2,meeting.getAdmin());
            pr.setDate(3, Date.valueOf(java.time.LocalDate.now()));
            pr.setString(4,String.valueOf(java.time.LocalTime.now()));
            pr.setString(5,meeting.getAdress());
            ResultSet rs = pr.executeQuery();
            while (rs.next()){
                meeting =new Meeting();
                meeting.setId(rs.getInt("id"));
                meeting.setName(rs.getString("name"));
                meeting.setAdmin(rs.getString("admin_mail"));
                meeting.setAdress(rs.getString("adress"));
                meeting.setDate(rs.getString("date"));
                meeting.setTime(rs.getString("time"));
            }
            if (meeting.getId()!=0) {
                pr = c.prepareStatement("insert into meetings values (?,?);");
                pr.setInt(1, meeting.getId());
                pr.setString(2, meeting.getAdmin());
                pr.execute();
            }
            pr.cancel();
            c.close();
        }catch (Exception e){
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }
        return meeting;
    }

    @Override
    public List<Meeting> getAllMeetings() {
        List<Meeting> meet = new ArrayList<>();
        Connection c = null;
        Statement stmt = null;
        int id;
        try {
            c = dataSource.getConnection();
            stmt = c.createStatement();
            PreparedStatement pr = c.prepareStatement("select users.* from meetings inner join users on meetings.umail = users.mail where mid = ?;");
            ResultSet rs = stmt.executeQuery("select * from meeting;");
            while (rs.next()) {
                id = rs.getInt("id");
                Meeting meeting = new Meeting();
                meeting.setId(id);
                meeting.setName(rs.getString("name"));
                meeting.setAdmin(rs.getString("admin_mail"));
                meeting.setDate(rs.getString("date"));
                meeting.setTime(rs.getString("time"));
                meeting.setAdress(rs.getString("adress"));
                pr.setInt(1, id);
                ResultSet users = pr.executeQuery();
                while (users.next()) {
                    User user = new User();
                    user.setId(users.getString("id"));
                    user.setName(users.getString("name"));
                    user.setRole(users.getString("userrole"));
                    user.setMail(users.getString("mail"));
                    meeting.addUser(user);
                }
                meet.add(meeting);
            }
            c.commit();
            rs.close();
            stmt.close();
            c.close();
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }
        return meet;
    }

    @Override
    public List<Meeting> getUserMeetings(String uid) {
        List<Meeting> meet = new ArrayList<>();
        Connection c = null;
        int id = 0;
        try {
            c = dataSource.getConnection();
            PreparedStatement mr = c.prepareStatement("select * from meeting where id in (select meetings.mid from meetings where meetings.umail = ?);");
            PreparedStatement pr = c.prepareStatement("select users.* from meetings inner join users on meetings.umail = users.mail where mid = ?;");
            mr.setString(1,uid);
            ResultSet rs = mr.executeQuery();
            while (rs.next()) {
                id = rs.getInt("id");
                Meeting meeting = new Meeting();
                meeting.setId(id);
                meeting.setName(rs.getString("name"));
                meeting.setAdmin(rs.getString("admin_mail"));
                meeting.setDate(rs.getString("date"));
                meeting.setTime(rs.getString("time"));
                meeting.setAdress(rs.getString("adress"));
                pr.setInt(1, id);
                ResultSet users = pr.executeQuery();
                while (users.next()) {
                    User user = new User();
                    user.setId(users.getString("id"));
                    user.setName(users.getString("name"));
                    user.setRole(users.getString("userrole"));
                    user.setMail(users.getString("mail"));
                    meeting.addUser(user);
                }
                meet.add(meeting);
            }
            c.commit();
            rs.close();
            c.close();
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }
        return meet;
    }

    @Override
    public Meeting getMettById(String uid,int id) {
        Meeting meeting = new Meeting();
        boolean flag = false;
        try {
            Connection c = dataSource.getConnection();
            PreparedStatement stmt = c.prepareStatement("select * from meeting where id = ?;");
            PreparedStatement pr = c.prepareStatement("select users.* from meetings inner join users on meetings.umail = users.mail where mid = ?;");
            stmt.setInt(1, id);
            pr.setInt(1,id);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                meeting.setId(rs.getInt("id"));
                meeting.setName(rs.getString("name"));
                meeting.setAdmin(rs.getString("admin_mail"));
                meeting.setDate(rs.getString("date"));
                meeting.setTime(rs.getString("time"));
                meeting.setAdress(rs.getString("adress"));
                ResultSet users = pr.executeQuery();
                while (users.next()) {
                    User user = new User();
                    user.setId(users.getString("id"));
                    user.setName(users.getString("name"));
                    user.setRole(users.getString("userrole"));
                    user.setMail(users.getString("mail"));
                    if (user.getMail().equals(uid)){
                        flag=true;
                    }
                    meeting.addUser(user);
                }
            }
            rs.close();
            stmt.close();
            c.close();
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }
        if (flag) {
            return meeting;
        }
        else return null;
    }

    @Override
    public void addUser(int id, String umail) {
            try {
                Connection c = dataSource.getConnection();
                PreparedStatement pr = c.prepareStatement("insert into meetings (mid,umail) values (?,?);");
                pr.setInt(1, id);
                pr.setString(2, umail);
                pr.execute();
                pr.close();
                c.close();
            } catch (Exception e) {
                System.err.println(e.getClass().getName() + ": " + e.getMessage());
            }
//        Message message = new Message();
//        Message.Data data = new Message.Data();
//        data.setEvent("add_to_meeting");
//        data.setF("server");
//        data.setInfo(String.valueOf(id));
//        data.setMessage("You have been added to meeting! Tap to open.");
//        message.setData(data);
//        UserService userService = new UserService();
//        message.setTo(userService.findByMail(umail).getId());
//        try {
//            fb.sendMessage(message);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        //return flag;
    }

    @Override
    public void deleteUser(int id, String uid) {
        if (uid.length()>0 && id > 0){
            try{
                Connection c = dataSource.getConnection();
                PreparedStatement pr = c.prepareStatement("delete from meetings where mid = ? and umail = ?;");
                pr.setInt(1, id);
                pr.setString(2,uid);
                pr.execute();
                pr.close();
                c.close();
            }catch (Exception e){
                System.err.println(e.getClass().getName() + ": " + e.getMessage());
            }
        }
    }

    @Override
    public void update(Meeting meeting) {
        Meeting meeting1;
        if (meeting.getId()>0){
            try{
                Connection c=dataSource.getConnection();
                PreparedStatement pr = c.prepareStatement("update meeting set name = ? where id = ?;");
                pr.setString(1,meeting.getName());
                pr.setInt(2,meeting.getId());
                pr.executeQuery();
                pr.close();
                c.close();
            }catch (Exception e){
                System.err.println(e.getClass().getName() + ": " + e.getMessage());
            }
        }
        meeting1 = getMettById(ProfileCotroller.uid,meeting.getId());
        Message.Data data = new Message.Data();
        data.setF("server");
        data.setEvent("update_meeting");
        Message message = new Message();
        message.setData(data);
        sendTooAll(meeting1,message);
    }


    private void sendTooAll(Meeting meeting, Message message){
        if (meeting!=null) {
            List<User> users = meeting.getUsers();
            for (User user : users) {
                message.setTo(user.getId());
                try {
                    fb.sendMessage(message);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
