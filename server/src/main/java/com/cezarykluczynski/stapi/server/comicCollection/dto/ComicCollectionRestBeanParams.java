package com.cezarykluczynski.stapi.server.comicCollection.dto;

import com.cezarykluczynski.stapi.server.common.dto.PageSortBeanParams;

import javax.ws.rs.FormParam;

public class ComicCollectionRestBeanParams extends PageSortBeanParams {

	@FormParam("guid")
	private String guid;

	@FormParam("title")
	private String title;

	@FormParam("publishedYearFrom")
	private Integer publishedYearFrom;

	@FormParam("publishedYearTo")
	private Integer publishedYearTo;

	@FormParam("numberOfPagesFrom")
	private Integer numberOfPagesFrom;

	@FormParam("numberOfPagesTo")
	private Integer numberOfPagesTo;

	@FormParam("stardateFrom")
	private Float stardateFrom;

	@FormParam("stardateTo")
	private Float stardateTo;

	@FormParam("yearFrom")
	private Integer yearFrom;

	@FormParam("yearTo")
	private Integer yearTo;

	@FormParam("photonovel")
	private Boolean photonovel;

	public String getGuid() {
		return guid;
	}

	public String getTitle() {
		return title;
	}

	public Integer getPublishedYearFrom() {
		return publishedYearFrom;
	}

	public Integer getPublishedYearTo() {
		return publishedYearTo;
	}

	public Integer getNumberOfPagesFrom() {
		return numberOfPagesFrom;
	}

	public Integer getNumberOfPagesTo() {
		return numberOfPagesTo;
	}

	public Float getStardateFrom() {
		return stardateFrom;
	}

	public Float getStardateTo() {
		return stardateTo;
	}

	public Integer getYearFrom() {
		return yearFrom;
	}

	public Integer getYearTo() {
		return yearTo;
	}

	public Boolean getPhotonovel() {
		return photonovel;
	}

	public static ComicCollectionRestBeanParams fromPageSortBeanParams(PageSortBeanParams pageSortBeanParams) {
		if (pageSortBeanParams == null) {
			return null;
		}

		ComicCollectionRestBeanParams comicCollectionRestBeanParams = new ComicCollectionRestBeanParams();
		comicCollectionRestBeanParams.setPageNumber(pageSortBeanParams.getPageNumber());
		comicCollectionRestBeanParams.setPageSize(pageSortBeanParams.getPageSize());
		comicCollectionRestBeanParams.setSort(pageSortBeanParams.getSort());
		return comicCollectionRestBeanParams;
	}

}