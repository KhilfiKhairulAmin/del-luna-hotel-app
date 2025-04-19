package com.delluna.dellunahotel.models;

public class Guest {
    private String guestId;
    private String email;
    private String name;
    private String phone;
    private String gender;
    private String personality;
    private String securityQuestion;
    private String hashPassword;
    private String hashSecurityAnswer;

    public Guest(String guestId, String email, String name, String phone, 
                String gender, String personality, String securityQuestion,
                String hashPassword, String hashSecurityAnswer) {
        this.guestId = guestId;
        this.email = email;
        this.name = name;
        this.phone = phone;
        this.gender = gender;
        this.personality = personality;
        this.securityQuestion = securityQuestion;
        this.hashPassword = hashPassword;
        this.hashSecurityAnswer = hashSecurityAnswer;
    }

    // Getters and Setters for all fields
    public String getGuestId() { return guestId; }
    public void setGuestId(String guestId) { this.guestId = guestId; }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    
    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }
    
    public String getPersonality() { return personality; }
    public void setPersonality(String personality) { this.personality = personality; }
    
    public String getSecurityQuestion() { return securityQuestion; }
    public void setSecurityQuestion(String securityQuestion) { this.securityQuestion = securityQuestion; }
    
    public String getHashPassword() { return hashPassword; }
    public void setHashPassword(String hashPassword) { this.hashPassword = hashPassword; }
    
    public String getHashSecurityAnswer() { return hashSecurityAnswer; }
    public void setHashSecurityAnswer(String hashSecurityAnswer) { this.hashSecurityAnswer = hashSecurityAnswer; }

    @Override
    public String toString() {
        return "Guest{" +
                "guestId='" + guestId + '\'' +
                ", email='" + email + '\'' +
                ", name='" + name + '\'' +
                ", phone='" + phone + '\'' +
                ", gender='" + gender + '\'' +
                ", personality='" + personality + '\'' +
                ", securityQuestion='" + securityQuestion + '\'' +
                '}'; // Excludes password and security answer for security
    }
}