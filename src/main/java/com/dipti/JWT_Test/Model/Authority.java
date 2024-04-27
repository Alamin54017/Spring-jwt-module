package com.dipti.JWT_Test.Model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "authority_table")
public class Authority {

	@Id
    @GeneratedValue
    @Column(name = "authority_id")
    private int id;

    @Column(name = "authority", unique = true)
    private String authority;
    
    
}
