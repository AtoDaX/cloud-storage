package edu.pet.cloudstorage.utils;


import edu.pet.cloudstorage.model.User;

public class Utils {
    public static String getUserDirectory(User user){
        return "user-"+user.getId()+"-files/";
    }
}
