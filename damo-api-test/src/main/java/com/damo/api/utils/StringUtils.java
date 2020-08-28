/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.damo.api.utils;

 public class StringUtils {

     public static String getPath(String packeName,String filaName)
     {
         String path = "";
         path = System.getProperty("user.dir") + "\\src\\main\\java\\";
         path = path + packeName.replace(".", "\\") + "\\data\\" + filaName;
         return path;
     }
     
     public static String round(double value,int digits)
     {
         return String.format("%."+digits+"f", value);
     }
      
     public static String round(String value,int digits)
     {
         return String.format("%."+digits+"f", Double.valueOf(value));
     }
     
     public static String subZeroAndDot(String s)
     {
         if(s.indexOf(".") > 0)
         {
             s = s.replaceAll("0+?$", "");//去掉多余的0 
             s = s.replaceAll("[.]$", "");//如最后一位是.则去掉
         }
         return s;
     }    
     
     public static String subZeroAndDot(Double s)
     {
         String str = round(s,2);
         if(str.indexOf(".") > 0)
         {
             str = str.replaceAll("0+?$", "");//去掉多余的0 
             str = str.replaceAll("[.]$", "");//如最后一位是.则去掉
         }
         return str;
     }    

}