package com.jojoldu.book.springboot.domain.posts;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class PostsRepositoryTest {

    @Autowired
    PostsRepository postsRepository;

    @After
    public void cleanup() {
        postsRepository.deleteAll();
    }

    @Test
    public void 게시글저장_불러오기() {
        final String title = "테스트 게시글";
        final String content = "테스트 본문";

        postsRepository.save(Posts.builder()
            .title(title)
            .content(content)
            .author("jojoldu@gmail.com")
            .build());

        final List<Posts> postsList = postsRepository.findAll();

        final Posts posts = postsList.get(0);
        assertThat(posts.getTitle()).isEqualTo(title);
        assertThat(posts.getContent()).isEqualTo(content);
    }
}