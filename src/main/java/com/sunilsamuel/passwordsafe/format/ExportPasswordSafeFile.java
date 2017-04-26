package com.sunilsamuel.passwordsafe.format;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Map;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.ServletOutputStream;

import org.apache.commons.lang3.StringUtils;

public class ExportPasswordSafeFile extends ExportFile {

	public ExportPasswordSafeFile() {
	}

	@Override
	public void process(Map<String, Object> data) {
		this.data = data;
	}
	// http://stackoverflow.com/questions/6608529/how-to-use-cipheroutputstream-correctly-to-encrypt-and-decrypt-log-created-with

	@Override
	public void writeTo(ServletOutputStream stream) {
		String password = (String) data.get("password");
		password = properPassword(password);
		ObjectOutputStream oos;
		try {
			CipherOutputStream cstream = new CipherOutputStream(stream, getCipher(password, Cipher.ENCRYPT_MODE));
			oos = new ObjectOutputStream(cstream);
			oos.writeObject(data);
			oos.flush();
			oos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private Cipher getCipher(String password, int mode) {
		byte[] keyBytes = password.getBytes();
		final byte[] ivBytes = new byte[] { 0x00, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08, 0x09, 0x0a, 0x0b,
				0x0c, 0x0d, 0x0e, 0x0f }; // example

		try {

			final SecretKey key = new SecretKeySpec(keyBytes, "AES");
			final IvParameterSpec IV = new IvParameterSpec(ivBytes);
			final Cipher cipher = Cipher.getInstance("AES/CFB8/NoPadding");
			cipher.init(mode, key, IV);
			return cipher;
		} catch (InvalidKeyException | InvalidAlgorithmParameterException | NoSuchAlgorithmException
				| NoSuchPaddingException e) {
			e.printStackTrace();
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	public Map<String, Object> readFrom(InputStream stream, String password) {
		try {
			password = properPassword(password);
			CipherInputStream cstream = new CipherInputStream(stream, getCipher(password, Cipher.DECRYPT_MODE));
			ObjectInputStream ois = new ObjectInputStream(cstream);
			Map<String, Object> object = (Map<String, Object>) ois.readObject();
			ois.close();
			return object;
		} catch (ClassNotFoundException | IOException e) {
			e.printStackTrace();
			return null;
		}

	}

	public String properPassword(String password) {
		if (password.length() > 16) {
			return password.substring(0, 16);
		}
		if (password.length() == 16) {
			return password;
		}
		return StringUtils.rightPad(password, 16, password);
	}

	@Override
	public String getContentType() {
		return "application/octet-stream";
	}

	@Override
	public void close() {
	}

}
