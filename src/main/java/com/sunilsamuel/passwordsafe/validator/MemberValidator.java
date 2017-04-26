package com.sunilsamuel.passwordsafe.validator;

import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import com.sunilsamuel.passwordsafe.model.Member;

public class MemberValidator implements Validator {

	@Override
	public boolean supports(Class<?> clazz) {
		return Member.class.equals(clazz);
	}

	@Override
	public void validate(Object object, Errors errors) {
		if (object == null) {
			errors.reject("member.null");
		}
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "email", "email.empty");
	}

	public void validateFull(Object object, Errors errors) {
		validate(object, errors);
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "password", "password.empty");
	}

}
