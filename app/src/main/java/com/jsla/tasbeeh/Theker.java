package com.jsla.tasbeeh;

public class Theker {
    private String theker;
    private int counter;
    private boolean isRead = false;

    public Theker(String theker,int counter){
        this.theker = theker;
        this.counter = counter;
    }

    public Theker(){

    }

    public String getTheker() {
        return theker;
    }

    public void setTheker(String theker) {
        this.theker = theker;
    }

    public int getCounter() {
        return counter;
    }

    public void setCounter(int counter) {
        this.counter = counter;
    }

    public boolean isRead() {
        return isRead;
    }

    public void setRead(boolean read) {
        isRead = read;
    }
}
