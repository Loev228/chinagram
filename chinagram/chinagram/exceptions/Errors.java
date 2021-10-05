package ru.netcracker.chinagram.exceptions;

public class Errors {
    public static final String USER_WITH_ID_NOT_FOUND = "User with id = '%s' not found";
    public static final String USER_WITH_NAME_NOT_FOUND = "User with username = '%s' not found";
    public static final String USER_IS_NOT_VALID = "User with id = '%s' or username = '%s' already exists";
    public static final String USER_DOESNT_FOLLOW_USER_WITH_ID = "User (id = '%s') doesn't follow user with id = '%s'";
    public static final String USER_ALREADY_FOLLOWS_USER_WITH_ID = "User (id = '%s') already follows user with id = '%s'";
}
