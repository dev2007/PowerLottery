package com.awu.powerlottery.util;

/**
 * Created by awu on 2015-10-15.
 */
public enum LotteryType {
    SHUANGSEQIU("ssq",50),
    FUCAI3D("fc3d",52),
    QILECAI("qlc",51),
    DALETOU("dlt",1),
    QIXINGCAI("qxc",2),
    PAILEI3("pl3",3),
    PAILEI5("pl5",4);

    private String name;
    private int value;
    private LotteryType(String name,int value){
        this.name = name;
        this.value = value;
    }

    public static String getName(int value){
        for(LotteryType type : LotteryType.values()){
            if(type.getValue() == value){
                return type.name;
            }
        }
        return "";
    }

    /**
     * get LotteryType's name.
     * @return
     */
    public String getName(){
        return name;
    }

    /**
     * get LotteryType's value.
     * @return
     */
    public int getValue(){
        return value;
    }
}
