/*------------------------------------------------------------------------------
 * COPYRIGHT Ericsson 2012
 *
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 *----------------------------------------------------------------------------*/

package com.edi.test.restful;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.KeyStore;

import org.apache.http.conn.ssl.SSLSocketFactory;



/**
 * the helper class for generate customized SSLSocketFactory
 *
 * @author Ericsson MTV Team
 * @since Jul 26, 2012
 */
public final class SSLSocketFactoryHelper {

    private static String keyStorePath = "";
    private static String keyStorePass = "";
    private static String trustStorePath = "";
    private static String trustStorePass = "";
    private static KeyStore keyStore = null;
    private static KeyStore trustStore = null;
    private static SSLSocketFactory  trustFactory = null;
    private static SSLSocketFactory  bothFactory = null;

    private static KeyStore loadKeyStore(String path, String passwd) {
        KeyStore keyStore = null;
        FileInputStream keyStoreIn = null;
        try {
            keyStoreIn = new FileInputStream(new File(path));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
        
        try {
            keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
            keyStore.load(keyStoreIn,  SystemCommandUtil.decodePassword(passwd).toCharArray());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                keyStoreIn.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        
        return keyStore;
    }

    private static KeyStore getKeyStore(String path, String passwd) {
        if (keyStore == null) {
            if(!validatePathAndPasswd(path, passwd)) {
                System.out.println("path or password is empty");
                return keyStore;
            }
        }

         if (!keyStorePath.equalsIgnoreCase(path)
          || !keyStorePass.equalsIgnoreCase(passwd)) {
             keyStore = loadKeyStore(path, passwd);
             if(keyStore != null) {
                 keyStorePath = path;
                 keyStorePass = passwd;
             }
         }
        return keyStore;
    }

    private static boolean validatePathAndPasswd(String path, String passwd) {
        if (path == null || path.isEmpty() || passwd == null || passwd.isEmpty()) {
            return false;
        }

        return true;
    }

    private static KeyStore getTrustStore(String path, String passwd) {
        if (trustStore == null) {
            if(!validatePathAndPasswd(path, passwd)) {
                System.out.println("path or password is empty");

                return keyStore;
            }
        }

        if (!trustStorePath.equalsIgnoreCase(path)
         || !trustStorePass.equalsIgnoreCase(passwd)) {
            trustStore = loadKeyStore(path, passwd);
            if (trustStore != null) {
                trustStorePath = path;
                trustStorePass = passwd;
            }
        }

        return trustStore;
    }
    
    
    public static SSLSocketFactory getDefaultSSLSocketFactory(
                    String ksPath, 
                    String ksPass,
                    String tsPath,
                    String tsPass) {
        if (bothFactory == null) {
            KeyStore localTrustStore = getTrustStore(tsPath, tsPass);
            KeyStore localKeyStore = getKeyStore(ksPath, ksPass);
            if (localTrustStore == null || localKeyStore == null) {
                return bothFactory;
            }

            // the password is encrypted
            try {
                bothFactory = new SSLSocketFactory(localKeyStore, 
                        SystemCommandUtil.decodePassword(ksPass), localTrustStore);
                System.out.println("Create two-way SSLSocketFactory successfully from " + ksPath + " and " + tsPath);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return bothFactory;
    }
    
    public static SSLSocketFactory getTrustSSLSocketFactory(
                String tsPath,
                String tsPass) {
        if (trustFactory == null) {
            KeyStore localTrustStore = getTrustStore(tsPath, tsPass);
            if (localTrustStore == null) {
                return trustFactory;
            }

            try {
                trustFactory = new SSLSocketFactory(localTrustStore);
                System.out.println("Create trust SSLSocketFactory successfully from " + tsPath);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return trustFactory;
    }

}