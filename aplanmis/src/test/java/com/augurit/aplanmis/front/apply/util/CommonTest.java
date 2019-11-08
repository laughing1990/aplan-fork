package com.augurit.aplanmis.front.apply.util;

import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.List;

public class CommonTest {

    public static void main(String[] args) {
        List<String> names = new ArrayList<>();
        names.add("xiong");
        names.add("li");
        names.add("hello");

        remove(names);

        System.out.println(names);
    }

    private static void remove(List<String> list) {
        Assert.state(list.size() > 0, "at least one element.");

        list.remove("xiong");
    }
}
