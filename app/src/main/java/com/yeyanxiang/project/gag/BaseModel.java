package com.yeyanxiang.project.gag;

import com.google.gson.Gson;

/**
 * @author 叶雁翔
 * 
 * @Email yanxiang1120@gmail.com
 * 
 * @version 1.0
 * 
 * @update 2014年6月26日
 * 
 * @简介
 */
public abstract class BaseModel {
    public String toJson() {
        return new Gson().toJson(this);
    }
}
