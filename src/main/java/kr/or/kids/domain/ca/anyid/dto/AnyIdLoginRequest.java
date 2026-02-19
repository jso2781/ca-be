package kr.or.kids.domain.ca.anyid.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record AnyIdLoginRequest(
        @JsonProperty("ssob") String ssob,
        @JsonProperty("tag") String tag
) {}
