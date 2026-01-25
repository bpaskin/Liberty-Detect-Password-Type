package com.ibm.example;

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
        }
    }
}
