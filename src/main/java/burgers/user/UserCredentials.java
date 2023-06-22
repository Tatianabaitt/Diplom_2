package burgers.user;

import lombok.Data;
import org.apache.commons.lang3.RandomStringUtils;

@Data
public class UserCredentials {
    private String email;
    private String password;

    public UserCredentials(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public UserCredentials(User user) {
        this.email = user.getEmail();
        this.password = user.getPassword();
    }

    public UserCredentials UserCredentialsBadEmail(UserCredentials creds) {
        creds.email = RandomStringUtils.randomAlphabetic(5).toLowerCase() + "@yandex.ru";
        return creds;
    }

    public UserCredentials UserCredentialsBadPassword(UserCredentials creds) {
        creds.email = RandomStringUtils.randomAlphabetic(10).toLowerCase();
        return creds;
    }

    public static UserCredentials from(User user) {
        return new UserCredentials(user);
    }
}
