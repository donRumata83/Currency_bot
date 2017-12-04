package Bot.Currencies;


import Bot.Enums.Mark;

import java.io.Serializable;

public class Currency implements Serializable{
    private String name;
    private Mark mark;

    private float nbu_ask = 0;
    private float nbu_bid = 0;

    private float mb_ask = 0;
    private float mb_bid = 0;

    private float bank_ask = 0;
    private float bank_bid = 0;

    private float auc_ask = 0;
    private float auc_bid = 0;

    public Currency(String name, Mark mark) {
        this.name = name;
        this.mark = mark;
    }

    public float getNbu_ask() {
        return nbu_ask;
    }

    public void setNbu_ask(float nbu_ask) {
        this.nbu_ask = nbu_ask;
    }

    public float getNbu_bid() {
        return nbu_bid;
    }

    public void setNbu_bid(float nbu_bid) {
        this.nbu_bid = nbu_bid;
    }

    public float getMb_ask() {
        return mb_ask;
    }

    public void setMb_ask(float mb_ask) {
        this.mb_ask = mb_ask;
    }

    public float getMb_bid() {
        return mb_bid;
    }

    public void setMb_bid(float mb_bid) {
        this.mb_bid = mb_bid;
    }

    public float getBank_ask() {
        return bank_ask;
    }

    public void setBank_ask(float bank_ask) {
        this.bank_ask = bank_ask;
    }

    public float getBank_bid() {
        return bank_bid;
    }

    public void setBank_bid(float bank_bid) {
        this.bank_bid = bank_bid;
    }

    public float getAuc_ask() {
        return auc_ask;
    }

    public void setAuc_ask(float auc_ask) {
        this.auc_ask = auc_ask;
    }

    public float getAuc_bid() {
        return auc_bid;
    }

    public void setAuc_bid(float auc_bid) {
        this.auc_bid = auc_bid;
    }

    public String getName() {
        return name;
    }

    public String getMark() {
        return mark.getMark();
    }


}
