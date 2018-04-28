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
                meeting.setAdmin(rs.getInt("admin"));
                pr.setInt(1, id);
                ResultSet users = pr.executeQuery();
                while (users.next()) {
                    User user = new User();
                    user.setId(users.getInt("id"));
                    user.setUsername(users.getString("username"));
                    user.setRole(users.getString("userrole"));
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
    public List<Meeting> getUserMeetings(int uid) {
        List<Meeting> meet = new ArrayList<>();
        Connection c = null;
        int id = 0;
        try {
            c = dataSource.getConnection();
            PreparedStatement mr = c.prepareStatement("select * from meeting where id in (select meetings.mid from meetings where meetings.uid = ?);");
            PreparedStatement pr = c.prepareStatement("select users.* from meetings inner join users on meetings.uid = users.id where mid = ?;");
            mr.setInt(1,uid);
            ResultSet rs = mr.executeQuery();
            while (rs.next()) {
                id = rs.getInt("id");
                Meeting meeting = new Meeting();
                meeting.setId(id);
                meeting.setName(rs.getString("name"));
                meeting.setAdmin(rs.getInt("admin"));
                pr.setInt(1, id);
                ResultSet users = pr.executeQuery();
                while (users.next()) {
                    User user = new User();
                    user.setId(users.getInt("id"));
                    user.setUsername(users.getString("username"));
                    user.setRole(users.getString("userrole"));
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
    public Meeting getMettById(int uid,int id) {
        Meeting meeting = new Meeting();
        boolean flag = false;
        try {
            Connection c = dataSource.getConnection();
            PreparedStatement stmt = c.prepareStatement("select * from meeting where id = ?;");
            PreparedStatement pr = c.prepareStatement("select users.* from meetings inner join users on meetings.uid = users.id where mid = ?;");
            stmt.setInt(1, id);
            pr.setInt(1,id);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                meeting.setId(rs.getInt("id"));
                meeting.setName(rs.getString("name"));
                meeting.setAdmin(rs.getInt("admin"));
                ResultSet users = pr.executeQuery();
                while (users.next()) {
                    User user = new User();
                    user.setId(users.getInt("id"));
                    user.setUsername(users.getString("username"));
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
    public void addUser(int id, int uid) {
        if (uid>0 && id>0) {
            try {
                Connection c = dataSource.getConnection();
                PreparedStatement pr = c.prepareStatement("insert into meetings(mid,uid) values (?,?);");
                pr.setInt(1, id);
                pr.setInt(2,uid);
                pr.execute();
                pr.close();
                c.close();
            } catch (Exception e) {
                System.err.println(e.getClass().getName() + ": " + e.getMessage());
            }
        }
    }

    @Override
    public void deleteUser(int id, int uid) {
        if (uid>0 && id > 0){
            try{
                Connection c = dataSource.getConnection();
                PreparedStatement pr = c.prepareStatement("delete from meetings where mid = ? and uid = ?;");
                pr.setInt(1, id);
                pr.setInt(2,uid);
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
