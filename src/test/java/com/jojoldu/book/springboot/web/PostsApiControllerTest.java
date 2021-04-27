package com.jojoldu.book.springboot.web;

import static org.assertj.core.api.Assertions.assertThat;

import com.jojoldu.book.springboot.domain.posts.Posts;
import com.jojoldu.book.springboot.domain.posts.PostsRepository;
import com.jojoldu.book.springboot.web.dto.PostsSaveRequestDto;
import com.jojoldu.book.springboot.web.dto.PostsUpdateRequestDto;
import java.util.List;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class PostsApiControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private PostsRepository postsRepository;

    @After
    public void tearDown() throws Exception {
        postsRepository.deleteAll();
    }

    @Test
    public void Posts_등록된다() throws Exception {
        final String title = "title";
        final String content = "content";
        final PostsSaveRequestDto requestDto = PostsSaveRequestDto.builder()
            .title(title)
            .content(content)
            .author("author")
            .build();

        final String url = "http://localhost:" + port + "/api/v1/posts";
        final ResponseEntity<Long> responseEntity = restTemplate.postForEntity(url, requestDto, Long.class);

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).isGreaterThan(0L);

        final List<Posts> all = postsRepository.findAll();
        assertThat(all.get(0).getTitle()).isEqualTo(title);
        assertThat(all.get(0).getContent()).isEqualTo(content);
    }

    @Test
    public void Posts_수정된다() throws Exception {
        final Posts savePosts = postsRepository.save(Posts.builder()
        .title("title")
        .content("content")
        .author("author")
        .build());

        final Long updateId = savePosts.getId();
        final String expectedTitle = "title2";
        final String expectedContent = "content2";

        final PostsUpdateRequestDto requestDto = PostsUpdateRequestDto.builder()
            .title(expectedTitle)
            .content(expectedContent)
            .build();

        final HttpEntity<PostsUpdateRequestDto> requestEntity = new HttpEntity<>(requestDto);
        final String url = "http://localhost:" + port + "/api/v1/posts/" + updateId;
        final ResponseEntity<Long> responseEntity = restTemplate
            .exchange(url, HttpMethod.PUT, requestEntity, Long.class);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).isGreaterThan(0L);

        final List<Posts> all = postsRepository.findAll();
        assertThat(all.get(0).getTitle()).isEqualTo(expectedTitle);
        assertThat(all.get(0).getContent()).isEqualTo(expectedContent);
    }
}