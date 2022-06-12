package com.voidsow.community.backend.repository;

import com.voidsow.community.backend.entity.Post;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository extends ElasticsearchRepository<Post, Integer> {
}
