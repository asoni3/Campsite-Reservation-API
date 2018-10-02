package com.upgrade.rest.api;

public class MessageDTO {

    private String status;
    private String message;
    public MessageDTO(){

    }
    public MessageDTO(String status, String message){
        this.message = message;
        this.status = status;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }
}
