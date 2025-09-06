package com.boot.Entites;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnore;

	

@Entity
@Table(name="Book")
public class Book {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int Bid;
	
	@NotBlank(message = "This is a Required Field")
	private String name;
	@NotBlank(message = "This is a Required Field")
	private String authorName;
	@NotNull
	@Min(value = 10,message = "Value should be greater than or equal to 10")
	private int cost;
	@NotNull
	@Min(value = 100,message = "Value should be greater than or equal to 100")
	private int numofpages;
	private String image;
	@Column(length = 1000)
	private String description;
	
	@ManyToOne
	@JsonIgnore
	private User user;
	
	public Book() {
		super();
	}

	public int getBid() {
		return Bid;
	}

	public void setBid(int bid) {
		Bid = bid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAuthorName() {
		return authorName;
	}

	public void setAuthorName(String authorName) {
		this.authorName = authorName;
	}

	public int getCost() {
		return cost;
	}

	public void setCost(int cost) {
		this.cost = cost;
	}

	public int getNumofpages() {
		return numofpages;
	}

	public void setNumofpages(int numofpages) {
		this.numofpages = numofpages;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	@Override
	public String toString() {
		return "Book [Bid=" + Bid + ", name=" + name + ", authorName=" + authorName + ", cost=" + cost + ", numofpages="
				+ numofpages + ", image=" + image + ", description=" + description + ", user=" + user.getName() + "]";
	}
	
	
	
}

