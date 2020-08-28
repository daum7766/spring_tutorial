package com.example.demo.dto;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity //데이터베이스가 해당 객체를 자동으로 인식할 수 있도록 해준다.
@Data //getter, setter 자동으로 해줌
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id //pk로 사용하겠다
    private String uid;

    @JsonIgnore //json으로 바꿀때 빼겠다
    private String password;
    private String email;
    
    @Column(insertable = false, updatable = false) //읽기전용
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    private LocalDateTime createDate;
}