package edu.hhu.zaoerck.baiduMap.domain;

import java.io.Serializable;

public class User implements Serializable{
	private int id;
	private String account;//账号
	private String password;//密码
	private String gender;//性别
	private String birthday;//出生日期
	private String phoneNum;//电话号码
	private String email;//邮箱
	private String address;//籍贯
	private String interest;//兴趣
	private String introduction;//介绍
	private String remAccountStatus;
	private String remPasswordStatus;
	private String preLoginStatus;
	public User() {
		super();
		// TODO Auto-generated constructor stub
	}
	public User(String account, String password, String gender, String birthday, String phoneNum, String email, String address, 
			String interest, String introduction, String remAccountStatus, String remPasswordStatus, String preLoginStatus) {
		super();
		this.account = account;
		this.password = password;
		this.gender = gender;
		this.birthday = birthday;
		this.phoneNum = phoneNum;
		this.email = email;
		this.address = address;
		this.interest = interest;
		this.introduction = introduction;
		this.remAccountStatus = remAccountStatus;
		this.remPasswordStatus = remPasswordStatus;
		this.preLoginStatus = preLoginStatus;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getAccount() {
		return account;
	}
	public void setAccount(String account) {
		this.account = account;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public String getBirthday() {
		return birthday;
	}
	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}
	public String getPhoneNum() {
		return phoneNum;
	}
	public void setPhoneNum(String phoneNum) {
		this.phoneNum = phoneNum;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getInterest() {
		return interest;
	}
	public void setInterest(String interest) {
		this.interest = interest;
	}
	public String getIntroduction() {
		return introduction;
	}
	public void setIntroduction(String introduction) {
		this.introduction = introduction;
	}
	public String getRemAccountStatus() {
		return remAccountStatus;
	}
	public void setRemAccountStatus(String remAccountStatus) {
		this.remAccountStatus = remAccountStatus;
	}
	public String getRemPasswordStatus() {
		return remPasswordStatus;
	}
	public void setRemPasswordStatus(String remPasswordStatus) {
		this.remPasswordStatus = remPasswordStatus;
	}
	public String getPreLoginStatus() {
		return preLoginStatus;
	}
	public void setPreLoginStatus(String preLoginStatus) {
		this.preLoginStatus = preLoginStatus;
	}
	@Override
	public String toString() {
		return "User [id=" + id + ", account=" + account + ", password="
				+ password + ", gender=" + gender + ", birthday=" + birthday
				+ ", phoneNum=" + phoneNum + ", email=" + email + ", address="
				+ address + ", interest=" + interest + ", introduction="
				+ introduction + ", remAccountStatus=" + remAccountStatus
				+ ", remPasswordStatus=" + remPasswordStatus
				+ ", preLoginStatus=" + preLoginStatus + "]";
	}
	
	
	
}
