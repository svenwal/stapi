package com.cezarykluczynski.stapi.server.location.reader;

import com.cezarykluczynski.stapi.client.v1.rest.model.LocationBaseResponse;
import com.cezarykluczynski.stapi.client.v1.rest.model.LocationFullResponse;
import com.cezarykluczynski.stapi.model.location.entity.Location;
import com.cezarykluczynski.stapi.server.common.mapper.PageMapper;
import com.cezarykluczynski.stapi.server.common.reader.BaseReader;
import com.cezarykluczynski.stapi.server.common.reader.FullReader;
import com.cezarykluczynski.stapi.server.common.validator.StaticValidator;
import com.cezarykluczynski.stapi.server.location.dto.LocationRestBeanParams;
import com.cezarykluczynski.stapi.server.location.mapper.LocationBaseRestMapper;
import com.cezarykluczynski.stapi.server.location.mapper.LocationFullRestMapper;
import com.cezarykluczynski.stapi.server.location.query.LocationRestQuery;
import com.google.common.collect.Iterables;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import javax.inject.Inject;

@Service
public class LocationRestReader implements BaseReader<LocationRestBeanParams, LocationBaseResponse>,
		FullReader<String, LocationFullResponse> {

	private LocationRestQuery locationRestQuery;

	private LocationBaseRestMapper locationBaseRestMapper;

	private LocationFullRestMapper locationFullRestMapper;

	private PageMapper pageMapper;

	@Inject
	public LocationRestReader(LocationRestQuery locationRestQuery, LocationBaseRestMapper locationBaseRestMapper,
			LocationFullRestMapper locationFullRestMapper, PageMapper pageMapper) {
		this.locationRestQuery = locationRestQuery;
		this.locationBaseRestMapper = locationBaseRestMapper;
		this.locationFullRestMapper = locationFullRestMapper;
		this.pageMapper = pageMapper;
	}

	@Override
	public LocationBaseResponse readBase(LocationRestBeanParams input) {
		Page<Location> locationPage = locationRestQuery.query(input);
		LocationBaseResponse locationResponse = new LocationBaseResponse();
		locationResponse.setPage(pageMapper.fromPageToRestResponsePage(locationPage));
		locationResponse.getLocations().addAll(locationBaseRestMapper.mapBase(locationPage.getContent()));
		return locationResponse;
	}

	@Override
	public LocationFullResponse readFull(String uid) {
		StaticValidator.requireUid(uid);
		LocationRestBeanParams locationRestBeanParams = new LocationRestBeanParams();
		locationRestBeanParams.setUid(uid);
		Page<com.cezarykluczynski.stapi.model.location.entity.Location> locationPage = locationRestQuery.query(locationRestBeanParams);
		LocationFullResponse locationResponse = new LocationFullResponse();
		locationResponse.setLocation(locationFullRestMapper.mapFull(Iterables.getOnlyElement(locationPage.getContent(), null)));
		return locationResponse;
	}
}