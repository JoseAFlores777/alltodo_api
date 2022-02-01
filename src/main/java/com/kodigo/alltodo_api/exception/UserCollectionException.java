package com.kodigo.alltodo_api.exception;

public class UserCollectionException extends Exception {

    private static final long serialVersionUID = 1L;

    public UserCollectionException(String message) {
        super(message);
    }

    public static String NotFoundException( String id ){
        return "User with "+id+" not found";
    }

    public static  String UserAlreadyExist(){
        return "User with given email already exists";
    }
}
