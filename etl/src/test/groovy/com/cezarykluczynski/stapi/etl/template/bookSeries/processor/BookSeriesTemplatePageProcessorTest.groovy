package com.cezarykluczynski.stapi.etl.template.bookSeries.processor

import com.cezarykluczynski.stapi.etl.common.dto.EnrichablePair
import com.cezarykluczynski.stapi.etl.common.service.PageBindingService
import com.cezarykluczynski.stapi.etl.template.bookSeries.dto.BookSeriesTemplate
import com.cezarykluczynski.stapi.etl.template.service.TemplateFinder
import com.cezarykluczynski.stapi.model.page.entity.Page as ModelPage
import com.cezarykluczynski.stapi.sources.mediawiki.api.enums.MediaWikiSource
import com.cezarykluczynski.stapi.sources.mediawiki.dto.Page
import com.cezarykluczynski.stapi.sources.mediawiki.dto.PageHeader
import com.cezarykluczynski.stapi.sources.mediawiki.dto.Template
import com.cezarykluczynski.stapi.util.ReflectionTestUtils
import com.cezarykluczynski.stapi.util.constant.TemplateTitle
import com.google.common.collect.Lists
import spock.lang.Specification

class BookSeriesTemplatePageProcessorTest extends Specification {

	private static final String TITLE = 'TITLE'
	private static final Long PAGE_ID = 11L
	private static final MediaWikiSource SOURCES_MEDIA_WIKI_SOURCE = MediaWikiSource.MEMORY_ALPHA_EN

	private PageBindingService pageBindingServiceMock

	private TemplateFinder templateFinderMock

	private BookSeriesTemplatePartsEnrichingProcessor bookSeriesTemplatePartsEnrichingProcessorMock

	private BookSeriesTemplateFixedValuesEnrichingProcessor bookSeriesTemplateFixedValuesEnrichingProcessorMock

	private BookSeriesTemplatePageProcessor bookSeriesTemplatePageProcessor

	void setup() {
		pageBindingServiceMock = Mock()
		templateFinderMock = Mock()
		bookSeriesTemplatePartsEnrichingProcessorMock = Mock()
		bookSeriesTemplateFixedValuesEnrichingProcessorMock = Mock()
		bookSeriesTemplatePageProcessor = new BookSeriesTemplatePageProcessor(pageBindingServiceMock, templateFinderMock,
				bookSeriesTemplatePartsEnrichingProcessorMock, bookSeriesTemplateFixedValuesEnrichingProcessorMock)
	}

	void "missing template results BookSeriesTemplate with only the title and source"() {
		given:
		Page page = new Page(
				title: TITLE,
				pageId: PAGE_ID,
				mediaWikiSource: SOURCES_MEDIA_WIKI_SOURCE)
		ModelPage modelPage = new ModelPage()

		when:
		BookSeriesTemplate bookSeriesTemplate = bookSeriesTemplatePageProcessor.process(page)

		then:
		1 * pageBindingServiceMock.fromPageToPageEntity(page) >> modelPage
		1 * bookSeriesTemplateFixedValuesEnrichingProcessorMock.enrich(_ as EnrichablePair) >> {
			EnrichablePair<BookSeriesTemplate, BookSeriesTemplate> enrichablePair ->
				assert enrichablePair.input == enrichablePair.output
		}
		1 * templateFinderMock.findTemplate(page, TemplateTitle.SIDEBAR_COMIC_SERIES) >> Optional.empty()
		0 * _
		bookSeriesTemplate.title == TITLE
		bookSeriesTemplate.page == modelPage
		ReflectionTestUtils.getNumberOfNotNullFields(bookSeriesTemplate) == 3
	}

	void "returns null when page is an effect of redirect"() {
		given:
		PageHeader pageHeader = Mock()
		Page page = new Page(redirectPath: Lists.newArrayList(pageHeader))

		when:
		BookSeriesTemplate bookSeriesTemplate = bookSeriesTemplatePageProcessor.process(page)

		then:
		bookSeriesTemplate == null
	}

	void "when sidebar book series is found, enriching processor is called"() {
		given:
		Template.Part templatePart = Mock()
		List<Template.Part> templatePartList = Lists.newArrayList(templatePart)
		Page page = new Page(title: TITLE)
		Template sidebarBookSeriesTemplate = new Template(parts: templatePartList)

		when:
		bookSeriesTemplatePageProcessor.process(page)

		then:
		1 * pageBindingServiceMock.fromPageToPageEntity(page)
		1 * bookSeriesTemplateFixedValuesEnrichingProcessorMock.enrich(_ as EnrichablePair) >> {
			EnrichablePair<BookSeriesTemplate, BookSeriesTemplate> enrichablePair ->
				assert enrichablePair.input == enrichablePair.output
		}
		1 * templateFinderMock.findTemplate(page, TemplateTitle.SIDEBAR_COMIC_SERIES) >> Optional.of(sidebarBookSeriesTemplate)
		1 * bookSeriesTemplatePartsEnrichingProcessorMock.enrich(_) >> { EnrichablePair<List<Template.Part>, BookSeriesTemplate> enrichablePair ->
			assert enrichablePair.input == templatePartList
			assert enrichablePair.output != null
		}
		0 * _
	}

}
