package com.example.hanghaetinder_bemain.domain.common.vaildator;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import com.example.hanghaetinder_bemain.domain.common.annotation.Password;

public class PasswordValidator implements ConstraintValidator<Password, String> {

	@Override
	public boolean isValid(final String value, final ConstraintValidatorContext context) {

		Pattern pattern = Pattern.compile("^(?=.*[a-zA-Z])(?=.*[0-9])(?=.*[$&+,:;=?@#|'<>.^*()%!-]).{8,15}$");
		Matcher matcher = pattern.matcher(value);

		return matcher.matches();
	}
}
