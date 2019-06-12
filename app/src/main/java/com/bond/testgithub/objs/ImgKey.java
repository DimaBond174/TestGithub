package com.bond.testgithub.objs;

public class ImgKey implements  IKey {
    /* Основой ключа является путь к изображению или R.id */
    public final String str; //тут обычно путь к файлу
    private final int hash;
    public final int width;
    public final int resID;
    public final int enlarge; //Надо ли увеличивать больше чем есть 0/1

    public ImgKey(int resID, int width) {
        this.width=width;
        this.enlarge=1;
        long summ = resID + width + enlarge;//k1+k2+k3;
        hash = (int)(summ ^ (summ >>> 32));
        //hash=resID;
        str=null;
        this.resID=resID;
    }

    public ImgKey(String str, int width, int enlarge) {
        this.width=width;
        this.enlarge=enlarge;
        this.str=str;
        long summ = str.hashCode() + width + enlarge;//k1+k2+k3;
        hash = (int)(summ ^ (summ >>> 32));
        this.resID=0;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ImgKey)) return false;
        ImgKey key = (ImgKey) o;
        if (width!=key.width) {
            return false;
        }
        if (enlarge!=key.enlarge) {
            return false;
        }
        if (null==str && null==key.str) {
            return resID==key.resID;
        } else if (null!=str && null!=key.str) {
            return str.equals(key.str);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return hash;
    }

    @Override
    public int  cmp(Object o)  {
        if (this == o)  return  0;
        ImgKey other = (ImgKey) o;
        int  cmp  =  width - other.width;
        if (0 != cmp) return cmp;
        cmp = enlarge - other.enlarge;
        if (0 != cmp) return cmp;
        if (null == str && null != other.str) {
            return 1;
        }
        if (null != str && null == other.str) {
            return -1;
        }
        if (null == str) {
            return resID - other.resID;
        }
        return  str.compareTo(other.str);
    }

    @Override
    public long get_hash()  {
        return hash < 0 ?  -hash : hash;
    }
}
