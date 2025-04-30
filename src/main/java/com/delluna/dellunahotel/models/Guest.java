package com.delluna.dellunahotel.models;

public class Guest {
    public String guestId;
    public String email;
    public String fullName;
    public String phone;
    public String gender;
    public String personality;
    public String securityQuestion;
    public String hashPassword;
    public String hashSecurityAnswer;

    public Guest(String guestId, String email, String name, String phone, 
                String gender, String personality, String securityQuestion,
                String hashPassword, String hashSecurityAnswer) {
        this.guestId = guestId;
        this.email = email;
        this.fullName = name;
        this.phone = phone;
        this.gender = gender;
        this.personality = personality;
        this.securityQuestion = securityQuestion;
        this.hashPassword = hashPassword;
        this.hashSecurityAnswer = hashSecurityAnswer;
    }

    public Guest() {};
}
