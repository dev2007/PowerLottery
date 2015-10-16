package com.awu.powerlottery.util;

/**
 * Created by awu on 2015-10-15.
 */
public enum LotteryType {
    SHUANGSEQIU("双色球",50),
    FUCAI3D("福彩3D",52);

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
