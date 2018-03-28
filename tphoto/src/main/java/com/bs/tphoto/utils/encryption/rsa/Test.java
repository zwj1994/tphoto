package com.bs.tphoto.utils.encryption.rsa;

import com.bs.tphoto.utils.encryption.rsa.RSACoder;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Map;

public class Test {
	public static void main(String[] args) {
		Map<String, Object> keyMap;
				try {
					keyMap = RSACoder.initKey();
					String publicKey = RSACoder.getPublicKey(keyMap);
					String privateKey = RSACoder.getPrivateKey(keyMap);
//			String inputStr = "朱文杰";
//			byte[] data = inputStr.getBytes();
//
//			byte[] encodedData = RSACoder.encryptByPublicKey(data, publicKey);
//
//			byte[] decodedData = RSACoder.decryptByPrivateKey(encodedData,privateKey);
//
//			String outputStr = new String(decodedData);
//			System.err.println("加密前: " + inputStr + "\n\r" + "解密后: " + outputStr);
//			
//			assertEquals(inputStr, outputStr);
			
			
			
			System.err.println("私钥加密——公钥解密");
			String inputStr = "sign";
			byte[] data = inputStr.getBytes();

			byte[] encodedData = RSACoder.encryptByPrivateKey(data, privateKey);

			byte[] decodedData = RSACoder
					.decryptByPublicKey(encodedData, publicKey);

			String outputStr = new String(decodedData);
			System.err.println("加密前: " + inputStr + "\n\r" + "解密后: " + outputStr);
			assertEquals(inputStr, outputStr);

			System.err.println("私钥签名——公钥验证签名");
			// 产生签名
			String sign = RSACoder.sign(encodedData, privateKey);
			System.err.println("签名:\r" + sign);
			// 验证签名
			boolean status = RSACoder.verify(encodedData, publicKey, sign);
			System.err.println("状态:\r" + status);
			assertTrue(status);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
}
