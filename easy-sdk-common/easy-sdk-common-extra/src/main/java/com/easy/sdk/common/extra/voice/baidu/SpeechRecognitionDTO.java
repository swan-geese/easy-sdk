package com.easy.sdk.common.extra.voice.baidu;

import lombok.Data;

@Data
public class SpeechRecognitionDTO {

    private String[] result;
    private String sn;
    private String corpus_no;
    private String err_no;
    private String err_msg;
}
