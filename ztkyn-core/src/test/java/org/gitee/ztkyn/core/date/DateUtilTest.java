package org.gitee.ztkyn.core.date;

import java.security.SecureRandom;
import java.text.MessageFormat;
import java.util.Arrays;
import java.util.Objects;
import java.util.Set;
import java.util.SortedSet;

import org.gitee.ztkyn.core.colleciton.ECollectionUtil;
import org.junit.jupiter.api.Test;

class DateUtilTest {

	@Test
	void parseDate() {
		System.out.println(DateUtil.parseDate("null"));
	}

	@Test
	void parseDateTime() {
		int[] numbers = { 1, 8, 5, 6, 30, 7, 29, 12, 16, 18, 19, 20 };
		int[] lastNumbers = { 1, 8, 6, 7, 12, 5 };
		Set<String> numberStrSet = ECollectionUtil.MutableSet.newSet();
		while (true) {
			SortedSet<Integer> numberSet = ECollectionUtil.MutableSet.newSortedSet();
			SecureRandom secureRandom = new SecureRandom();
			for (int i = 0; true; i++) {
				int number = numbers[secureRandom.nextInt(0, numbers.length)];
				if (numberSet.contains(number))
					continue;
				numberSet.add(number);
				if (Objects.equals(numberSet.size(), 6))
					break;
			}
			String format = MessageFormat.format("{0},{1}", Arrays.toString(numberSet.toArray()),
					lastNumbers[secureRandom.nextInt(0, lastNumbers.length)]);
			if (!numberStrSet.contains(format)) {
				System.out.println(format);
				numberStrSet.add(format);
			}
		}

	}

}