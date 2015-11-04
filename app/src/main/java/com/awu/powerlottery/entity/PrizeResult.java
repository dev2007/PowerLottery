package com.awu.powerlottery.entity;

/**
 * Created by awu on 2015-11-03.
 */
public class PrizeResult {
    private String type;
    private int phase;
    private String result;
    private int total;
    private int prizemoney;
    private String date;

    public void setType(String type){
        this.type = type;
    }

    public String getType(){
        return type;
    }

    public void setPhase(int phase){
        this.phase = phase;
    }

    public int getPhase(){
        return phase;
    }

    public void setResult(String result){
        this.result = result;
    }

    public String getResult(){
        return result;
    }

    public void setTotal(int total){
        this.total = total;
    }

    public int getTotal(){
        return total;
    }

    public void setPrizemoney(int prizemoney){
        this.prizemoney = prizemoney;
    }

    public int getPrizemoney(){
        return prizemoney;
    }

    public void setDate(String date){
        this.date = date;
    }

    public String getDate(){
        return  date;
    }
}
