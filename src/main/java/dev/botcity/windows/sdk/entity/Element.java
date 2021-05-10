package dev.botcity.windows.sdk.entity;

public class Element {

	private String content;
	private String classn;
	private int id;
	
	public Element(String content, String classn, int id) {
		super();
		this.content = content;
		this.classn = classn;
		this.id = id;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getClassn() {
		return classn;
	}
	public void setClassn(String classn) {
		this.classn = classn;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	
	
}
