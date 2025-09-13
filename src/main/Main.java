package main;

import service.LedgerService;

public class Main {
    public static void main(String[] args) {
        LedgerService ledgerService = new LedgerService();
        ledgerService.runDemo();
    }
}
