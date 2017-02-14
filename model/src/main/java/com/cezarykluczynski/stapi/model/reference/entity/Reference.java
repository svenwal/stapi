package com.cezarykluczynski.stapi.model.reference.entity;

import com.cezarykluczynski.stapi.model.reference.entity.enums.ReferenceType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class Reference {

	@Id
	@Column(nullable = false)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "reference_sequence_generator")
	@SequenceGenerator(name = "reference_sequence_generator", sequenceName = "reference_sequence", allocationSize = 1)
	private Long id;

	private String guid;

	@Enumerated(EnumType.STRING)
	private ReferenceType referenceType;

	private String referenceNumber;

}
