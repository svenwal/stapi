package com.cezarykluczynski.stapi.etl.template.video.dto;

import com.cezarykluczynski.stapi.model.page.entity.Page;
import com.cezarykluczynski.stapi.model.series.entity.Series;
import com.cezarykluczynski.stapi.model.video_release.entity.enums.VideoReleaseFormat;
import lombok.Data;

import java.time.LocalDate;

@Data
public class VideoTemplate {

	private String title;

	private Page page;

	private Series series;

	private VideoReleaseFormat format;

	private Integer numberOfEpisodes;

	private Integer numberOfFeatureLengthEpisodes;

	private Integer numberOfDataCarriers;

	private Integer runTime;

	private Float stardateFrom;

	private Float stardateTo;

	private Integer yearFrom;

	private Integer yearTo;

	private LocalDate regionFreeReleaseDate;

	private LocalDate region1AReleaseDate;

	private LocalDate region1SlimlineReleaseDate;

	private LocalDate region2AReleaseDate;

	private LocalDate region2SlimlineReleaseDate;

	private LocalDate region4AReleaseDate;

	private LocalDate region4SlimlineReleaseDate;

	private Boolean amazonDigitalRelease;

	private Boolean dailymotionDigitalRelease;

	private Boolean googlePlayDigitalRelease;

	@SuppressWarnings("memberName")
	private Boolean iTunesDigitalRelease;

	private Boolean ultraVioletDigitalRelease;

	private Boolean vimeoDigitalRelease;

	private Boolean vuduDigitalRelease;

	private Boolean xboxSmartGlassDigital;

	private Boolean youTubeDigitalRelease;

	private Boolean netflixDigitalRelease;

}