package com.memorynotfound.service;

import com.memorynotfound.config.DtSource;
import com.memorynotfound.model.User;
import com.memorynotfound.model.UserEvent;

import javax.sql.DataSource;
import java.sql.*;
import java.util.*;
import java.util.List;

public class UserService implements Uservice{

    private static String url = "jdbc:postgresql://localhost/paveldata";
    private static String user = "pavel";
    private static String password = "75237523";

    private DataSource dataSource = DtSource.getDts();

    public  List<User> getAll() {
        List<User> users = new ArrayList<>();
        Connection c = null;
        Statement stmt = null;
        try {
            c = dataSource.getConnection();
            stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery("select * from users;");
            while (rs.next()) {
                User user = new User();
                user.setId(rs.getInt("id"));
                user.setUsername(rs.getString("username"));
                user.setPassword(rs.getString("password"));
                user.setRole(rs.getString("userrole"));
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

    public List<UserEvent> getEvents(int id) {

        List<UserEvent> events = new ArrayList<>();
        Connection c = null;
        try {
            c = dataSource.getConnection();
            PreparedStatement stmt = c.prepareStatement("select * from events where id = ?;");
            stmt.setInt(1,id);
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

    public void addEvent(int id, UserEvent userEvent) {
        try {
            Connection c = dataSource.getConnection();


            PreparedStatement stmt = c.prepareStatement("INSERT INTO events(id, event) VALUES(?, ?);");
            stmt.setInt(1,id);
            stmt.setString(2, userEvent.getEvent());
            stmt.executeUpdate();

            c.commit();
            c.close();
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }
    }

    public List<User> getFriends(int id){
        List<User> friends = new ArrayList<>();
        try{
            Connection c = dataSource.getConnection();
            PreparedStatement pr = c.prepareStatement("select users.* from friends inner join users on friends.fid = users.id where uid = ?;");
            pr.setInt(1, id);
            ResultSet rs = pr.executeQuery();
            while (rs.next()){
                User user = new User();
                user.setId(rs.getInt("id"));
                user.setUsername(rs.getString("username"));
                user.setRole(rs.getString("userrole"));
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
    public void addFriend(int id, int fid) {
        try{
            Connection c = dataSource.getConnection();
            PreparedStatement pr = c.prepareStatement("insert into friends values(?,?)");
            pr.setInt(1,id);
            pr.setInt(2,fid);
            pr.execute();
        }catch (Exception e){
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }
    }

    @Override
    public void deleteFriend(int id, int fid) {
        try{
            Connection c = dataSource.getConnection();
            PreparedStatement pr = c.prepareStatement(" delete from friends where uid = ? and fid = ?;");
            pr.setInt(1, id);
            pr.setInt(2,fid);
            pr.execute();
        }catch (Exception e){
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }
    }

    public User findById(int id) {
        Connection c = null;
        User user1 = new User();
        try {
            c = dataSource.getConnection();
            PreparedStatement stmt = c.prepareStatement("select * from users where id = ?;");
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                user1.setId(rs.getInt("id"));
                user1.setUsername(rs.getString("username"));
                user1.setPassword(rs.getString("password"));
                user1.setRole(rs.getString("userrole"));
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
            PreparedStatement stmt = c.prepareStatement("select * from users where username = ?;");
            stmt.setString(1, name);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                User user =new User();
                user.setId(rs.getInt("id"));
                user.setUsername(rs.getString("username"));
                user.setPassword(rs.getString("password"));
                user.setRole(rs.getString("userrole"));
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

    public void create(User user1) {
        try {
            Connection c = dataSource.getConnection();
            PreparedStatement stmt = c.prepareStatement("INSERT INTO users(username, password, userrole) VALUES(?, ?, ?);");
            stmt.setString(1, user1.getUsername());
            stmt.setString(2, user1.getPassword());
            stmt.setString(3, user1.getRole());
            stmt.executeUpdate();

            c.commit();
            c.close();
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }
    }

    public void delete(int id) {
        try {
            Connection c = dataSource.getConnection();
            PreparedStatement stmt = c.prepareStatement("delete from users where id = ?;");
            stmt.setInt(1, id);
            stmt.execute();
            stmt.close();
            c.commit();
            c.close();
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }
    }

    public int auth(String username, String pass) {
        int i=0;
        try {
            Class.forName("org.postgresql.Driver");
            Connection c = DriverManager
                    .getConnection(url,
                            user, password);
            c.setAutoCommit(false);

            PreparedStatement stmt = c.prepareStatement("select * from users where username = ? and password = ?;");
            stmt.setString(1,username);
            stmt.setString(2,pass);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                i = rs.getInt("id");
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
