package utils;

import org.bouncycastle.crypto.generators.Argon2BytesGenerator;
import org.bouncycastle.crypto.params.Argon2Parameters;

import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Base64;

public class PasswordUtils {

    // Function to hash the password using Argon2
    public static String hashPassword(String password) {
        // Generate a random salt
        byte[] salt = new byte[16];  // 16 bytes (128 bits)
        SecureRandom random = new SecureRandom();
        random.nextBytes(salt);

        // Hash the password with Argon2
        Argon2Parameters.Builder builder = new Argon2Parameters.Builder(Argon2Parameters.ARGON2_id)
                .withSalt(salt)
                .withParallelism(1) // Number of parallel threads
                .withMemoryAsKB(65536) // Memory cost in KB (higher is more secure but slower)
                .withIterations(3);    // Number of iterations (higher is more secure but slower)

        Argon2BytesGenerator generator = new Argon2BytesGenerator();
        generator.init(builder.build());

        byte[] hash = new byte[32];  // Output hash size (32 bytes = 256 bits)
        generator.generateBytes(password.toCharArray(), hash);

        // Encode the hash and salt in Base64 for storage
        String encodedHash = Base64.getEncoder().encodeToString(hash);
        String encodedSalt = Base64.getEncoder().encodeToString(salt);

        // Return the salt and hash combined in format "salt:hash"
        return encodedSalt + ":" + encodedHash;
    }

    // Function to verify password (this will be needed for login)
    public static boolean verifyPassword(String password, String storedHash) {
        // Split the stored hash into salt and hash parts
        String[] parts = storedHash.split(":");
        String encodedSalt = parts[0];
        String encodedHash = parts[1];

        // Decode the salt
        byte[] salt = Base64.getDecoder().decode(encodedSalt);

        // Hash the input password with the same salt
        Argon2Parameters.Builder builder = new Argon2Parameters.Builder(Argon2Parameters.ARGON2_id)
                .withSalt(salt)
                .withParallelism(1)
                .withMemoryAsKB(65536)
                .withIterations(3);

        Argon2BytesGenerator generator = new Argon2BytesGenerator();
        generator.init(builder.build());

        byte[] hash = new byte[32];
        generator.generateBytes(password.toCharArray(), hash);

        // Compare the newly hashed password with the stored hash
        String newEncodedHash = Base64.getEncoder().encodeToString(hash);
        return newEncodedHash.equals(encodedHash);
    }
}
