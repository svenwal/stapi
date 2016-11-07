package com.cezarykluczynski.stapi.server.staff.query;

import com.cezarykluczynski.stapi.client.v1.soap.StaffRequest;
import com.cezarykluczynski.stapi.model.staff.dto.StaffRequestDTO;
import com.cezarykluczynski.stapi.model.staff.entity.Staff;
import com.cezarykluczynski.stapi.model.staff.repository.StaffRepository;
import com.cezarykluczynski.stapi.server.common.mapper.PageMapper;
import com.cezarykluczynski.stapi.server.staff.dto.StaffRestBeanParams;
import com.cezarykluczynski.stapi.server.staff.mapper.StaffRequestMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import javax.inject.Inject;

@Service
public class StaffQuery {

	private StaffRequestMapper staffRequestMapper;

	private PageMapper pageMapper;

	private StaffRepository staffRepository;

	@Inject
	public StaffQuery(StaffRequestMapper staffRequestMapper, PageMapper pageMapper, StaffRepository staffRepository) {
		this.staffRequestMapper = staffRequestMapper;
		this.pageMapper = pageMapper;
		this.staffRepository = staffRepository;
	}

	public Page<Staff> query(StaffRequest staffRequest) {
		StaffRequestDTO staffRequestDTO = staffRequestMapper.map(staffRequest);
		PageRequest pageRequest = pageMapper.fromRequestPageToPageRequest(staffRequest.getPage());
		return staffRepository.findMatching(staffRequestDTO, pageRequest);
	}

	public Page<Staff> query(StaffRestBeanParams staffRestBeanParams) {
		StaffRequestDTO staffRequestDTO = staffRequestMapper.map(staffRestBeanParams);
		PageRequest pageRequest = pageMapper.fromPageAwareBeanParamsToPageRequest(staffRestBeanParams);
		return staffRepository.findMatching(staffRequestDTO, pageRequest);
	}

}
