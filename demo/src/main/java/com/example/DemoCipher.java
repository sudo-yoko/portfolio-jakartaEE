package com.example;

import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

/**
 * 暗号化と複合を行うユーティリティクラス
 */
public class DemoCipher {
    private static final String ALGORITHM = "AES";

    /**
     * 暗号化する
     * 
     * @param plaintext 平文
     * @return 暗号文(暗号化済みのbyte配列をBase64エンコードした文字列)
     */
    public static String encrypt(String plaintext) {
        // AES鍵を取得
        String aesKey = "16byte128bit----";
        SecretKeySpec secretKey = new SecretKeySpec(aesKey.getBytes(StandardCharsets.UTF_8), ALGORITHM);

        byte[] unencrypted = null;
        try {
            // 暗号化モードに初期化
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);

            // 平文をbyte配列にする
            unencrypted = plaintext.getBytes(StandardCharsets.UTF_8);

            // 暗号化する
            byte[] encrypted = cipher.doFinal(unencrypted);

            // 暗号化後のbyte配列をBase64エンコードして文字列の形式にする
            return Base64.getEncoder().encodeToString(encrypted);

        } catch (NoSuchAlgorithmException | NoSuchPaddingException | IllegalBlockSizeException | BadPaddingException
                | InvalidKeyException e) {
            throw new RuntimeException(e);

        } finally {
            // 平文のbyte配列を、GCに依存せずに直ちにクリアする
            if (unencrypted != null) {
                clearBytes(unencrypted);
            }
        }
    }

    public static String decrypt(String ciphertext) {
        // AES鍵を取得
        String aesKey = "16byte128bit----";
        SecretKeySpec secretKey = new SecretKeySpec(aesKey.getBytes(StandardCharsets.UTF_8), ALGORITHM);
        byte[] unencrypted = null;

        try {
            // 複合モードに初期化
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, secretKey);

            // 暗号文をBase64でコードする
            byte[] encrypted = Base64.getDecoder().decode(ciphertext);

            // 復号する
            unencrypted = cipher.doFinal(encrypted);

            // byte配列を文字列に変換する
            return new String(unencrypted, StandardCharsets.UTF_8);

        } catch (NoSuchAlgorithmException | NoSuchPaddingException | IllegalBlockSizeException | BadPaddingException
                | InvalidKeyException e) {
            throw new RuntimeException(e);

        } finally {
            // 平文のbyte配列を、GCに依存せずに直ちにクリアする
            if (unencrypted != null) {
                clearBytes(unencrypted);
            }
        }
    }

    /**
     * byte配列の内容をクリアする。すべての要素に0を設定する
     */
    private static void clearBytes(byte[] bytes) {
        if (bytes != null) {
            for (int i = 0; i < bytes.length; i++) {
                bytes[i] = 0;
            }
        }
    }
}
