package com.cezarykluczynski.stapi.server.location.reader;

import com.cezarykluczynski.stapi.client.v1.soap.LocationBaseRequest;
import com.cezarykluczynski.stapi.client.v1.soap.LocationBaseResponse;
import com.cezarykluczynski.stapi.client.v1.soap.LocationFullRequest;
import com.cezarykluczynski.stapi.client.v1.soap.LocationFullResponse;
import com.cezarykluczynski.stapi.model.location.entity.Location;
import com.cezarykluczynski.stapi.server.common.mapper.PageMapper;
import com.cezarykluczynski.stapi.server.common.reader.BaseReader;
import com.cezarykluczynski.stapi.server.common.reader.FullReader;
import com.cezarykluczynski.stapi.server.common.validator.StaticValidator;
import com.cezarykluczynski.stapi.server.location.mapper.LocationBaseSoapMapper;
import com.cezarykluczynski.stapi.server.location.mapper.LocationFullSoapMapper;
import com.cezarykluczynski.stapi.server.location.query.LocationSoapQuery;
import com.google.common.collect.Iterables;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Service
public class LocationSoapReader implements BaseReader<LocationBaseRequest, LocationBaseResponse>,
		FullReader<LocationFullRequest, LocationFullResponse> {

	private LocationSoapQuery locationSoapQuery;

	private LocationBaseSoapMapper locationBaseSoapMapper;

	private LocationFullSoapMapper locationFullSoapMapper;

	private PageMapper pageMapper;

	public LocationSoapReader(LocationSoapQuery locationSoapQuery, LocationBaseSoapMapper locationBaseSoapMapper,
			LocationFullSoapMapper locationFullSoapMapper, PageMapper pageMapper) {
		this.locationSoapQuery = locationSoapQuery;
		this.locationBaseSoapMapper = locationBaseSoapMapper;
		this.locationFullSoapMapper = locationFullSoapMapper;
		this.pageMapper = pageMapper;
	}

	@Override
	public LocationBaseResponse readBase(LocationBaseRequest input) {
		Page<Location> locationPage = locationSoapQuery.query(input);
		LocationBaseResponse locationResponse = new LocationBaseResponse();
		locationResponse.setPage(pageMapper.fromPageToSoapResponsePage(locationPage));
		locationResponse.getLocations().addAll(locationBaseSoapMapper.mapBase(locationPage.getContent()));
		return locationResponse;
	}

	@Override
	public LocationFullResponse readFull(LocationFullRequest input) {
		StaticValidator.requireGuid(input.getGuid());
		Page<Location> locationPage = locationSoapQuery.query(input);
		LocationFullResponse locationFullResponse = new LocationFullResponse();
		locationFullResponse.setLocation(locationFullSoapMapper.mapFull(Iterables.getOnlyElement(locationPage.getContent(), null)));
		return locationFullResponse;
	}

}
