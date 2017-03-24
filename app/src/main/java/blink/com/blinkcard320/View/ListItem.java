package blink.com.blinkcard320.View;


import java.util.concurrent.atomic.AtomicInteger;


public class ListItem {
	public static final int WAITING = 1; 
	public static final int RUNNING = 2;
	public static final int SUCCESS = 3;
	public static final int SUSPEND = 0;
	public static final int FAILED = -1;
	

	public static final int DOWNLOAD = 5;
	public static final int UPLOAD = 6;
	
	//fields
	private String fileName = "__.__";
	private String absolutePath = "//"; 
	private long fileLength = 0L;
	private long currentLength = 0L; 
	private String percentage = "0%"; 
	private int state = WAITING; 
	private double speed = 0.0d;
	private long currentTime = 0;
	private int taskType;
	private int totalblock=0;
	private  AtomicInteger alreadblock=new AtomicInteger();

	public synchronized AtomicInteger getAlreadblock() {
		return alreadblock;
	}
	
	public void AddNum(){
		alreadblock.getAndIncrement();
	}
	
	public void setZero(){
		alreadblock=new AtomicInteger();

	}
	
	public synchronized void setAlreadblock(AtomicInteger alreadblock) {
		this.alreadblock = alreadblock;
	}
	public int getTotalblock() {
		return totalblock;
	}
	public void setTotalblock(int totalblock) {
		this.totalblock = totalblock;
	}
	public int getTaskType() {
		return taskType;
	}
	public void setTaskType(int taskType) {
		this.taskType = taskType;
	}
	public long getCurrentTime() {
		return currentTime;
	}
	public void setCurrentTime() {
		currentTime = System.currentTimeMillis();
	}
	/**
	 * @return the fileName
	 */
	public String getFileName() {
		return fileName;
	}
	/**
	 * @param fileName the fileName to set
	 */
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	/**
	 * @return the absolutePath
	 */
	public String getAbsolutePath() {
		return absolutePath;
	}
	/**
	 * @param absolutePath the absolutePath to set
	 */
	public void setAbsolutePath(String absolutePath) {
		this.absolutePath = absolutePath;
	}
	/**
	 * @return the fileLength
	 */
	public long getFileLength() {
		return fileLength;
	}
	/**
	 * @param fileLength the fileLength to set
	 */
	public void setFileLength(long fileLength) {
		this.fileLength = fileLength;
	}
	/**
	 * @return the currentLength
	 */
	public long getCurrentLength() {
		return currentLength;
	}
	/**
	 * @param currentLength the currentLength to set
	 */
	public void setCurrentLength(long currentLength) {
		this.currentLength = currentLength;
	}
	/**
	 * @return the percentage
	 */
	public String getPercentage() {
		return percentage;
	}
	/**
	 * @param percentage the percentage to set
	 */
	public void setPercentage(String percentage) {
		this.percentage = percentage;
	}
	/**
	 * @return the state
	 */
	public int getState() {
		return state;
	}
	/**
	 * @param state the state to set
	 */
	public void setState(int state) {
		this.state = state;
	}
	@Override
	public String toString() {
		return "(" + (taskType == DOWNLOAD ? "download" : "upload") + ": " + fileName + ")";
	}
	@Override
	public boolean equals(Object o) {
		if(! (o instanceof ListItem))
			return false;
		else {
			ListItem i = (ListItem)o;
			return o.toString().equals(i.toString());
		}
	}
	
	/**
	 * @return the String representation of the file size in B, KB, MB, or GB
	 */
	 
	 public String getFileSize() {
		 if(fileLength < _KB)
			 return fileLength + " B";
		 else if(_KB <= fileLength && fileLength < _MB)
			 return fileLength/_KB + " KB";
		 else if(_MB <= fileLength && fileLength < _GB)
			 return fileLength/_MB + " MB";
		 else 
			 return fileLength/_MB + " MB";
	 }
	 public void setSpeed(double speed) {
		 this.speed = speed;
	 }
	 public double getSpeed() {
		 return speed;
	 }
	 public String getSpeedString() {
		 if(speed < _KB)
			 return String.format("%.2f", speed) + " B/S";
		 else if(_KB <= speed && speed < _MB)
			 return String.format("%.2f", speed/_KB) + " K/S";
		 else if(_MB <= speed && speed < _GB)
			 return String.format("%.2f", speed/_MB) + " M/S";
		 else
			 return String.format("%.2f", speed/_MB) + " M/S";
	 }
	 
	 public static final long _KB = 1024;
	 public static final long _MB = 1024 * _KB;
	 public static final long _GB = 1024 * _MB;
}
