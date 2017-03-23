package com.cezarykluczynski.stapi.server.comicStrip.endpoint;

import com.cezarykluczynski.stapi.client.v1.rest.model.ComicStripBaseResponse;
import com.cezarykluczynski.stapi.client.v1.rest.model.ComicStripFullResponse;
import com.cezarykluczynski.stapi.server.comicStrip.dto.ComicStripRestBeanParams;
import com.cezarykluczynski.stapi.server.comicStrip.reader.ComicStripRestReader;
import com.cezarykluczynski.stapi.server.common.dto.PageSortBeanParams;

import javax.inject.Inject;
import javax.ws.rs.BeanParam;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

@Path("v1/rest/comicStrip")
@Produces(MediaType.APPLICATION_JSON)
public class ComicStripRestEndpoint {

	private ComicStripRestReader comicStripRestReader;

	@Inject
	public ComicStripRestEndpoint(ComicStripRestReader comicStripRestReader) {
		this.comicStripRestReader = comicStripRestReader;
	}

	@GET
	@Consumes(MediaType.APPLICATION_JSON)
	public ComicStripFullResponse getComicStrip(@QueryParam("guid") String guid) {
		return comicStripRestReader.readFull(guid);
	}

	@GET
	@Path("search")
	@Consumes(MediaType.APPLICATION_JSON)
	public ComicStripBaseResponse searchComicStrip(@BeanParam PageSortBeanParams pageSortBeanParams) {
		return comicStripRestReader.readBase(ComicStripRestBeanParams.fromPageSortBeanParams(pageSortBeanParams));
	}

	@POST
	@Path("search")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public ComicStripBaseResponse searchComicStrip(@BeanParam ComicStripRestBeanParams comicStripRestBeanParams) {
		return comicStripRestReader.readBase(comicStripRestBeanParams);
	}

}
