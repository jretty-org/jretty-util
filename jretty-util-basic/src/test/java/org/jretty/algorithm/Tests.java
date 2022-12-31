package org.jretty.algorithm;

import java.security.MessageDigest;
import java.util.Random;

import org.jretty.util.AlgorithmUtils;

/**
 * 
 * @author zollty
 * @since 2022年11月24日
 */
public class Tests {
    
    //@org.junit.Test
    public void test(){
        sha1CryptTest();
    }
    
    void sha1CryptTest() {
        RandomHan rh = new RandomHan();
        for(int i=10; i<100; i++) {
            String s = rh.randomHanString(i);
            org.junit.Assert.assertEquals(AlgorithmUtils.sha1Crypt(s), getSha1(s));
            org.junit.Assert.assertEquals(AlgorithmUtils.sha1Crypt(s), toUserPwd(s));
            System.out.println(s);
        }
        
    }
    
    class RandomHan { // 随机汉字
        Random ran = new Random();
        int delta = 0x9fa5 - 0x4e00 + 1;
        char getRandomHan() {
            return (char) (0x4e00 + ran.nextInt(delta));
        }
        String randomHanString(int len) {
            StringBuilder sbu = new StringBuilder();
            for (int i = 0; i < len; i++) {
                sbu.append(getRandomHan());
            }
            return sbu.toString();
        }
    }
    
    
    static String toUserPwd(final String password) {
        try {
         if (password == null) {
          return null;
         }
         final MessageDigest messageDigest = MessageDigest.getInstance("SHA1");
         final byte[] digests = messageDigest.digest(password.getBytes("UTF-8"));
         final StringBuilder stringBuilder = new StringBuilder();
         for (int i = 0; i < digests.length; i++) {
          int halfbyte = (digests[i] >>> 4) & 0x0F;
          for (int j = 0; j <= 1; j++) {
           stringBuilder.append(
             ((0 <= halfbyte) && (halfbyte <= 9))
               ? (char) ('0' + halfbyte)
               : (char) ('a' + (halfbyte - 10)));
           halfbyte = digests[i] & 0x0F;
          }
         }
         return stringBuilder.toString();
        } catch (final Throwable throwable) {
         return null;
        }
       }


    static String getSha1(String str) {
        char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
        try {
            MessageDigest mdTemp = MessageDigest.getInstance("SHA1");
            mdTemp.update(str.getBytes("UTF-8"));
            byte[] md = mdTemp.digest();
            int j = md.length;
            char buf[] = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; i++) {
                byte byte0 = md[i];
                buf[k++] = hexDigits[byte0 >>> 4 & 0xf];
                buf[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(buf);
        } catch (Exception e) {
            return null;
        }
    }

}
