package com.sunilsamuel.passwordsafe;

import org.apache.commons.lang3.StringUtils;
import org.junit.Test;

public class PasswordSafeTest {

	@Test
	public void passwordTest() {
		String password = "sunilsamuelsunilsunil";

		System.out.println("[" + password + "][" + properPassword(password) + "]");

	}

	private String properPassword(String password) {
		if (password.length() > 16) {
			return password.substring(0, 16);
		}
		if (password.length() == 16) {
			return password;
		}
		return StringUtils.rightPad(password, 16, password);
	}

}
