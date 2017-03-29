package com.cezarykluczynski.stapi.server.astronomicalObject.mapper

import com.cezarykluczynski.stapi.client.v1.rest.model.AstronomicalObjectBase
import com.cezarykluczynski.stapi.model.astronomicalObject.dto.AstronomicalObjectRequestDTO
import com.cezarykluczynski.stapi.model.astronomicalObject.entity.AstronomicalObject
import com.cezarykluczynski.stapi.server.astronomicalObject.dto.AstronomicalObjectRestBeanParams
import com.google.common.collect.Lists
import org.mapstruct.factory.Mappers

class AstronomicalObjectBaseRestMapperTest extends AbstractAstronomicalObjectMapperTest {

	private AstronomicalObjectBaseRestMapper astronomicalObjectBaseRestMapper

	void setup() {
		astronomicalObjectBaseRestMapper = Mappers.getMapper(AstronomicalObjectBaseRestMapper)
	}

	void "maps AstronomicalObjectRestBeanParams to AstronomicalObjectRequestDTO"() {
		given:
		AstronomicalObjectRestBeanParams astronomicalObjectRestBeanParams = new AstronomicalObjectRestBeanParams(
				guid: GUID,
				name: NAME,
				astronomicalObjectType: ASTRONOMICAL_OBJECT_TYPE,
				locationGuid: LOCATION_GUID)

		when:
		AstronomicalObjectRequestDTO astronomicalObjectRequestDTO = astronomicalObjectBaseRestMapper.mapBase astronomicalObjectRestBeanParams

		then:
		astronomicalObjectRequestDTO.guid == GUID
		astronomicalObjectRequestDTO.name == NAME
		astronomicalObjectRequestDTO.astronomicalObjectType == ASTRONOMICAL_OBJECT_TYPE
		astronomicalObjectRequestDTO.locationGuid == LOCATION_GUID
	}

	void "maps DB entity to base REST entity"() {
		given:
		AstronomicalObject dBAstronomicalObject = createAstronomicalObject()

		when:
		AstronomicalObjectBase restAstronomicalObject = astronomicalObjectBaseRestMapper.mapBase(Lists.newArrayList(dBAstronomicalObject))[0]

		then:
		restAstronomicalObject.guid == GUID
		restAstronomicalObject.name == NAME
		restAstronomicalObject.astronomicalObjectType == REST_ASTRONOMICAL_OBJECT_TYPE
		restAstronomicalObject.location != null
	}

}