package com.cezarykluczynski.stapi.etl.comicSeries.creation.configuration

import com.cezarykluczynski.stapi.etl.comicSeries.creation.processor.ComicSeriesReader
import com.cezarykluczynski.stapi.etl.common.configuration.AbstractCreationConfigurationTest
import com.cezarykluczynski.stapi.etl.configuration.job.service.StepCompletenessDecider
import com.cezarykluczynski.stapi.etl.util.constant.CategoryName
import com.cezarykluczynski.stapi.etl.util.constant.JobName
import com.cezarykluczynski.stapi.etl.util.constant.StepName
import com.cezarykluczynski.stapi.sources.mediawiki.api.CategoryApi
import com.cezarykluczynski.stapi.sources.mediawiki.api.enums.MediaWikiSource
import org.springframework.context.ApplicationContext

class ComicSeriesCreationConfigurationTest extends AbstractCreationConfigurationTest {

	private static final String TITLE_COMIC_SERIES = 'TITLE_COMIC_SERIES'

	private ApplicationContext applicationContextMock

	private CategoryApi categoryApiMock

	private StepCompletenessDecider jobCompletenessDeciderMock

	private ComicSeriesCreationConfiguration comicSeriesCreationConfiguration

	void setup() {
		applicationContextMock = Mock(ApplicationContext)
		categoryApiMock = Mock(CategoryApi)
		jobCompletenessDeciderMock = Mock(StepCompletenessDecider)
		comicSeriesCreationConfiguration = new ComicSeriesCreationConfiguration(
				applicationContext: applicationContextMock,
				categoryApi: categoryApiMock,
				stepCompletenessDecider: jobCompletenessDeciderMock)
	}

	void "ComicSeriesReader is created with all pages when step is not completed"() {
		when:
		ComicSeriesReader comicSeriesReader = comicSeriesCreationConfiguration.comicSeriesReader()
		List<String> categoryHeaderTitleList = readerToList(comicSeriesReader)

		then:
		1 * jobCompletenessDeciderMock.isStepComplete(JobName.JOB_CREATE, StepName.CREATE_COMIC_SERIES) >> false
		1 * categoryApiMock.getPagesIncludingSubcategories(CategoryName.COMIC_SERIES, MediaWikiSource.MEMORY_ALPHA_EN) >>
				createListWithPageHeaderTitle(TITLE_COMIC_SERIES)
		0 * _
		categoryHeaderTitleList.contains TITLE_COMIC_SERIES
	}

	void "ComicSeriesReader is created with no pages when step is completed"() {
		when:
		ComicSeriesReader comicSeriesReader = comicSeriesCreationConfiguration.comicSeriesReader()
		List<String> categoryHeaderTitleList = readerToList(comicSeriesReader)

		then:
		1 * jobCompletenessDeciderMock.isStepComplete(JobName.JOB_CREATE, StepName.CREATE_COMIC_SERIES) >> true
		0 * _
		categoryHeaderTitleList.empty
	}

}