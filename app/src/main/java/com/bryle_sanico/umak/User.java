package com.bryle_sanico.umak;

import androidx.annotation.NonNull;

import java.io.Serializable;

public class User implements Serializable {
    public Integer id, age;
    public String first_name, middle_name, last_name, address, contact_no, email;

    public User(int id, int age, String firstName, String middleName, String lastName, String address, String contactNo, String email) {
        this.id = id;
        this.age = age;
        this.first_name = firstName;
        this.middle_name = middleName;
        this.last_name = lastName;
        this.address = address;
        this.contact_no = contactNo;
        this.email = email;
    }

    @NonNull
    @Override
    public String toString() {
        return "User{id=" + id +
                ", first_name='" + first_name + '\'' +
                ", middle_name='" + middle_name + '\'' +
                ", last_name='" + last_name + '\'' +
                ", age='" + age + '\'' +
                ", address='" + address + '\'' +
                ", contact_no='" + contact_no + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
