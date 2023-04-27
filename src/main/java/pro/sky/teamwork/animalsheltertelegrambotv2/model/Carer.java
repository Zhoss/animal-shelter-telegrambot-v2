package pro.sky.teamwork.animalsheltertelegrambotv2.model;

public class Carer {
    private long id;
    private String fullName;
    private int birthYear;
    private String phoneNumber;
    private long chatId;
    private String passportNumber;

    public Carer() {
    }

    public long getChatId() {
        return chatId;
    }

    public void setChatId(long chatId) {
        this.chatId = chatId;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        if (!fullName.isEmpty() && !fullName.isBlank()) {
            this.fullName = fullName;
        } else {
            throw new IllegalArgumentException("Требуется указать корректное ФИО опекуна");
        }
    }

    public int getBirthYear() {
        return birthYear;
    }

    public void setBirthYear(int birthYear) {
        this.birthYear = birthYear;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        if (!phoneNumber.isEmpty() && !phoneNumber.isBlank()) {
            this.phoneNumber = phoneNumber;
        } else {
            throw new IllegalArgumentException("Требуется указать корректный номер телефона опекуна");
        }
    }

    public String getPassportNumber() {
        return passportNumber;
    }

    public void setPassportNumber(String passportNumber) {
        if (passportNumber != null && !passportNumber.isEmpty() && !passportNumber.isBlank()) {
            this.passportNumber = passportNumber;
        } else {
            throw new IllegalArgumentException("Требуется указать корректный номер паспорта опекуна");
        }
    }
}
