package com.example;

import com.example.config.PasswordEncodeConfig;
import com.example.pojo.*;
import com.example.pojo.Process;
import com.example.repository.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mysql.cj.log.Log;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Map;
import java.util.Random;

@SpringBootTest
@Transactional
@Slf4j
@Rollback(false)
class GraduationProjectApplicationTests {
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private TeacherRepository teacherRepository;
	@Autowired
	private StudentRepository studentRepository;
	@Test
	void contextLoads() {
		User user = new User();
		user.setNumber("0000000000");
		user.setPassword("0000000000");
		user.setRole(User.ROLE_ADMIN);
		user.setInsertTime(LocalDateTime.now());
		user.setUpdateTime(LocalDateTime.now());
		user.setDescription("超级管理员");
		userRepository.save(user);
	}
	@Autowired
	private PasswordEncodeConfig passwordEncodeConfig;
	@Test
	public void test2() {
		String number = "2021213145";
		String name = "赵小晶";
		Student student = new Student();
		User user = new User();
		user.setNumber(number);
		user.setPassword(passwordEncodeConfig.passwordEncoder().encode(number));
		user.setRole(User.ROLE_STUDENT);
		user.setDescription("学生");
		userRepository.save(user);
		student.setUserNumber(number);
		student.setSName(name);
		student.setInsertTime(LocalDateTime.now());
		studentRepository.save(student);
	}
	@Test
	public void test3() {
		String number = "0000000001";
		String name = "王波";
		Teacher teacher = new Teacher();
		User user = new User();
		user.setNumber(number);
		user.setPassword(passwordEncodeConfig.passwordEncoder().encode(number));
		user.setRole(User.ROLE_TEACHER);
		user.setDescription("老师");
		userRepository.save(user);
		teacher.setGroupId(1);
		teacher.setTName(name);
		teacher.setTotal(10);
		teacher.setUserNumber(number);
		teacher.setLeftSelect(10);
		teacherRepository.save(teacher);
	}
	@Autowired
	private ProcessRepository processRepository;
	@Test
	public void test4() {
		Process process = new Process();
		process.setItems("[{\"name\": \"打分项一\", \"number\": \"1\", \"score\": 90, \"detail\": \"干得不错\"}]");
		process.setProcessName("开题");
		process.setTeacherId(1158269467945410560L);
		process.setStudentDetail("{\"name\":\"赵小晶\",\"number\":\"2021213145\"}");
		processRepository.save(process);
		String json = "[{\"name\"}]";
		/*processRepository.saveProcessJson("36237284378","开题","赵小晶","2021213145",
				"1158269467945410560",
				"[{\"name\": \"打分项一\", \"number\": \"1\", \"score\": 90, \"detail\": \"干得不错\"}]");*/
	}
	@Test
	public void test5() {
		String password = "2021213176";
		String encode = passwordEncodeConfig.passwordEncoder().encode(password);
		log.debug("{}",encode);
		String encode1 = passwordEncodeConfig.passwordEncoder().encode(password);
		log.debug("{}",encode1);
	}
	@Test
	public void test6() {
		String encode = passwordEncodeConfig.passwordEncoder().encode("0000000001");
		System.out.println(userRepository.updatePassword("0000000001",encode));
	}
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
	@Autowired
	private StringRedisTemplate stringRedisTemplate;
	@Autowired
	private GroupRepository groupRepository;
	@Test
	public void test8() {
		Group group = new Group();
		group.setGroupId(1);
		group.setStudent("[{\"sName\": \"zhao\", \"sNumber\": \"2021213176\"},{\"sName\": \"luo\", \"sNumber\": \"2021213175\"}]");
		group.setTeacher("[{\"tName\": \"BO\", \"tNumber\": \"0000000001\"},{\"tName\": \"Li\", \"tNumber\": \"0000000002\"}]");
		group.setLeftCount(10);
		groupRepository.save(group);
	}
	@Autowired
	private ObjectMapper objectMapper;
	@Test
	public void test9() throws JsonProcessingException {
		Process process = new Process();
		String name = "zhao";
		String number = "2021213176";
		Map<String, String> json = Map.of("name", name,
				"number", number);
		String s = objectMapper.writeValueAsString(json);
		process.setStudentDetail(s);
		process.setProcessName("dhjsakdkas");
		processRepository.save(process);
		System.out.println(process.getId());
	}
}
