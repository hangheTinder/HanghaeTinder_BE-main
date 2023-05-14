package com.example.hanghaetinder_bemain.util;

import java.time.LocalDate;
import java.time.Period;

public class AgeCalculator {
	public static int calculateAge(LocalDate birthday) {
		LocalDate currentDate = LocalDate.now();
		Period period = Period.between(birthday, currentDate);
		return period.getYears();
	}

}
