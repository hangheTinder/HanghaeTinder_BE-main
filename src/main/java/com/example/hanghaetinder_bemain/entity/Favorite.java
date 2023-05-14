package com.example.hanghaetinder_bemain.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.Getter;

@Entity
@Getter
public class Favorite {

	@Id
	private Long id;
	private String favoriteName;



}
