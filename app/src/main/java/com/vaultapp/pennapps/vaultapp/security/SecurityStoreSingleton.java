package com.vaultapp.pennapps.vaultapp.security;

import android.security.KeyPairGeneratorSpec;
import android.util.Log;

import com.vaultapp.pennapps.vaultapp.MainActivity;
import com.vaultapp.pennapps.vaultapp.R;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.UnrecoverableEntryException;
import java.security.cert.CertificateException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.Calendar;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.NoSuchPaddingException;
import javax.security.auth.x500.X500Principal;

import static android.content.ContentValues.TAG;


/**
 * Created by hhbhagat on 1/21/2017.
 */

public class SecurityStoreSingleton {

    public static SecurityStoreSingleton store = null;
    private RSAPublicKey PubKey = null;

    private SecurityStoreSingleton() {
    }

    public static SecurityStoreSingleton getInstance() {
        if (store == null) {
            store = new SecurityStoreSingleton();
        }
        return store;
    }

    public RSAPrivateKey EnsureStore() throws KeyStoreException, NoSuchAlgorithmException, CertificateException, IOException {
        try {
            KeyStore keyStore = KeyStore.getInstance("AndroidKeyStore");
            keyStore.load(null);

            String alias = MainActivity.ctx.getResources().getString(R.string.PKIName);

            int nBefore = keyStore.size();
            // Create the keys if necessary
            if (!keyStore.containsAlias(alias)) {

                Calendar notBefore = Calendar.getInstance();
                Calendar notAfter = Calendar.getInstance();
                notAfter.add(Calendar.YEAR, 1);
                KeyPairGeneratorSpec spec = new KeyPairGeneratorSpec.Builder(MainActivity.ctx)
                        .setAlias(alias)
                        .setKeyType("RSA")
                        .setKeySize(1024)
                        .setSubject(new X500Principal("CN=AndroidPKI"))
                        .setSerialNumber(BigInteger.ONE)
                        .setStartDate(notBefore.getTime())
                        .setEndDate(notAfter.getTime())
                        .build();
                KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA", "AndroidKeyStore");
                generator.initialize(spec);

                KeyPair keyPair = generator.generateKeyPair();
            }
            int nAfter = keyStore.size();

            KeyStore.PrivateKeyEntry privateKeyEntry = (KeyStore.PrivateKeyEntry) keyStore.getEntry(alias, null);
            RSAPrivateKey privateKey = (RSAPrivateKey) privateKeyEntry.getPrivateKey();
            PubKey = (RSAPublicKey) privateKeyEntry.getCertificate().getPublicKey();
            return privateKey;
        } catch (NoSuchAlgorithmException e) {
            Log.e(TAG, Log.getStackTraceString(e));
        } catch (NoSuchProviderException e) {
            Log.e(TAG, Log.getStackTraceString(e));
        } catch (InvalidAlgorithmParameterException e) {
            Log.e(TAG, Log.getStackTraceString(e));
        } catch (KeyStoreException e) {
            Log.e(TAG, Log.getStackTraceString(e));
        } catch (CertificateException e) {
            Log.e(TAG, Log.getStackTraceString(e));
        } catch (IOException e) {
            Log.e(TAG, Log.getStackTraceString(e));
        } catch (UnrecoverableEntryException e) {
            Log.e(TAG, Log.getStackTraceString(e));
        } catch (UnsupportedOperationException e) {
            Log.e(TAG, Log.getStackTraceString(e));
        }
        return null;
    }

    public boolean Store(String str) {
        // Encrypt the text
        try {
            if (PubKey == null) {
                EnsureStore();
            }
            String encryptedDataFilePath = MainActivity.appDataDir + File.separator + "PKI";
            Cipher inCipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            inCipher.init(Cipher.ENCRYPT_MODE, PubKey);

            CipherOutputStream cipherOutputStream =
                    new CipherOutputStream(
                            new FileOutputStream(encryptedDataFilePath), inCipher);
            cipherOutputStream.write(str.getBytes("UTF-8"));
            cipherOutputStream.close();
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public String Retreive() {

        Cipher outCipher = null;
        try {
            outCipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        }
        try {
            outCipher.init(Cipher.DECRYPT_MODE, EnsureStore());
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (KeyStoreException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (CertificateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        String encryptedDataFilePath = MainActivity.appDataDir + File.separator + "PKI";
        CipherInputStream cipherInputStream = null;
        try {
            cipherInputStream = new CipherInputStream(new FileInputStream(encryptedDataFilePath), outCipher);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        byte[] byteArr = new byte[5000];

        int index = 0;
        int nextByte;
        try {
            while ((nextByte = cipherInputStream.read()) != -1) {
                byteArr[index] = (byte) nextByte;
                index++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            return new String(byteArr, 0, index, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return "";
    }
}
