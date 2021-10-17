package com.sidorovich.pavel.buber.test;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public enum Transaction {
    INSTANCE;

    private final ReentrantLock lock;
    private final Condition sufficientFunds;

    Transaction() {
        this.lock = new ReentrantLock();
        this.sufficientFunds = lock.newCondition();
    }

    public void transferMoney(BankAccount from, BankAccount to, int sum) {
        lock.lock();
        try {
            while (from.getBalance() < sum) {
                sufficientFunds.await();
            }
            from.debit(sum);
            to.credit(sum);
            sufficientFunds.signalAll();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }
}
