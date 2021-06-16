package common;

import server.utility.PasswordHasher;

import java.io.Serializable;
import java.util.Objects;

public class User implements Serializable {
    private String login;
    private String password;
    private String color;
    public User(String login, String password) {
        this.login = login;
        this.password = password;

    }

    public User(String login, String password,String color) {
        this.login = login;
        this.password = password;
        this.color=color;
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }
    public String getColor() {
        return color;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o instanceof User) {
            User userObj = (User) o;
            return login.equals(userObj.getLogin()) && password.equals(userObj.getPassword());
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(login, password);
    }

    @Override
    public String toString() {
        return "User: /n"+login + ":" + password;
    }
}
