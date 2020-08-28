package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RestController;



@RestController //controller 내의 모든 요청매핑은 JSP페이지가 아닌 데이터로 응답한다.
@SpringBootApplication//@는 어노테이션이라고 한다.
public class DemoApplication {
	// RestController를 이용하면 @ResponseBody를 사용하지 않아도 된다.
	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}



}
