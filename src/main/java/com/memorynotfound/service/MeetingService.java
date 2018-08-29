package com.memorynotfound.service;


import com.memorynotfound.config.DtSource;
import com.memorynotfound.controller.ProfileCotroller;
import com.memorynotfound.model.Meeting;
import com.memorynotfound.model.User;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class MeetingService implements Mservice {

    Uservice uservice = new UserService();
    DataSource dataSource = DtSource.getDts();

    @Override
    public Meeting createMeeting(Meeting meeting) {
        Meeting returnMeeting=new Meeting();
        try{
            Connection c = dataSource.getConnection();
            PreparedStatement pr = c.prepareStatement("insert into meeting (name, admin_mail) values (?,?);");
            pr.setString(1,meeting.getName());
            pr.setString(2,meeting.getAdmin());
            ResultSet rs = pr.executeQuery();
            while (rs.next()){
                returnMeeting.setId(rs.getInt("id"));
                returnMeeting.setName(rs.getString("name"));
                returnMeeting.setAdmin(rs.getString("admin_mail"));
            }
            pr = c.prepareStatement("insert into meetings values (?,?);");
            pr.setInt(1,returnMeeting.getId());
            pr.setString(2,meeting.getAdmin());
            pr.execute();
            pr.cancel();
            c.close();
        }catch (Exception e){
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }
        return returnMeeting;
    }

    @Override
    public List<Meeting> getAllMeeting() {
        List<Meeting> meet = new ArrayList<>();
        Connection c = null;
        Statement stmt = null;
        int id;
        try {
            c = dataSource.getConnection();
            stmt = c.createStatement();
            PreparedStatement pr = c.prepareStatement("select users.* from meetings inner join users on meetings.uid = users.id where mid = ?;");
            ResultSet rs = stmt.executeQuery("select * from meeting;");
            while (rs.next()) {
                id = rs.getInt("id");
                Meeting meeting = new Meeting();
                meeting.setId(id);
                meeting.setName(rs.getString("name"));
                meeting.setAdmin(rs.getString("admin_mail"));
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
                ResultSet users = pr.executeQuery();
                while (users.next()) {
                    User user = new User();
                    user.setId(users.getString("id"));
                    user.setName(users.getString("name"));
                    user.setRole(users.getString("userrole"));
                    if (user.getId()==uid){
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
    public void addUser(int id, String uid) {
        if (uid.length()>0 && id>0) {
            try {
                Connection c = dataSource.getConnection();
                PreparedStatement pr = c.prepareStatement("insert into meetings(mid,umail) values (?,?);");
                pr.setInt(1, id);
                pr.setString(2,uid);
                pr.execute();
                pr.close();
                c.close();
            } catch (Exception e) {
                System.err.println(e.getClass().getName() + ": " + e.getMessage());
            }
        }
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
    public Meeting update(Meeting meeting) {
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
        return meeting1;
    }
}
