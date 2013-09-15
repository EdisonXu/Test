/*------------------------------------------------------------------------------
 * COPYRIGHT Ericsson 2012
 *
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 *----------------------------------------------------------------------------*/
package com.ericsson.bmsc.oam.config.util;


import java.math.BigInteger;

import javax.crypto.Cipher;

import javax.crypto.spec.SecretKeySpec;

import com.ericsson.bmsc.common.constant.ErrorCode;
import com.ericsson.bmsc.oam.logging.BmscLogger;

/**
 * the utility class used to decode the keystore password
 *
 * @author Ericsson MTV Team
 * @since Aug 3, 2012
 */
public final class SystemCommandUtil {

    private static final String KEY = "jaas is the way";
    
    public static String decodePassword(String password) {
        if (password == null || password.isEmpty()) {
            return "";
        }
        
        return decode(password);
        
    }
    
    public static String encode(String secret) {
        try {
            byte[] kbytes = "jaas is the way".getBytes();
            SecretKeySpec key = new SecretKeySpec(kbytes, "Blowfish");

            Cipher cipher = Cipher.getInstance("Blowfish");
            cipher.init(1, key);
            byte[] encoding = cipher.doFinal(secret.getBytes());
            BigInteger n = new BigInteger(encoding);
            return n.toString(16);
        } catch(Exception e) {
            return secret;
        }
     }
    
    public static String decode(String secret) {
        
        try {
            byte[] kbytes = KEY.getBytes();
            SecretKeySpec key = new SecretKeySpec(kbytes, "Blowfish");

            BigInteger n = new BigInteger(secret, 16);
            byte[] encoding = n.toByteArray();

            if (encoding.length % 8 != 0)
            {
              int length = encoding.length;
              int newLength = (length / 8 + 1) * 8;
              int pad = newLength - length;
              byte[] old = encoding;
              encoding = new byte[newLength];
              for (int i = old.length - 1; i >= 0; --i)
              {
                encoding[(i + pad)] = old[i];
              }
            }

            Cipher cipher = Cipher.getInstance("Blowfish");
            cipher.init(2, key);
            byte[] decode = cipher.doFinal(encoding);
            return new String(decode);
          } catch(Exception e) {
              BmscLogger.eventError(BmscLogger.SYS_CMD,
                  ErrorCode.INVALID_KEYSTORE_PASSWD, secret);
              return "";
          }
    }
}
