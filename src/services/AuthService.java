package services;

// AuthService.java
import database.model.User;

public class AuthService {

    private static User authenticatedUser;

    public static void setAuthenticatedUser(User user) {
        authenticatedUser = user;
    }

    public static User getAuthenticatedUser() {
        return authenticatedUser;
    }

    public static String getUserRole() {
        return authenticatedUser != null ? authenticatedUser.getRole() : null;
    }

    public static boolean isAuthenticated() {
        return authenticatedUser != null;
    }

    public static void logout() {
        authenticatedUser = null; // Clear user data
    }
}
