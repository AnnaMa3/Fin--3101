package org.example;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {

        TokenManager token = TokenManager.getInstance();
        String writeToken = token.getWriteToken();
        System.out.println("Write Token: " + writeToken);
        String readToken = token.getReadToken();
        System.out.println("Read Token: " + readToken);
    }
}