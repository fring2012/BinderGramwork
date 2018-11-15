package com.example.fring.bindergramwork;

public class UserManager implements IUserManager{
    Person person;
    private volatile static UserManager sInstance = null;
    private UserManager(){

    }
    //getInstance 定了有个规则 单例对象getInstance 方法名
    public static UserManager getInstance() {
        if (sInstance == null) {
            synchronized (UserManager.class) {
                if (sInstance == null) {
                    sInstance = new UserManager();
                }
            }
        }
        return sInstance;
    }
    @Override
    public Person getPerson() {
        return person;
    }
    public void setPerson(Person person){
        this.person = person;
    }
}
