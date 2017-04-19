package com.cezarykluczynski.stapi.etl.template.comicSeries.dto;

import com.cezarykluczynski.stapi.etl.template.common.dto.PublishableSeriesTemplate;
import com.cezarykluczynski.stapi.model.company.entity.Company;
import com.cezarykluczynski.stapi.model.page.entity.Page;
import com.google.common.collect.Sets;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Set;

@Data
@EqualsAndHashCode(callSuper = true)
public class ComicSeriesTemplate extends PublishableSeriesTemplate {

	private Page page;

	private String title;

	private Integer numberOfIssues;

	private Float stardateFrom;

	private Float stardateTo;

	private Integer yearFrom;

	private Integer yearTo;

	private Boolean miniseries;

	private Boolean photonovelSeries;

	private Set<Company> publishers = Sets.newHashSet();

	private boolean productOfRedirect;

}
