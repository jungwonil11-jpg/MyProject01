package com.study.myproject01.common.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DataVO {
    private boolean success;
    private String message;
    private Object data;
}
