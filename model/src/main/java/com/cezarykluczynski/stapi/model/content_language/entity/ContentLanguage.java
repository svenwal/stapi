package com.cezarykluczynski.stapi.model.content_language.entity;

import com.cezarykluczynski.stapi.model.common.annotation.TrackedEntity;
import com.cezarykluczynski.stapi.model.common.annotation.enums.TrackedEntityType;
import com.cezarykluczynski.stapi.model.content_language.repository.ContentLanguageRepository;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.Column;
import javax.persistence.Entity;
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
@Cache(usage = CacheConcurrencyStrategy.READ_ONLY)
@TrackedEntity(type = TrackedEntityType.REAL_WORLD_HELPER, repository = ContentLanguageRepository.class, apiEntity = false,
		singularName = "content language", pluralName = "content languages")
public class ContentLanguage {

	@Id
	@Column(nullable = false)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "content_language_sequence_generator")
	@SequenceGenerator(name = "content_language_sequence_generator", sequenceName = "content_language_sequence", allocationSize = 1)
	private Long id;

	private String name;

	@Column(name = "iso_639_1_code")
	private String iso639_1Code;

}