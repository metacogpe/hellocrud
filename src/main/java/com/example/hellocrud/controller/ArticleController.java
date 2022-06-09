package com.example.hellocrud.controller;

import com.example.hellocrud.dto.ArticleForm;
import com.example.hellocrud.entity.Article;
import com.example.hellocrud.repository.ArticleRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@Slf4j
public class ArticleController {

    @Autowired  // DI : 스프링 부트가 미리 생성해 놓은 객체를 가져다가 자동 연결
    private ArticleRepository articleRepository;  // DI

    @GetMapping("/articles")
    public String list(Model model) {
        // 1: 출력할 모든 article을 DB로부터 조회
        List<Article> articleList = articleRepository.findAll();
        // 2: 조회한 article묶음을 뷰로
        model.addAttribute("articleList", articleList);
        // 3: 뷰페이지 설정하여 리턴 (뷰페이지: templates/articles/list.mustache)
        return "articles/list";
    }
// 신규 생성을 위한 article 입력 폼
    @GetMapping("/articles/new")
    public String newArticleForm() {
        return "articles/new";
    }

// 신규 생성
    @PostMapping("/articles/create")
    public String createArticle(ArticleForm form) {
        log.info(form.toString());
//        System.out.println(form.toString());

        // 1. dto를 변환해서 Entity만들기
        Article article = form.toEntity();
        log.info(article.toString());
//        System.out.println(article.toString());

        // 2. repository에게 entity를 DB안에 저장하기
        Article saved = articleRepository.save(article);
        log.info(saved.toString());
//        return "redirect:/articles/" + saved.getId(); // ex) /articles/5
        return "redirect:/articles";
    }

// 상세
    @GetMapping("/articles/{id}") //{id}는 변하는 수를 의미, PatheVariable로 정의
        public String show(@PathVariable Long id, Model model) {
        log.info("id = " + id);
        // 1: id로 데이터를 가져옴!
        Article articleEntity = articleRepository.findById(id).orElse(null);
//        List< CommentDto> commentDtos = commentService.comments(id);  // 위에서 commentService 등록하여 사용 가능
        // 2: 가져온 데이터를 모델에 등록!
        model.addAttribute("article",articleEntity);
//        model.addAttribute("commentDtos", commentDtos);
        // 3: 보여줄 페이지를 설정!
        return "articles/detail";
    }

// 상세 편집
    @GetMapping("/articles/{id}/edit")
    public String edit(@PathVariable Long id, Model model) {
        // 수정할 데이터 가져오기
        Article articleEntity = articleRepository.findById(id).orElse(null);

        // 모델에 데이터를 등록
        model.addAttribute("article", articleEntity);

        // 뷰 페이지 설정
        return "articles/edit";
}

// 상세 편집 후 업데이트 표시
@PostMapping("/articles/update")
    // dto로 받기 : ArticleForm
    public String update(ArticleForm form) {
        log.info(form.toString()); // 폼을 통해 수정한 데이터 받아오는지 로그로 확인

        // 1: DTO를 엔터티로 변환
        Article articleEntity = form.toEntity();
        log.info(articleEntity.toString());

        // 2: 엔터티를 DB로 저장
        // 2-1: DB에서 기존 데이터를 가져오기
        Article target = articleRepository.findById(articleEntity.getId()).orElse(null);
        // 2-2: 기존 데이터가 있다면, 값을 갱신
        if (target != null) {
            articleRepository.save(articleEntity);  // 엔터티가 DB로 갱신
        }
        // 3: 수정 결과 페이지로 리다이렉트
        return "redirect:/articles/" + articleEntity.getId();
    }

// 삭제
    @GetMapping("/articles/{id}/delete")
    public String delete(@PathVariable Long id, RedirectAttributes rttr) {
        log.info("삭제 요청이 있습니다.");

        //1: 삭제 target 가져오기
        Article target = articleRepository.findById(id).orElse(null);
        log.info(target.toString());
        //2: 대상 삭제
        if (target != null) {
            articleRepository.delete(target);
            rttr.addFlashAttribute("msg", "삭제가 완료되었습니다");
        }
        //3: 결과 페이지로 리다이렉트
        return "redirect:/articles";
    }
}
