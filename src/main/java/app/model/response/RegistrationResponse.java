package app.model.response;

import lombok.Data;

@Data
public class RegistrationResponse {
    private String username;
    private String message;

    public RegistrationResponse() {
        this.message = "Account with that username or email already exists.";
    }

    public RegistrationResponse(String username) {
        this.username = username;
        this.message = "Account created successfully.";
    }
}
