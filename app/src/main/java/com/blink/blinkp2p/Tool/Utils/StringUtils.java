/**
* @Title: StringUtils.java
* @Package com.blink.router.model.utils
* @Description: TODO(��һ�仰�������ļ���ʲô)
* @author Administrator
* @date 2015-12-5
* @version V1.0
*/
package com.blink.blinkp2p.Tool.Utils;

import android.util.Log;

import java.util.ArrayList;


/**
 * @ClassName: StringUtils
 * @Description: TODO(������һ�仰��������������)
 * @author Administrator
 * @date 2015-12-5
 *
 */
public class StringUtils {
	
	public static boolean check(String ip,String mask,String startip,String endip){
		 String regex = "(2[5][0-5]|2[0-4]\\d|1\\d{2}|\\d{1,2})\\.(25[0-5]|2[0-4]\\d|1\\d{2}|\\d{1,2})\\.(25[0-5]|2[0-4]\\d|1\\d{2}|\\d{1,2})\\.(25[0-5]|2[0-4]\\d|1\\d{2}|\\d{1,2})";
		 if(!ip.matches(regex))
			 return false;
		 if(!mask.matches(regex))
			 return false;
		 if(!startip.matches(regex))
			 return false;
		 if(!endip.matches(regex))
			 return false;
		 int count=0;
		 int shortnum=ip.length();
		 if(shortnum>startip.length()){
			 shortnum=startip.length();
		 }
		 if(shortnum>endip.length()){
			 shortnum=endip.length();
		 }
		 for(int i=0;i<shortnum;i++){
			 if(ip.charAt(i)=='.'){
				 count++;
			 }
			 if(!(ip.charAt(i)==startip.charAt(i)&&ip.charAt(i)==endip.charAt(i))){
				 if(count==3)
					 break;
				 return false;
			 }
		 }
		 return true;
	}
	
	public static boolean check_normal(String[] s){
		 String regex = "(2[5][0-5]|2[0-4]\\d|1\\d{2}|\\d{1,2})\\.(25[0-5]|2[0-4]\\d|1\\d{2}|\\d{1,2})\\.(25[0-5]|2[0-4]\\d|1\\d{2}|\\d{1,2})\\.(25[0-5]|2[0-4]\\d|1\\d{2}|\\d{1,2})";
		for(int i=0;i<s.length;i++){
		
			String s1=s[i];
			 if(!s1.matches(regex))
				 return false;
			 
		}
		return true;
	}
	
	public static  String GetStartEndString(char start,char end,String str){
		
		int s=0,e=0;
		for(int i=0;i<str.length();i++){
			if(str.charAt(i)==start){
				s=i;
			}
			if(str.charAt(i)==end){
				e=i;
				break;
			}
		}
		String res=str.substring(s, e+1);
		return res;
	}
	
	public static String GetSpeed(int speed){
		
		String res="";
		int kb=1024*1024;
		if(speed/1024==0){
			res=String.valueOf(speed+" B/S");
			return res;
		}else if(speed/kb!=0){
			res=String.valueOf(speed/kb +" MB/S");
			return res;
		}else {
			res=String.valueOf(speed/1024 +" KB/S");
			return res;
		}
		
	}
	
	public static boolean checkMac(String mac, ArrayList<String> list) {

		for (int i = 0; i < list.size(); i++) {
			if (list.get(i).equals(mac)) {
				return false;
			}
		}
		return true;

	}
	
	public static boolean IsAdpter(String s){
		if(s==null||s.equals("")||s.length()==0||s.length()>6||s.contains(" ")){
			return false;
		}
		for(int i=0;i<s.length();i++){
			char a=s.charAt(i);
			if(a<'0'||a>'9')
				return false;
		}
		return true;
	}
	

	

	
	
	

	
	public static String getversion(String sver){
		
		try {
			String[] res=sver.split(",");
			return res[1];	
		} catch (Exception e) {
			// TODO: handle exception
		}
		return "1.0.0";
	}
	
	public static int[] VerStringToInt(String myversion){
		int[] mynum=new int[3];
		int mynump=0;
		int temp=0;
		for(int i=0;i<myversion.length();i++){
			if(myversion.charAt(i)=='.'){
				String s=myversion.substring(temp,i);
				temp=i+1;
				mynum[mynump]=Integer.valueOf(s);
				mynump++;
			}
		}
		String s=myversion.substring(temp,myversion.length());
		mynum[mynump]=Integer.valueOf(s);
		mynump++;
		return mynum;
	}
	
	public static int[] IPStringToInt(String myversion){
		int[] mynum=new int[7];
		int mynump=0;
		int temp=0;
		for(int i=0;i<myversion.length();i++){
			if(myversion.charAt(i)=='.'){
				String s=myversion.substring(temp,i);
				temp=i+1;
				mynum[mynump]=Integer.valueOf(s);
				mynump++;
			}
		}
		String s=myversion.substring(temp,myversion.length());
		mynum[mynump]=Integer.valueOf(s);
		mynump++;
		return mynum;
	}
	
	public static int[] getRouteDevicesInfo(String s){
		
		int[] res={0,0,0};
		try {
			int is=Integer.valueOf(s);
			res[0]=is&0x1;
			res[1]=(is>>1)&0x1;
			res[2]=(is>>2)&0x1;
			Log.d("getroutedevices",""+res[0]+res[1]+res[2]);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return res;
		}
		return res;
	}
	
	public static boolean IsInActivity(String s){
		String path[]={"/","video\\","picture\\","download\\","music\\","desktop\\"};
		for(int i=0;i<path.length;i++){
			
			if(s.equals(path[i])){
				return true;
			}
			
		}
		return false;
			
	}

}
