package com.cezarykluczynski.stapi.etl.episode.creation.processor

import com.cezarykluczynski.stapi.etl.page.common.processor.PageHeaderProcessor
import com.cezarykluczynski.stapi.etl.template.episode.dto.EpisodeTemplate
import com.cezarykluczynski.stapi.etl.template.episode.processor.ToEpisodeTemplateProcessor
import com.cezarykluczynski.stapi.model.episode.entity.Episode
import com.cezarykluczynski.stapi.sources.mediawiki.dto.Page
import com.cezarykluczynski.stapi.sources.mediawiki.dto.PageHeader
import spock.lang.Specification

class EpisodeProcessorTest extends Specification {

	private PageHeaderProcessor pageHeaderProcessorMock

	private ToEpisodeTemplateProcessor toEpisodeTemplateProcessorMock

	private ToEpisodeEntityProcessor toEpisodeEntityProcessorMock

	private EpisodeProcessor episodeProcessor

	def setup() {
		pageHeaderProcessorMock = Mock(PageHeaderProcessor)
		toEpisodeTemplateProcessorMock = Mock(ToEpisodeTemplateProcessor)
		toEpisodeEntityProcessorMock = Mock(ToEpisodeEntityProcessor)
		episodeProcessor = new EpisodeProcessor(pageHeaderProcessorMock, toEpisodeTemplateProcessorMock,
				toEpisodeEntityProcessorMock)
	}

	def "converts PageHeader to Episode"() {
		given:
		PageHeader pageHeader = PageHeader.builder().build()
		Page page = new Page()
		EpisodeTemplate episodeTemplate = new EpisodeTemplate()
		Episode episode = new Episode()

		when:
		Episode outputEpisode = episodeProcessor.process(pageHeader)

		then: 'processors are used in right order'
		1 * pageHeaderProcessorMock.process(pageHeader) >> page
		1 * toEpisodeTemplateProcessorMock.process(page) >> episodeTemplate
		1 * toEpisodeEntityProcessorMock.process(episodeTemplate) >> episode

		then: 'last processor output is returned'
		outputEpisode == episode
	}

}