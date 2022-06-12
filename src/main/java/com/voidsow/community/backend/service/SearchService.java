package com.voidsow.community.backend.service;

import com.voidsow.community.backend.entity.Post;
import com.voidsow.community.backend.entity.PostExample;
import com.voidsow.community.backend.mapper.PostMapper;
import com.voidsow.community.backend.repository.PostRepository;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class SearchService {
    private final ElasticsearchRestTemplate searchTemplate;
    private final PostService postService;

    @Autowired
    public SearchService(PostMapper postMapper, PostRepository postRepository, ElasticsearchRestTemplate searchTemplate, PostService postService) {
        this.searchTemplate = searchTemplate;
        this.postService = postService;

        //将数据库已有的所有post写到elastic中
        postRepository.deleteAll();
        postRepository.saveAll(postMapper.selectByExampleWithBLOBs(new PostExample()));
    }

    public Map<String, Object> search(String keyword, int page, int pageSize) {
        NativeSearchQuery query = new NativeSearchQueryBuilder().withQuery(QueryBuilders.multiMatchQuery(keyword, "title", "content"))
                .withSorts(SortBuilders.fieldSort("type").order(SortOrder.DESC))
                .withSorts(SortBuilders.fieldSort("score").order(SortOrder.DESC))
                .withSorts(SortBuilders.fieldSort("gmtModified").order(SortOrder.DESC))
                .withPageable(PageRequest.of(page - 1, pageSize))
                .withHighlightFields(new HighlightBuilder.Field("title").preTags("<em>").postTags("</em>"),
                        new HighlightBuilder.Field("content").preTags("<em>").postTags("</em>"))
                .build();
        SearchHits<Post> searchHits = searchTemplate.search(query, Post.class, IndexCoordinates.of("post"));
        List<Post> posts = new ArrayList<>();
        System.out.println(searchHits.getSearchHits());
        searchHits.forEach(searchHit -> {
            Post post = searchHit.getContent();
            if (!searchHit.getHighlightField("title").isEmpty())
                post.setTitle(searchHit.getHighlightField("title").get(0));
            if (!searchHit.getHighlightField("content").isEmpty())
                post.setContent(searchHit.getHighlightField("content").get(0));
            posts.add(post);
        });
        return postService.encapsulatePosts(posts, searchHits.getTotalHits(), pageSize);
    }

}
