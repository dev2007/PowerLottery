package com.awu.powerlottery.util;

/**
 * Created by awu on 2015-10-15.
 */
public enum LotteryType {
    DEFAULT("empty",0),
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

    public static LotteryType valueOf(int value){
        switch (value){
            case 1:
                return LotteryType.DALETOU;
            case 2:
                return LotteryType.QIXINGCAI;
            case 3:
                return LotteryType.PAILEI3;
            case 4:
                return LotteryType.PAILEI5;
            case 50:
                return LotteryType.SHUANGSEQIU;
            case 51:
                return LotteryType.QILECAI;
            case 52:
                return LotteryType.FUCAI3D;
            default:
                return LotteryType.DEFAULT;
        }
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
