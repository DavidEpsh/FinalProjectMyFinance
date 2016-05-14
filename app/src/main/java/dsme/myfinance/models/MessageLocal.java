package dsme.myfinance.models;


public class MessageLocal {

    private long messageId;
    private long chatId;
    private String messageContent;
    private String sender;

    public MessageLocal(long messageId, long chatId, String messageContent, String sender){
        this.messageId = messageId;
        this.chatId = chatId;
        this.messageContent = messageContent;
        this.sender = sender;
    }

    public long getMessageId() {
        return messageId;
    }

    public void setMessageId(long messageId) {
        this.messageId = messageId;
    }

    public long getChatId() {
        return chatId;
    }

    public void setChatId(long chatId) {
        this.chatId = chatId;
    }

    public String getMessageContent() {
        return messageContent;
    }

    public void setMessageContent(String messageContent) {
        this.messageContent = messageContent;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }


}
