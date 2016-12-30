package cn.edu.s07150819gdmec.myguard.m3communicationguard.entity;

/**
 * Created by Administrator on 2016/12/21.
 */
public class BlackContactInfo {
    public String phoneNumber;
    public String contactName;
    public int mode;

    public String getModeString (int mode){
        switch (mode){
            case 1:
                return "电话拦截";
            case 21:
                return "短信拦截";

            case 3:
                return "电话、短信拦截";

        }
        return "";
    }
}
