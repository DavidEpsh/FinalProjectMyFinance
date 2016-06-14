package dsme.myfinance.models;


import java.sql.Date;

public class MessageLocal {

    private String messageId;
    private String senderId;
    private String messageContent;
    private String recepeintId;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    private String date;

    public MessageLocal(String messageId, String senderId, String recepeintId, String messageContent, String date){
        this.messageId = messageId;
        this.senderId = senderId;
        this.messageContent = messageContent;
        this.recepeintId = recepeintId;
        this.date = date;
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getMessageContent() {
        return messageContent;
    }

    public void setMessageContent(String messageContent) {
        this.messageContent = messageContent;
    }

    public String getRecepeintId() {
        return recepeintId;
    }

    public void setRecepeintId(String recepeintId) {
        this.recepeintId = recepeintId;
    }


}
