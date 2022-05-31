package com.voidsow.community.backend.dto;

import lombok.Data;

@Data
public class Follow {
    int follower;
    int followee;
    boolean followed;
}
