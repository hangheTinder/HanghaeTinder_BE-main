package com.example.hanghaetinder_bemain.domain.member.util;

import java.time.LocalDate;
import java.time.Period;


// 나이 계산을 위한 유틸 입니다.
public class AgeCalculator {
	public static int calculateAge(LocalDate birthday) {
		LocalDate currentDate = LocalDate.now();
		Period period = Period.between(birthday, currentDate);
		return period.getYears();
	}

}
