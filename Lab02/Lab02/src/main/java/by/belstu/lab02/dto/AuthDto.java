package by.belstu.lab02.dto;

public class AuthDto {
    private String login;
    private String password;

    public AuthDto() {
    }

    public AuthDto(String username, String password) {
        this.login = username;
        this.password = password;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


}
