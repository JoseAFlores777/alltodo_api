package com.kodigo.alltodo_api.exception;

public class ProjectCollectionException extends Exception {

    private static final long serialVersionUID = 1L;

    public ProjectCollectionException(String message) {
        super(message);
    }

    public static String NotFoundException( String id ){
        return "Project with "+id+" not found";
    }

    public static  String TodoAlreadyExist(){
        return "Project with given name already exists";
    }
}
