package com.cezarykluczynski.stapi.etl.template.comics.processor;

import com.cezarykluczynski.stapi.etl.comicStrip.creation.service.ComicStripCandidatePageGatheringService;
import com.cezarykluczynski.stapi.etl.common.dto.EnrichablePair;
import com.cezarykluczynski.stapi.etl.common.processor.CategoryTitlesExtractingProcessor;
import com.cezarykluczynski.stapi.etl.common.processor.WikitextCharactersProcessor;
import com.cezarykluczynski.stapi.etl.common.service.PageBindingService;
import com.cezarykluczynski.stapi.etl.template.comics.dto.ComicsTemplate;
import com.cezarykluczynski.stapi.etl.template.service.TemplateFinder;
import com.cezarykluczynski.stapi.etl.util.TitleUtil;
import com.cezarykluczynski.stapi.etl.util.constant.CategoryTitle;
import com.cezarykluczynski.stapi.sources.mediawiki.dto.Page;
import com.cezarykluczynski.stapi.sources.mediawiki.dto.Template;
import com.cezarykluczynski.stapi.util.constant.PageTitle;
import com.cezarykluczynski.stapi.util.constant.TemplateTitle;
import com.google.common.collect.Sets;
import org.apache.commons.lang3.StringUtils;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.Optional;
import java.util.Set;

@Service
public class ComicsTemplatePageProcessor implements ItemProcessor<Page, ComicsTemplate> {

	private static final Set<String> INVALID_TITLES = Sets.newHashSet(PageTitle.COMICS, PageTitle.PHOTONOVELS);
	private static final Set<String> PHOTONOVEL_CATEGORIES = Sets.newHashSet(CategoryTitle.PHOTONOVELS, CategoryTitle.PHOTONOVELS_COLLECTIONS);
	private static final String COMIC = "(comic";
	private static final String FOTONOVEL = "(fotonovel";
	private static final String OMNIBUS = "(omnibus";

	private final CategoryTitlesExtractingProcessor categoryTitlesExtractingProcessor;

	private final ComicStripCandidatePageGatheringService comicStripCandidatePageGatheringService;

	private final PageBindingService pageBindingService;

	private final TemplateFinder templateFinder;

	private final ComicsTemplateCompositeEnrichingProcessor comicsTemplateCompositeEnrichingProcessor;

	private final ComicsTemplatePartsEnrichingProcessor comicsTemplatePartsEnrichingProcessor;

	private final WikitextCharactersProcessor wikitextCharactersProcessor;

	@Inject
	public ComicsTemplatePageProcessor(CategoryTitlesExtractingProcessor categoryTitlesExtractingProcessor,
			ComicStripCandidatePageGatheringService comicStripCandidatePageGatheringService, PageBindingService pageBindingService,
			TemplateFinder templateFinder, ComicsTemplateCompositeEnrichingProcessor comicsTemplateCompositeEnrichingProcessor,
			ComicsTemplatePartsEnrichingProcessor comicsTemplatePartsEnrichingProcessor, WikitextCharactersProcessor wikitextCharactersProcessor) {
		this.categoryTitlesExtractingProcessor = categoryTitlesExtractingProcessor;
		this.comicStripCandidatePageGatheringService = comicStripCandidatePageGatheringService;
		this.pageBindingService = pageBindingService;
		this.templateFinder = templateFinder;
		this.comicsTemplateCompositeEnrichingProcessor = comicsTemplateCompositeEnrichingProcessor;
		this.comicsTemplatePartsEnrichingProcessor = comicsTemplatePartsEnrichingProcessor;
		this.wikitextCharactersProcessor = wikitextCharactersProcessor;
	}

	@Override
	public ComicsTemplate process(Page item) throws Exception {
		if (shouldBeFilteredOut(item)) {
			return null;
		}

		Optional<Template> sidebarComicStripTemplateOptional = templateFinder.findTemplate(item, TemplateTitle.SIDEBAR_COMIC_STRIP);

		if (sidebarComicStripTemplateOptional.isPresent()) {
			comicStripCandidatePageGatheringService.addCandidate(item);
			return null;
		}

		String title = item.getTitle();
		ComicsTemplate comicsTemplate = new ComicsTemplate();
		comicsTemplate.setTitle(StringUtils.containsAny(title, COMIC, FOTONOVEL, OMNIBUS) ? TitleUtil.getNameFromTitle(title) : title);
		comicsTemplate.setPage(pageBindingService.fromPageToPageEntity(item));
		comicsTemplate.setProductOfRedirect(!item.getRedirectPath().isEmpty());
		comicsTemplate.setPhotonovel(isPhotonovel(item));
		comicsTemplate.setAdaptation(isAdaptation(item));

		comicsTemplateCompositeEnrichingProcessor.enrich(EnrichablePair.of(item, comicsTemplate));
		comicsTemplate.getCharacters().addAll(wikitextCharactersProcessor.process(item));

		Optional<Template> sidebarComicsTemplateOptional = templateFinder.findTemplate(item, TemplateTitle.SIDEBAR_COMIC, TemplateTitle.SIDEBAR_NOVEL,
				TemplateTitle.SIDEBAR_AUDIO);
		if (!sidebarComicsTemplateOptional.isPresent()) {
			return comicsTemplate;
		}

		Template sidebarComicsTemplate = sidebarComicsTemplateOptional.get();

		comicsTemplatePartsEnrichingProcessor.enrich(EnrichablePair.of(sidebarComicsTemplate.getParts(), comicsTemplate));

		return comicsTemplate;
	}

	private boolean shouldBeFilteredOut(Page item) {
		return INVALID_TITLES.contains(item.getTitle()) || categoryTitlesExtractingProcessor.process(item.getCategories())
				.contains(CategoryTitle.STAR_TREK_SERIES_MAGAZINES);
	}

	private boolean isPhotonovel(Page item) {
		return categoryTitlesExtractingProcessor.process(item.getCategories()).stream().anyMatch(PHOTONOVEL_CATEGORIES::contains);
	}

	private boolean isAdaptation(Page item) {
		return categoryTitlesExtractingProcessor.process(item.getCategories()).stream().anyMatch(CategoryTitle.COMIC_ADAPTATIONS::equals);
	}

}
