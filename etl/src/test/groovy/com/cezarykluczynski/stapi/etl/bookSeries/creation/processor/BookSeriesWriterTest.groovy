package com.cezarykluczynski.stapi.etl.bookSeries.creation.processor

import com.cezarykluczynski.stapi.model.bookSeries.entity.BookSeries
import com.cezarykluczynski.stapi.model.bookSeries.repository.BookSeriesRepository
import com.cezarykluczynski.stapi.model.page.service.DuplicateFilteringPreSavePageAwareFilter
import com.google.common.collect.Lists
import spock.lang.Specification

class BookSeriesWriterTest extends Specification {

	private BookSeriesRepository bookSeriesRepositoryMock

	private DuplicateFilteringPreSavePageAwareFilter duplicateFilteringPreSavePageAwareProcessorMock

	private BookSeriesWriter bookSeriesWriterMock

	void setup() {
		bookSeriesRepositoryMock = Mock(BookSeriesRepository)
		duplicateFilteringPreSavePageAwareProcessorMock = Mock(DuplicateFilteringPreSavePageAwareFilter)
		bookSeriesWriterMock = new BookSeriesWriter(bookSeriesRepositoryMock, duplicateFilteringPreSavePageAwareProcessorMock)
	}

	void "filters all entities using pre save processor, then writes all entities using repository"() {
		given:
		BookSeries bookSeries = new BookSeries()
		List<BookSeries> bookSeriesList = Lists.newArrayList(bookSeries)

		when:
		bookSeriesWriterMock.write(bookSeriesList)

		then:
		1 * duplicateFilteringPreSavePageAwareProcessorMock.process(_, BookSeries) >> { args ->
			assert args[0][0] == bookSeries
			bookSeriesList
		}
		1 * bookSeriesRepositoryMock.save(bookSeriesList)
		0 * _
	}

}
