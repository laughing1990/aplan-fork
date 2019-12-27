package com.augurit.aplanmis.mall.guide.vo;

import lombok.Getter;

@Getter
public enum NumToChinese {
    one(1,"一"),
    tow(2,"二"),
    there(3,"三"),
    four(4,"四"),
    five(5,"五"),
    six(6,"六"),
    seven(7,"七");

    private int num;
    private String chiness;

    NumToChinese(int num,String chinese){
        this.num = num;
        this.chiness = chinese;
    }

    public static String getChiness(int num){
        for (NumToChinese s : NumToChinese.values()) {
            if (s.getNum()==num){
                return s.getChiness();
            }
        }
        return "";
    }


}
