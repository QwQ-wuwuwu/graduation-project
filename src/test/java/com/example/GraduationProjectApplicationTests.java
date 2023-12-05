package com.example;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Random;

@SpringBootTest
@Transactional
@Slf4j
@Rollback(false)
class GraduationProjectApplicationTests {
	@Test
	public void test7() {
		ArrayList<Integer> integers = new ArrayList<>();
		integers.add(1);integers.add(2);
		integers.add(3);integers.add(4);
		integers.add(5);integers.add(6);
		Random random = new Random();
		Integer index = 0;
		ArrayList<Integer> list = new ArrayList<>();
		list.add(2);list.add(5);
		do {
			index = random.nextInt(integers.size());
			System.out.println(integers.get(index) + "----------");
		}while (list.contains(integers.get(index)));
		System.out.println(integers.get(index));
	}
}
