package com.sidorovich.pavel.buber.test;

public class BankAccount {

    private int money;

    public BankAccount(int money) {
        this.money = money;
    }

    public void credit(int sum) {
        money += sum;
    }

    public void debit(int sum) {
        money -= sum;
    }

    public int getBalance() {
        return money;
    }

    @Override
    public String toString() {
        return "BankAccount{" +
               "money=" + money +
               '}';
    }
}

//        BankAccount account1 = new BankAccount(0);
//        BankAccount account2 = new BankAccount(30);
//        Thread thread1 = new Thread(() -> Transaction.INSTANCE.transferMoney(account1, account2, 10));
//        Thread thread2 = new Thread(() -> Transaction.INSTANCE.transferMoney(account2, account1, 15));
//        thread1.start();
//        thread2.start();
//
//        thread2.join();
//        thread1.join();
//
//        System.out.println(account1);
//        System.out.println(account2);