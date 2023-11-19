package by.belstu.lab02.services;

import by.belstu.lab02.models.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

public class UserDetailsImpl implements UserDetails {

    private static final long serialVersionUID = 1L;

    private Integer id;

    private String email;
    @JsonIgnore
    private String password;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singleton(authorities);
    }

    private GrantedAuthority authorities;

    public UserDetailsImpl(int id, String email, String password,
                           GrantedAuthority authorities) {
        this.id = id;

        this.email = email;
        this.password = password;
        this.authorities = authorities;
    }

    public static UserDetailsImpl build(User user) {
        GrantedAuthority authorities = new SimpleGrantedAuthority(user.getRoles().getName().name());


        return new UserDetailsImpl(
                user.getId(),
                user.getLogin(),
                user.getPassword(),
                authorities);
    }


    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }


    public int getId() {
        return id;
    }


    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((null == id) ? 0 : id.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        UserDetailsImpl other = (UserDetailsImpl) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }
}