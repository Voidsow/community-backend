package com.voidsow.community.backend.controller;

import com.voidsow.community.backend.dto.Result;
import com.voidsow.community.backend.service.SearchService;
import com.voidsow.community.backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/search")
public class SearchController {
    SearchService searchService;

    @Autowired
    public SearchController(SearchService searchService, UserService userService) {
        this.searchService = searchService;
    }


    @GetMapping
    public Result search(String q, int page, @RequestParam("size") int pageSize) {
        return Result.getSuccess(searchService.search(q, page, pageSize));
    }
}
