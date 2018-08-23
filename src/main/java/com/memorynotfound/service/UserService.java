package com.memorynotfound.service;

import com.memorynotfound.config.DtSource;
import com.memorynotfound.model.User;
import com.memorynotfound.model.UserEvent;

import javax.sql.DataSource;
import java.sql.*;
import java.util.*;
import java.util.List;

public class UserService implements Uservice{


    private DataSource dataSource = DtSource.getDts();

    public  List<User> getAll() {
        List<User> users = new ArrayList<>();
        Statement stmt = null;
        Connection c = null;
        try {
            c= DtSource.getDts().getConnection();
            stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery("select * from users;");
            while (rs.next()) {
                User user1 = new User();
                user1.setId(rs.getString("id"));
                user1.setName(rs.getString("name"));
                user1.setPassword(rs.getString("password"));
                user1.setRole(rs.getString("userrole"));
                user1.setMail(rs.getString("mail"));
                users.add(user1);
            }
            rs.close();
            stmt.close();
            c.close();
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }
        return users;
    }


    public List<UserEvent> getEvents(String id) {

        List<UserEvent> events = new ArrayList<>();
        Connection c = null;
        try {
            c = dataSource.getConnection();
            PreparedStatement stmt = c.prepareStatement("select * from events where id = ?;");
            stmt.setString(1,id);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                UserEvent event = new UserEvent();
                event.setEvent(rs.getString("event"));
                events.add(event);
            }
            rs.close();
            stmt.close();
            c.close();
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }
        return events;
    }

    public void addEvent(String id, UserEvent userEvent) {
        try {
            Connection c = dataSource.getConnection();


            PreparedStatement stmt = c.prepareStatement("INSERT INTO events(id, event) VALUES(?, ?);");
            stmt.setString(1,id);
            stmt.setString(2, userEvent.getEvent());
            stmt.executeUpdate();

            c.commit();
            c.close();
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }
    }

    public List<User> getFriends(String id){
        List<User> friends = new ArrayList<>();
        try{
            Connection c = dataSource.getConnection();
            PreparedStatement pr = c.prepareStatement("select users.* from friends inner join users on friends.fid = users.id where uid = ?;");
            pr.setString(1, id);
            ResultSet rs = pr.executeQuery();
            while (rs.next()){
                User user = new User();
                user.setId(rs.getString("id"));
                user.setName(rs.getString("name"));
                user.setRole(rs.getString("userrole"));
                user.setMail(rs.getString("mail"));
                friends.add(user);
            }
            pr.close();
            c.close();
        }catch (Exception e){
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }
        return friends;
    }

    @Override
    public void addFriend(String id, String fid) {
        try{
            Connection c = dataSource.getConnection();
            PreparedStatement pr = c.prepareStatement("insert into friends values(?,?)");
            pr.setString(1,id);
            pr.setString(2,fid);
            pr.execute();
        }catch (Exception e){
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }
    }

    @Override
    public void deleteFriend(String id, String fid) {
        try{
            Connection c = dataSource.getConnection();
            PreparedStatement pr = c.prepareStatement(" delete from friends where uid = ? and fid = ?;");
            pr.setString(1, id);
            pr.setString(2,fid);
            pr.execute();
        }catch (Exception e){
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }
    }

    public User findById(String id) {
        Connection c = null;
        User user1 = new User();
        try {
            c = dataSource.getConnection();
            PreparedStatement stmt = c.prepareStatement("select * from users where id = ?;");
            stmt.setString(1, id);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                user1.setId(rs.getString("id"));
                user1.setName(rs.getString("name"));
                user1.setPassword(rs.getString("password"));
                user1.setRole(rs.getString("userrole"));
                user1.setMail(rs.getString("mail"));
            }
            rs.close();
            stmt.close();
            c.close();
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }
        return user1;
    }
    @Override
    public List<User> findByName(String name) {
        Connection c = null;
        List<User> users = new ArrayList<>();
        try {
            c = dataSource.getConnection();
            PreparedStatement stmt = c.prepareStatement("select * from users where name = ?;");
            stmt.setString(1, name);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                User user =new User();
                user.setId(rs.getString("id"));
                user.setName(rs.getString("name"));
                user.setPassword(rs.getString("password"));
                user.setRole(rs.getString("userrole"));
                user.setMail(rs.getString("mail"));
                users.add(user);
            }
            rs.close();
            stmt.close();
            c.close();
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }
        return users;
    }

    public boolean create(User user1) {
        try {
            Connection c = dataSource.getConnection();
            PreparedStatement stmt = c.prepareStatement("INSERT INTO users VALUES(?,?, ?, ?, ?);");
            stmt.setString(1,user1.getId());
            stmt.setString(2, user1.getName());
            stmt.setString(3, user1.getPassword());
            stmt.setString(4, user1.getRole());
            stmt.setString(5,user1.getMail());
            stmt.executeUpdate();
            c.commit();
            c.close();
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }
        return true;
    }

    public void delete(String id) {
        try {
            Connection c = dataSource.getConnection();
            PreparedStatement stmt = c.prepareStatement("delete from users where id = ?;");
            stmt.setString(1, id);
            stmt.execute();
            stmt.close();
            c.commit();
            c.close();
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }
    }

    public String auth(String username, String pass) {
        String url = "jdbc:postgresql://localhost/meeting";
        String user = "pavel";
        String password = "75237523";
        String i="";
        try {
            Class.forName("org.postgresql.Driver");
            Connection c = DriverManager
                    .getConnection(url,
                            user, password);
            c.setAutoCommit(false);

            PreparedStatement stmt = c.prepareStatement("select * from users where name = ? and password = ?;");
            stmt.setString(1,username);
            stmt.setString(2,pass);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                i = rs.getString("id");
            }
            rs.close();
            stmt.close();
            c.close();
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }
        return i;
    }
}
