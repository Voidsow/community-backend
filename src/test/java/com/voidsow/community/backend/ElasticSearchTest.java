package com.voidsow.community.backend;

import com.voidsow.community.backend.entity.Post;
import com.voidsow.community.backend.entity.PostExample;
import com.voidsow.community.backend.mapper.PostMapper;
import com.voidsow.community.backend.repository.PostRepository;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.*;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.*;

@SpringBootTest
public class ElasticSearchTest {
    @Autowired
    PostRepository testRepository;

    @Autowired
    PostMapper postMapper;

    @Autowired
    ElasticsearchRestTemplate elasticsearchTemplate;

    @Test
    void test() {
        testRepository.saveAll(postMapper.selectByExample(new PostExample()));
    }

    @Test
    void testUpate() {
        Post post = postMapper.selectByPrimaryKey(1);
        post.setTitle(post.getTitle() + "version2");
        testRepository.save(post);
    }

    @Test
    void testSearch() {
        NativeSearchQuery query = new NativeSearchQueryBuilder().withQuery(QueryBuilders.multiMatchQuery("民族", "title", "content"))
                .withSorts(SortBuilders.fieldSort("type").order(SortOrder.DESC))
                .withSorts(SortBuilders.fieldSort("score").order(SortOrder.DESC))
                .withSorts(SortBuilders.fieldSort("gmtModified").order(SortOrder.DESC))
                .withPageable(PageRequest.of(0, 10))
                .withHighlightFields(new HighlightBuilder.Field("title").preTags("<em>").postTags("<em>"),
                        new HighlightBuilder.Field("content").preTags("<em>").postTags("<em>"))
                .build();
        SearchHits<Post> posts = elasticsearchTemplate.search(query, Post.class, IndexCoordinates.of("post"));
        System.out.println(posts.getSearchHits());
        for (var post :
                posts) {
            System.out.println(post);
            System.out.println(post.getContent());
        }
    }
}
