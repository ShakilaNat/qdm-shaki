package com.qdm.cg.clients.entity;

import java.sql.Blob;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "tb_equipment")
public class Product {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long product_id;
	private long clientId;
	private String product_name;
	private String product_desc;
	private String product_code;
	private String review_description;
	private String review_title;
	private String rating_out_of_five;
	private String reviewed_on;

}
