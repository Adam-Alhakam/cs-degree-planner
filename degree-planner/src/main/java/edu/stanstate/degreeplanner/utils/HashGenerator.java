package edu.stanstate.degreeplanner.utils;

import org.mindrot.jbcrypt.BCrypt;

public class HashGenerator {
    public static void main(String[] args) {
        String password = "test123";
        String hash = BCrypt.hashpw(password, BCrypt.gensalt());
        System.out.println("Password: " + password);
        System.out.println("Hash: " + hash);
    }
}