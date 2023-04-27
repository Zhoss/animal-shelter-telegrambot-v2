package pro.sky.teamwork.animalsheltertelegrambotv2.model;

public enum ChatType {
    PRIVATE("Private"),
    GROUP("group");

    private final String chatTypeString;

    ChatType(String chatTypeString) {
        this.chatTypeString = chatTypeString;
    }

    public String getChatTypeString() {
        return chatTypeString;
    }

    public static ChatType findChatTypeByString(String command) {
        for (ChatType chatType : values()) {
            if (chatType.getChatTypeString().equals(command)) {
                return chatType;
            }
        }
        return null;
    }
}
