package com.fiapx.videoprocessor.core.domain.entities;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PreSignedResponse {
    private String id;
    private String preSignedUrl;
}
