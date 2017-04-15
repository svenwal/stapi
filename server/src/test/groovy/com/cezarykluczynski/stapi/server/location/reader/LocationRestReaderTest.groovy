package com.cezarykluczynski.stapi.server.location.reader

import com.cezarykluczynski.stapi.client.v1.rest.model.LocationBase
import com.cezarykluczynski.stapi.client.v1.rest.model.LocationBaseResponse
import com.cezarykluczynski.stapi.client.v1.rest.model.LocationFull
import com.cezarykluczynski.stapi.client.v1.rest.model.LocationFullResponse
import com.cezarykluczynski.stapi.client.v1.rest.model.ResponsePage
import com.cezarykluczynski.stapi.model.location.entity.Location
import com.cezarykluczynski.stapi.server.common.mapper.PageMapper
import com.cezarykluczynski.stapi.server.common.validator.exceptions.MissingGUIDException
import com.cezarykluczynski.stapi.server.location.dto.LocationRestBeanParams
import com.cezarykluczynski.stapi.server.location.mapper.LocationBaseRestMapper
import com.cezarykluczynski.stapi.server.location.mapper.LocationFullRestMapper
import com.cezarykluczynski.stapi.server.location.query.LocationRestQuery
import com.google.common.collect.Lists
import org.springframework.data.domain.Page
import spock.lang.Specification

class LocationRestReaderTest extends Specification {

	private static final String GUID = 'GUID'

	private LocationRestQuery locationRestQueryBuilderMock

	private LocationBaseRestMapper locationBaseRestMapperMock

	private LocationFullRestMapper locationFullRestMapperMock

	private PageMapper pageMapperMock

	private LocationRestReader locationRestReader

	void setup() {
		locationRestQueryBuilderMock = Mock()
		locationBaseRestMapperMock = Mock()
		locationFullRestMapperMock = Mock()
		pageMapperMock = Mock()
		locationRestReader = new LocationRestReader(locationRestQueryBuilderMock, locationBaseRestMapperMock, locationFullRestMapperMock,
				pageMapperMock)
	}

	void "passed request to queryBuilder, then to mapper, and returns result"() {
		given:
		LocationBase locationBase = Mock()
		Location location = Mock()
		LocationRestBeanParams locationRestBeanParams = Mock()
		List<LocationBase> restLocationList = Lists.newArrayList(locationBase)
		List<Location> locationList = Lists.newArrayList(location)
		Page<Location> locationPage = Mock()
		ResponsePage responsePage = Mock()

		when:
		LocationBaseResponse locationResponseOutput = locationRestReader.readBase(locationRestBeanParams)

		then:
		1 * locationRestQueryBuilderMock.query(locationRestBeanParams) >> locationPage
		1 * pageMapperMock.fromPageToRestResponsePage(locationPage) >> responsePage
		1 * locationPage.content >> locationList
		1 * locationBaseRestMapperMock.mapBase(locationList) >> restLocationList
		0 * _
		locationResponseOutput.locations == restLocationList
		locationResponseOutput.page == responsePage
	}

	void "passed GUID to queryBuilder, then to mapper, and returns result"() {
		given:
		LocationFull locationFull = Mock()
		Location location = Mock()
		List<Location> locationList = Lists.newArrayList(location)
		Page<Location> locationPage = Mock()

		when:
		LocationFullResponse locationResponseOutput = locationRestReader.readFull(GUID)

		then:
		1 * locationRestQueryBuilderMock.query(_ as LocationRestBeanParams) >> { LocationRestBeanParams locationRestBeanParams ->
			assert locationRestBeanParams.guid == GUID
			locationPage
		}
		1 * locationPage.content >> locationList
		1 * locationFullRestMapperMock.mapFull(location) >> locationFull
		0 * _
		locationResponseOutput.location == locationFull
	}

	void "requires GUID in full request"() {
		when:
		locationRestReader.readFull(null)

		then:
		thrown(MissingGUIDException)
	}

}
