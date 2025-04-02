package com.example;

/**
 * 暗号化と複合を行うユーティリティクラス
 */
public class DemoCipher {
    private static final String ALGORITHM = "AES";

    public static String encrypt(String plaintext) {
        // AES鍵を取得
        String aesKey = "16byte128bit----";
        SecretKeySpec secretKey = new SecretKeySpec(aesKey.getByte(StandardCharsets.UTF_8), ALGORITHM);

        byte[] unencrypted = null;
        //try{
         // 暗号化モードに初期化
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);

            // 平文をbyte配列にする
            unencrypted = plaintext.getBytes(StandardCharsets.UTF_8);

            // 暗号化する
            byte[] encrypted = cipher.doFinal(unencrypted);

            // 暗号化後のbyte配列をBase64エンコードして文字列の形式にする
            //return Base64.getEncoder().
       //}//
    }

}
