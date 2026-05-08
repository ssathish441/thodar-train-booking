package com.sathish.thodar;

import com.sathish.thodar.features.auth.AuthView;

public class Main {

    public static final String VERSION_NAME = "2.0.1";

    public static void main(String[] args) {
        System.out.println("       Welcome to Thodar      ");
        System.out.println("=========================================");
        
        new AuthView().showLandingMenu();
    }
}