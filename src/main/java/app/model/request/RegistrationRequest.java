package app.model.request;

import lombok.Data;

@Data
public class RegistrationRequest {
    private String login;
    private String email;
    private String password;
}
