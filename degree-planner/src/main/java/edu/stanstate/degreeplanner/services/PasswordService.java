package edu.stanstate.degreeplanner.services;

public class PasswordService {
    public static boolean verify(String plainPassword, String storedPassword) {
        return storedPassword != null && storedPassword.equals(plainPassword);
    }
}