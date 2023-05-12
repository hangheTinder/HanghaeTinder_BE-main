package com.example.hanghaetinder_bemain.vaildator;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;


public class EmailValidator implements ConstraintValidator<Email, String> {

	@Override
	public boolean isValid(final String value, final ConstraintValidatorContext context) {

		Pattern pattern = Pattern.compile("\\w+@\\w+\\.\\w+(\\.\\w+)?");
		Matcher matcher = pattern.matcher(value);

		return matcher.matches();
	}
}
