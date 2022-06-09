package com.example.hellocrud.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
//import org.springframework.data.annotation.Id;  // 사용하지 말아야 DB생성 가능

import javax.persistence.*;

@Entity  //DB가 해당 객체 인식이 가능!(해당 클래스로 테이블 만들기)
@AllArgsConstructor   //
@NoArgsConstructor    // default constructor 추가
@ToString             //
@Getter
public class Article {

    @Id // 대표값 지정
    @GeneratedValue(strategy = GenerationType.IDENTITY) //DB가 id를 자동 생성, 지정 입력하면 에러 발생
    private Long id;

    @Column
    private String title;

    @Column
    private String content;
//
//    public void patch(Article article) {
//        // 모든 정보를 보내지 않는 경우에도 동작하도록 아래의 코드 작성
//        if (article.title != null)
//            this.title = article.title;
//        if (article.content != null)
//            this.content = article.content;
//    }
}