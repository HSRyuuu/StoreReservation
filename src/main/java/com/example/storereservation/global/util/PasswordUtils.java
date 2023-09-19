package com.example.storereservation.global.util;

import com.example.storereservation.global.exception.ErrorCode;
import com.example.storereservation.global.exception.MyException;
import org.springframework.security.crypto.bcrypt.BCrypt;


public class PasswordUtils {

    public static void validatePlainTextPassword(String pw1, String pw2){
        if(!pw1.equals(pw2)){
            throw new MyException(ErrorCode.PASSWORD_CHECK_ERROR);
        }
    }

    public static boolean equals(String plainText, String hashed){
        if(plainText == null || plainText.length() < 1){
            return false;
        }
        if(hashed == null || hashed.length() < 1){
            return false;
        }

        return BCrypt.checkpw(plainText, hashed);
    }
    public static String encPassword(String plainText){
        if(plainText == null || plainText.length() < 1){
            return "";
        }
        return BCrypt.hashpw(plainText, BCrypt.gensalt());
    }
}
