package com.ibm.example;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

import com.ibm.websphere.crypto.PasswordUtil;
import com.ibm.ws.common.encoder.Base64Coder;

public class CryptoType {
    
    public static void main(String[] args) {
        System.out.println("Password Type Detection Utility");
        
        if (args.length != 1) {
            System.out.println("Usage: java crypoType <password_hash>");
            System.out.println("Example: java crypoType {xor}Lz4sLCgwLTs=");
            System.exit(1);
        }
        
        String passwordHash = args[0];
        System.out.println("Input: " + passwordHash);
        String detectedType = null;
    
        if (PasswordUtil.isValidCryptoAlgorithmTag(passwordHash)) {
            detectedType = PasswordUtil.getCryptoAlgorithm(passwordHash);
            System.out.println("Detected Type: " + detectedType);
        } else {
            System.err.println("Invalid Crypto Algorithm");
            System.exit(2);
        }

        if (detectedType.equalsIgnoreCase("aes")) {
            String password = PasswordUtil.removeCryptoAlgorithmTag(passwordHash);
            byte[] passwordBytes = Base64Coder.base64Decode(password.getBytes(StandardCharsets.UTF_8));

            if (null == passwordBytes) {
                System.err.println("Invalid aes password encryption");
                System.exit(3);
            }

            switch (passwordBytes[0]) {
                case 0: 
                    System.out.println("AES Encyption type: AES_V0 : AES-128");
                    break;
                case 1: 
                    System.out.println("AES Encyption type: AES_V1 : AES-256");
                    break;
                case 2: 
                    System.out.println("AES Encyption type: AES_V2 : AES-256");
                    break;
                default: 
                    System.err.println("AES Unknown Encryption");
                    System.exit(4);
            }

            try {
                // Capture System.err
                PrintStream originalErr = System.err;
                ByteArrayOutputStream errContent = new ByteArrayOutputStream();
                System.setErr(new PrintStream(errContent));
                PasswordUtil.passwordDecode(passwordHash);
                
                // Restore original streams
                System.setErr(originalErr);
                
                // captured output
                String capturedErr = errContent.toString();

                if (capturedErr.length() == 0) {
                    System.out.println("KEY: Built-in");
                } else {
                    System.err.println("KEY: External key");
                }
            } catch (Exception e) {
                e.printStackTrace();
                System.exit(5);
            }
        }
    }
}
