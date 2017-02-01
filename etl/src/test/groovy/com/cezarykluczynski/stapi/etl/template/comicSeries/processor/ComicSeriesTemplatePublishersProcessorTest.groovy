package com.cezarykluczynski.stapi.etl.template.comicSeries.processor

import com.cezarykluczynski.stapi.model.company.entity.Company
import com.cezarykluczynski.stapi.model.company.repository.CompanyRepository
import com.cezarykluczynski.stapi.model.page.entity.enums.MediaWikiSource
import com.cezarykluczynski.stapi.sources.mediawiki.api.WikitextApi
import com.google.common.collect.Lists
import spock.lang.Specification

class ComicSeriesTemplatePublishersProcessorTest extends Specification {

	private static final MediaWikiSource SOURCE = MediaWikiSource.MEMORY_ALPHA_EN
	private static final String LINK_TITLE_1 = 'LINK_TITLE_1'
	private static final String LINK_TITLE_2 = 'LINK_TITLE_2'
	private static final String WIKITEXT = 'WIKITEXT'

	private WikitextApi wikitextApiMock

	private CompanyRepository companyRepositoryMock

	private ComicSeriesTemplatePublishersProcessor comicSeriesTemplatePublishersProcessor

	void setup() {
		wikitextApiMock = Mock(WikitextApi)
		companyRepositoryMock = Mock(CompanyRepository)
		comicSeriesTemplatePublishersProcessor = new ComicSeriesTemplatePublishersProcessor(wikitextApiMock, companyRepositoryMock)
	}

	void "given wikitext, gets links from it, then returns companies that have associated pages with matching titles"() {
		given:
		Company company = Mock(Company)

		when:
		Set<Company> companySet = comicSeriesTemplatePublishersProcessor.process(WIKITEXT)

		then:
		1 * wikitextApiMock.getPageTitlesFromWikitext(WIKITEXT) >> Lists.newArrayList(LINK_TITLE_1, LINK_TITLE_2)
		1 * companyRepositoryMock.findByPageTitleAndPageMediaWikiSource(LINK_TITLE_1, SOURCE) >> Optional.of(company)
		1 * companyRepositoryMock.findByPageTitleAndPageMediaWikiSource(LINK_TITLE_2, SOURCE) >> Optional.empty()
		0 * _
		companySet.size() == 1
		companySet.contains company
	}

}
