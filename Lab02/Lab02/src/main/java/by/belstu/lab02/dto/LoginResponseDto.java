package by.belstu.lab02.dto;

import jakarta.servlet.http.Cookie;

class UserDetails {
    String login;
    int id;
    public String getLogin() {
        return login;
    }
    public void setLogin(String login) {
        this.login = login;
    }
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

}

public class LoginResponseDto {
    private boolean success;
    private String message;
    private String token;
    private UserDetails user;
    public boolean isSuccess() {
        return success;
    }
    public void setSuccess(boolean success) {
        this.success = success;
    }
    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }
    public String getToken() {
        return token;
    }
    public void setToken(String token) {
        this.token = token;
    }
    public UserDetails getUser() {
        return user;
    }

    public void setUser(String username, Integer id) {
        this.user = new UserDetails();
        this.user.setLogin(username);
        this.user.setId(id);
    }

}