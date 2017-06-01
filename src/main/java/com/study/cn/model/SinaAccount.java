package com.study.cn.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author fengzhiming
 * @version 1.0.0 createTime: 2017/6/1 11:13
 * @see
 * @since 1.0
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SinaAccount {

    private String name;

    private String password;
}
