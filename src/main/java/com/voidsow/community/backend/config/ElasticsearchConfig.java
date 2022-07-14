package com.voidsow.community.backend.config;

import com.voidsow.community.backend.entity.PostExample;
import com.voidsow.community.backend.mapper.PostMapper;
import com.voidsow.community.backend.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ElasticsearchConfig implements ApplicationListener<ApplicationReadyEvent> {
    PostMapper postMapper;
    PostRepository postRepository;

    @Autowired
    public ElasticsearchConfig(PostMapper postMapper, PostRepository postRepository) {
        this.postMapper = postMapper;
        this.postRepository = postRepository;
    }

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        //将数据库已有的所有post写到elastic中
        postRepository.deleteAll();
        postRepository.saveAll(postMapper.selectByExampleWithBLOBs(new PostExample()));
    }
}
