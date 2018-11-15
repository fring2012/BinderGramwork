package com.example.fring.bindergramwork;

import com.example.processlib.annotion.ClassId;

@ClassId("com.example.fring.bindergramwork.UserManager")
public interface IUserManager {
    Person getPerson();

    void setPerson(Person person);
}
