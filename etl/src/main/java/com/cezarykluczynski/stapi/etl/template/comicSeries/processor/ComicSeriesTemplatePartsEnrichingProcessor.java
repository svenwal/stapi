package com.cezarykluczynski.stapi.etl.template.comicSeries.processor;

import com.cezarykluczynski.stapi.etl.common.dto.EnrichablePair;
import com.cezarykluczynski.stapi.etl.common.processor.ItemEnrichingProcessor;
import com.cezarykluczynski.stapi.etl.common.processor.company.WikitextToCompaniesProcessor;
import com.cezarykluczynski.stapi.etl.template.comicSeries.dto.ComicSeriesTemplate;
import com.cezarykluczynski.stapi.etl.template.comicSeries.dto.ComicSeriesTemplateParameter;
import com.cezarykluczynski.stapi.etl.template.common.dto.datetime.StardateRange;
import com.cezarykluczynski.stapi.etl.template.common.dto.datetime.YearRange;
import com.cezarykluczynski.stapi.etl.template.common.processor.datetime.PublishableSeriesPublishedDatesEnrichingProcessor;
import com.cezarykluczynski.stapi.etl.template.common.processor.datetime.PublishableSeriesTemplateMiniseriesProcessor;
import com.cezarykluczynski.stapi.etl.template.common.processor.datetime.WikitextToStardateRangeProcessor;
import com.cezarykluczynski.stapi.etl.template.common.processor.datetime.WikitextToYearRangeProcessor;
import com.cezarykluczynski.stapi.sources.mediawiki.dto.Template;
import com.google.common.primitives.Ints;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.List;

@Service
public class ComicSeriesTemplatePartsEnrichingProcessor implements ItemEnrichingProcessor<EnrichablePair<List<Template.Part>, ComicSeriesTemplate>> {

	private WikitextToCompaniesProcessor wikitextToCompaniesProcessor;

	private PublishableSeriesPublishedDatesEnrichingProcessor publishableSeriesPublishedDatesEnrichingProcessor;

	private WikitextToYearRangeProcessor wikitextToYearRangeProcessor;

	private WikitextToStardateRangeProcessor wikitextToStardateRangeProcessor;

	private PublishableSeriesTemplateMiniseriesProcessor publishableSeriesTemplateMiniseriesProcessor;

	@Inject
	public ComicSeriesTemplatePartsEnrichingProcessor(WikitextToCompaniesProcessor wikitextToCompaniesProcessor,
			PublishableSeriesPublishedDatesEnrichingProcessor publishableSeriesPublishedDatesEnrichingProcessor,
			WikitextToYearRangeProcessor wikitextToYearRangeProcessor, WikitextToStardateRangeProcessor wikitextToStardateRangeProcessor,
			PublishableSeriesTemplateMiniseriesProcessor publishableSeriesTemplateMiniseriesProcessor) {
		this.wikitextToCompaniesProcessor = wikitextToCompaniesProcessor;
		this.publishableSeriesPublishedDatesEnrichingProcessor = publishableSeriesPublishedDatesEnrichingProcessor;
		this.wikitextToYearRangeProcessor = wikitextToYearRangeProcessor;
		this.wikitextToStardateRangeProcessor = wikitextToStardateRangeProcessor;
		this.publishableSeriesTemplateMiniseriesProcessor = publishableSeriesTemplateMiniseriesProcessor;
	}

	@Override
	public void enrich(EnrichablePair<List<Template.Part>, ComicSeriesTemplate> enrichablePair) throws Exception {
		ComicSeriesTemplate comicSeriesTemplate = enrichablePair.getOutput();

		for (Template.Part part : enrichablePair.getInput()) {
			String key = part.getKey();
			String value = part.getValue();

			switch (key) {
				case ComicSeriesTemplateParameter.PUBLISHER:
					comicSeriesTemplate.getPublishers().addAll(wikitextToCompaniesProcessor.process(value));
					break;
				case ComicSeriesTemplateParameter.PUBLISHED:
					if (comicSeriesTemplate.getPublishedYearFrom() == null && comicSeriesTemplate.getPublishedYearTo() == null) {
						publishableSeriesPublishedDatesEnrichingProcessor.enrich(EnrichablePair.of(part, comicSeriesTemplate));
					}
					break;
				case ComicSeriesTemplateParameter.ISSUES:
					if (comicSeriesTemplate.getNumberOfIssues() == null) {
						comicSeriesTemplate.setNumberOfIssues(Ints.tryParse(value));
					}
					break;
				case ComicSeriesTemplateParameter.YEAR:
					if (comicSeriesTemplate.getYearFrom() == null && comicSeriesTemplate.getYearTo() == null) {
						YearRange yearRange = wikitextToYearRangeProcessor.process(value);
						if (yearRange != null) {
							comicSeriesTemplate.setYearFrom(yearRange.getYearFrom());
							comicSeriesTemplate.setYearTo(yearRange.getYearTo());
						}
					}
					break;
				case ComicSeriesTemplateParameter.STARDATE:
					if (comicSeriesTemplate.getStardateFrom() == null && comicSeriesTemplate.getStardateTo() == null) {
						StardateRange stardateRange = wikitextToStardateRangeProcessor.process(value);
						if (stardateRange != null) {
							comicSeriesTemplate.setStardateFrom(stardateRange.getStardateFrom());
							comicSeriesTemplate.setStardateTo(stardateRange.getStardateTo());
						}
					}
					break;
				case ComicSeriesTemplateParameter.SERIES:
					if (comicSeriesTemplate.getMiniseries() == null) {
						comicSeriesTemplate.setMiniseries(publishableSeriesTemplateMiniseriesProcessor.process(value));
					}
					break;
				default:
					break;
			}
		}
	}

}
