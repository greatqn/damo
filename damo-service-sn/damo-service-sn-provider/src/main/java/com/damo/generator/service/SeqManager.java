package com.damo.generator.service;

import org.apache.commons.lang3.StringUtils;

/**
 * 维护一个字符串序号，可读可写，可加可减；
 * 1-10000
 * 1-10,14,20-20000
 */
public class SeqManager {
    public static final String NULL = "null";

    SeqRange range;

    public SeqManager(String seqStr) {
        if(!seqStr.equals(NULL)){
            range = new SeqRange(seqStr);
        }
    }

    public long next() {
        if(range == null){
            throw new RuntimeException("超出范围");
        }
        long sn = range.getSn();
        range = range.rebuild();
        return sn;
    }

    public void back(long l) {
        if(range == null){
            range = new SeqRange(""+l);
        }else{
            range = range.back(l);
        }
    }

    @Override
    public String toString(){
        if(range == null){
            return NULL;
        }else{
            return range.toString();
        }
    }

    private class SeqRange {
        long begin;
        long end;
        SeqRange next;
        public SeqRange(String seqStr){
            int point = StringUtils.indexOf(seqStr,",");
            String me = seqStr;
            if(point > 0){
                me = StringUtils.substring(seqStr,0,point);
                next = new SeqRange(StringUtils.substring(seqStr,point+1));
            }

            if(me.indexOf("-")>0){
                String[] kk = me.split("-");
                begin = Long.parseLong(kk[0]);
                end = Long.parseLong(kk[1]);
            }else{
                begin = Long.parseLong(me);
                end = begin;
            }
        }

        public SeqRange(long l) {
            begin = end = l;
        }

        @Override
        public String toString(){
            String me = "";
            me += begin;
            if(begin != end){
                me += "-" + end;
            }
            if(next != null){
                me += "," + next.toString();
            }
            return me;
        }

        public long getSn() {
            return begin++;
        }

        public SeqRange rebuild() {
            if(begin <= end){
                return this;
            }else{
                return next;
            }
        }

        public SeqRange back(long l) {
            if(l < begin){
                if(l+1 == begin){
                    begin = l;
                    return this;
                }
                SeqRange seqNew = new SeqRange(l);
                seqNew.next = this;
                return seqNew;
            }
            if( l > end){
                if(end+1 == l){
                    end = l;
                    return this;
                }
                if(next != null){
                    next = next.back(l);
                }else{
                    SeqRange seqNew = new SeqRange(l);
                    seqNew.next = this.next;
                    this.next = seqNew;
                }
            }
            return this;
        }
    }

    public static void main(String[] args) {
        String seqStr = "1-2,9-24,26-1000";
        SeqManager seq = new SeqManager(seqStr);
//        for (int i = 0; i < 20; i++) {
//            System.out.println(seq.next()  + "|" + seq.toString());
//        }

        seq.back(1L);
        System.out.println(seq.toString());
        seq.back(10L);
        System.out.println(seq.toString());
        seq.back(2L);
        System.out.println(seq.toString());
        seq.back(9L);
        System.out.println(seq.toString());
        seq.back(11L);
        System.out.println(seq.toString());
        seq.back(25L);
        System.out.println(seq.toString());
    }
}
