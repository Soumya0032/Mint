/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import dao.LoginDao;
import model.User;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

/**
 *
 * @author ROHAN
 */
@RestController
@RequestMapping("/")
public class LoginController {
    private static final String encryptionKey = "ABCDEFGHIJKLMNOP";
    private static final String characterEncoding = "UTF-8";
    private static final String cipherTransformation = "AES/CBC/PKCS5PADDING";
    private static final String aesEncryptionAlgorithem = "AES";
    
    private LoginDao loginDao;
    
    //login with fingerprint string 
    @ResponseBody
    @RequestMapping(value = "/login/{agentId}/{password}/{fingerprint}", method = RequestMethod.GET)
    public User validateUser(@PathVariable String agentId, @PathVariable String password, @PathVariable String fingerprint) {
        System.out.print("E : " + fingerprint);
        String decryptedFingerprint = decrypt(fingerprint);
       System.out.print("D : " + decryptedFingerprint);
        User message = null;
        try {
            message = loginDao.validateUser(agentId, password, decryptedFingerprint);
        } catch (Exception e) {
         message = new User();
        }
    return message;
    }
    
    @RequestMapping("/fingerprint/{imei}")
    public User getUserDetails(@PathVariable String imei){
        User user = loginDao.getUserDetails(imei);
        return user;
    }
    
    //login without fingerprint string 
    @ResponseBody
    @RequestMapping(value = "/login/{agentId}/{password}", method = RequestMethod.GET)
    public User validateUserCredential(@PathVariable String agentId, @PathVariable String password) {
       User message = null;
        try {
            message = loginDao.validateUserCredential(agentId, password);
        } catch (Exception e) {
         message = new User();
        }
    return message;
    }

    public void setLoginDao(LoginDao loginDao) {
        this.loginDao = loginDao;
    }
    
     public static String encrypt(String plainText) {
        String encryptedText = "";
        try {
            Cipher cipher   = Cipher.getInstance(cipherTransformation);
            byte[] key      = encryptionKey.getBytes(characterEncoding);
            SecretKeySpec secretKey = new SecretKeySpec(key, aesEncryptionAlgorithem);
            IvParameterSpec ivparameterspec = new IvParameterSpec(key);
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivparameterspec);
            byte[] cipherText = cipher.doFinal(plainText.getBytes("UTF8"));
            Base64.Encoder encoder = Base64.getEncoder();
            encryptedText = encoder.encodeToString(cipherText);

        } catch (Exception E) {
            System.err.println("Encrypt Exception : "+E.getMessage());
        }
        return encryptedText;
    }


    public static String decrypt(String encryptedText) {
        String decryptedText = "";
        try {
            Cipher cipher = Cipher.getInstance(cipherTransformation);
            byte[] key = encryptionKey.getBytes(characterEncoding);
            SecretKeySpec secretKey = new SecretKeySpec(key, aesEncryptionAlgorithem);
            IvParameterSpec ivparameterspec = new IvParameterSpec(key);
            cipher.init(Cipher.DECRYPT_MODE, secretKey, ivparameterspec);
            Base64.Decoder decoder = Base64.getDecoder();
            byte[] cipherText = decoder.decode(encryptedText.getBytes("UTF8"));
            decryptedText = new String(cipher.doFinal(cipherText), "UTF-8");

        } catch (Exception E) {
            System.err.println("decrypt Exception : "+E.getMessage());
        }
        return decryptedText;
    }
}
