package com.cezarykluczynski.stapi.model.comicStrip.entity;

import com.cezarykluczynski.stapi.model.character.entity.Character;
import com.cezarykluczynski.stapi.model.comicSeries.entity.ComicSeries;
import com.cezarykluczynski.stapi.model.common.entity.PageAwareEntity;
import com.cezarykluczynski.stapi.model.page.entity.PageAware;
import com.cezarykluczynski.stapi.model.staff.entity.Staff;
import com.google.common.collect.Sets;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.SequenceGenerator;
import java.util.Set;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class ComicStrip extends PageAwareEntity implements PageAware {

	@Id
	@Column(nullable = false)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "comic_strip_sequence_generator")
	@SequenceGenerator(name = "comic_strip_sequence_generator", sequenceName = "comic_strip_sequence", allocationSize = 1)
	private Long id;

	@Column(nullable = false)
	private String title;

	private String periodical;

	private Integer publishedYearFrom;

	private Integer publishedMonthFrom;

	private Integer publishedDayFrom;

	private Integer publishedYearTo;

	private Integer publishedMonthTo;

	private Integer publishedDayTo;

	private Integer numberOfPages;

	private Integer yearFrom;

	private Integer yearTo;

	@ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinTable(name = "comic_strips_comics_series",
			joinColumns = @JoinColumn(name = "comic_strip_id", nullable = false, updatable = false),
			inverseJoinColumns = @JoinColumn(name = "comic_series_id", nullable = false, updatable = false))
	private Set<ComicSeries> comicSeries = Sets.newHashSet();

	@ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinTable(name = "comic_strips_writers",
			joinColumns = @JoinColumn(name = "comic_strip_id", nullable = false, updatable = false),
			inverseJoinColumns = @JoinColumn(name = "staff_id", nullable = false, updatable = false))
	private Set<Staff> writers = Sets.newHashSet();

	@ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinTable(name = "comic_strips_artists",
			joinColumns = @JoinColumn(name = "comic_strip_id", nullable = false, updatable = false),
			inverseJoinColumns = @JoinColumn(name = "staff_id", nullable = false, updatable = false))
	private Set<Staff> artists = Sets.newHashSet();

	@ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinTable(name = "comic_strips_characters",
			joinColumns = @JoinColumn(name = "comic_strip_id", nullable = false, updatable = false),
			inverseJoinColumns = @JoinColumn(name = "character_id", nullable = false, updatable = false))
	private Set<Character> characters = Sets.newHashSet();

}