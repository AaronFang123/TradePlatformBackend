package com.xsjt.learn.neo4j;

import com.alibaba.fastjson.JSON;
import com.xsjt.learn.neo4j.dao.InvitingActionsRepository;
import com.xsjt.learn.neo4j.dao.MemberInvitRepository;
import com.xsjt.learn.neo4j.model.InviteRelationship;
import com.xsjt.learn.neo4j.model.MemberInvite;
import com.xsjt.learn.neo4j.util.CodeUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

/**
 * 测试类
 */

class UpAndLower {//创建类
    public static void main(String args[]){//主方法
        String str = new String("013v%DDDeee");//创建字符串str
        String newstr = str.toLowerCase();//使用toLowerCase()方法实现小写转换
        String newstr2 = str.toUpperCase();//使用toUpperCase()方法实现大写转换
        System.out.println(newstr);
        System.out.println(newstr2);
    }
}
