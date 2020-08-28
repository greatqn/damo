package com.damo.generator.service;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;

import java.text.ParseException;
import java.util.Date;
import java.util.UUID;

/**
 * A snowflake is a source of k-ordered unique 64-bit integers.
 * snowflake——Twitter 开源的分布式自增 ID 算法
 * max:9223372036854775807 2039-09-07 23:47:35:551,1023,4095
 * 再日期往后，会变负数。-1 2109-05-15 15:35:11:103,1023,4095
 */
public class Snowflake {

  public static final int NODE_SHIFT = 10;
  public static final int SEQ_SHIFT = 12;

  public static final short MAX_NODE = 1024;
  public static final short MAX_SEQUENCE = 4096;

  private short sequence;
  private long referenceTime;

  private int node;

  /**
   * A snowflake is designed to operate as a singleton instance within the context of a node.
   * If you deploy different nodes, supplying a unique node id will guarantee the uniqueness
   * of ids generated concurrently on different nodes.
   *
   * @param node This is an id you use to differentiate different nodes.
   */
  public Snowflake(int node) {
    if (node < 0 || node > MAX_NODE) {
      throw new IllegalArgumentException(String.format("node must be between %s and %s", 0, MAX_NODE));
    }
    this.node = node;
  }

  /**
   * Generates a k-ordered unique 64-bit integer. Subsequent invocations of this method will produce
   * increasing integer values.
   *
   * @return The next 64-bit integer.
   */
  public long next() {

    long currentTime = System.currentTimeMillis();
    long counter;

    synchronized(this) {

      if (currentTime < referenceTime) {
        throw new RuntimeException(String.format("Last referenceTime %s is after reference time %s", referenceTime, currentTime));
      } else if (currentTime > referenceTime) {
        this.sequence = 0;
      } else {
        if (this.sequence < Snowflake.MAX_SEQUENCE) {
          this.sequence++;
        } else {
          throw new RuntimeException("Sequence exhausted at " + this.sequence);
        }
      }
      counter = this.sequence;
      referenceTime = currentTime;
    }

    return currentTime << NODE_SHIFT << SEQ_SHIFT | node << SEQ_SHIFT | counter;
  }
  
  public static String show(long sn){
	  long time = sn>>>SEQ_SHIFT>>>NODE_SHIFT;
      long node = (sn & 0x3fffff) >> SEQ_SHIFT;
      long seq  = sn & 0xfff;
      return DateFormatUtils.format(new Date(time),"yyyy-MM-dd HH:mm:ss:SSS")+","+node+","+seq;
  }
  
  public static void main(String[] args) throws ParseException {
	  long currentTime = System.currentTimeMillis();
	  
	System.out.println(currentTime);
	System.out.println(DateFormatUtils.format(new Date(),"yyyy-MM-dd"));
	System.out.println(new Date().getTime());
	
	Snowflake sn = new Snowflake(1);
	Long k = sn.next();
	System.out.println(k+" "+show(k));
	k = sn.next();
	System.out.println(k+" "+show(k));
	k = sn.next();
	System.out.println(k+" "+show(k));
	k = sn.next();
	System.out.println(k+" "+show(k));
	
	k = 6631496772004880384L;
	System.out.println(k+" "+show(k));
	k = 9215187489502007296L;
	System.out.println(k+" "+show(k));
	

	k = -1L;
	System.out.println(k+" "+show(k)+ " " + Long.toBinaryString(k));
	
	
	 
	Date dt = DateUtils.parseDate("2039-9-8","yyyy-MM-dd");
	k = dt.getTime() << NODE_SHIFT << SEQ_SHIFT;
//	System.out.println(Long.toBinaryString(dt.getTime()));
	System.out.println(k+" "+show(k)+ " " + Long.toBinaryString(k));
	
	k = Long.MAX_VALUE;
	System.out.println(k+" "+show(k)+ " " + Long.toBinaryString(k));
	k++;
	System.out.println(k+" "+show(k)+ " " + Long.toBinaryString(k));
	
	long ct = System.currentTimeMillis();
	System.out.println(ct);
	System.out.println(dt.getTime());
	System.out.println(DateUtils.parseDate("2030-1-1","yyyy-MM-dd"));

	System.out.println(UUID.randomUUID().toString());
}

}
