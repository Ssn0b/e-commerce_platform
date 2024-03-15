package app.snob.ecommerce_platform.service;

import app.snob.ecommerce_platform.dto.UserResponse;

import java.util.List;
import java.util.UUID;

/**
 * The UserService interface provides methods to interact with user-related operations.
 */
public interface UserService {

    /**
     * Retrieves a list of users based on the provided parameters.
     *
     * @param name the name of the user (optional)
     * @param role the role of the user (optional)
     * @param page the page number for pagination
     * @param size the page size for pagination
     * @param sort the sorting criteria
     * @return a list of user responses
     */
    List<UserResponse> getAllUsers(String name, String role, int page, int size, String[] sort);

    /**
     * Retrieves a user by their unique identifier.
     *
     * @param id the unique identifier of the user
     * @return the user response
     */
    UserResponse getUserById(UUID id);

    /**
     * Retrieves a user by their email address.
     *
     * @param email the email address of the user
     * @return the user response
     */
    UserResponse getUserByEmail(String email);

    /**
     * Updates the role of a user identified by their unique identifier.
     *
     * @param id   the unique identifier of the user
     * @param role the new role to be assigned to the user
     */
    void updateUsersRole(UUID id, String role);

    /**
     * Deletes a user by their unique identifier.
     *
     * @param id the unique identifier of the user to be deleted
     */
    void deleteUserById(UUID id);
}
