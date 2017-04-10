package com.cezarykluczynski.stapi.server.character.reader;

import com.cezarykluczynski.stapi.client.v1.rest.model.CharacterBaseResponse;
import com.cezarykluczynski.stapi.client.v1.rest.model.CharacterFullResponse;
import com.cezarykluczynski.stapi.model.character.entity.Character;
import com.cezarykluczynski.stapi.server.character.dto.CharacterRestBeanParams;
import com.cezarykluczynski.stapi.server.character.mapper.CharacterBaseRestMapper;
import com.cezarykluczynski.stapi.server.character.mapper.CharacterFullRestMapper;
import com.cezarykluczynski.stapi.server.character.query.CharacterRestQuery;
import com.cezarykluczynski.stapi.server.common.mapper.PageMapper;
import com.cezarykluczynski.stapi.server.common.reader.BaseReader;
import com.cezarykluczynski.stapi.server.common.reader.FullReader;
import com.cezarykluczynski.stapi.server.common.validator.StaticValidator;
import com.google.common.collect.Iterables;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import javax.inject.Inject;

@Service
public class CharacterRestReader implements BaseReader<CharacterRestBeanParams, CharacterBaseResponse>, FullReader<String, CharacterFullResponse> {

	private CharacterRestQuery characterRestQuery;

	private CharacterBaseRestMapper characterBaseRestMapper;

	private CharacterFullRestMapper characterFullRestMapper;

	private PageMapper pageMapper;

	@Inject
	public CharacterRestReader(CharacterRestQuery characterRestQuery, CharacterBaseRestMapper characterBaseRestMapper,
			CharacterFullRestMapper characterFullRestMapper, PageMapper pageMapper) {
		this.characterRestQuery = characterRestQuery;
		this.characterBaseRestMapper = characterBaseRestMapper;
		this.characterFullRestMapper = characterFullRestMapper;
		this.pageMapper = pageMapper;
	}

	@Override
	public CharacterBaseResponse readBase(CharacterRestBeanParams characterRestBeanParams) {
		Page<Character> characterPage = characterRestQuery.query(characterRestBeanParams);
		CharacterBaseResponse characterResponse = new CharacterBaseResponse();
		characterResponse.setPage(pageMapper.fromPageToRestResponsePage(characterPage));
		characterResponse.getCharacters().addAll(characterBaseRestMapper.mapBase(characterPage.getContent()));
		return characterResponse;
	}

	@Override
	public CharacterFullResponse readFull(String guid) {
		StaticValidator.requireGuid(guid);
		CharacterRestBeanParams characterRestBeanParams = new CharacterRestBeanParams();
		characterRestBeanParams.setGuid(guid);
		Page<Character> characterPage = characterRestQuery.query(characterRestBeanParams);
		CharacterFullResponse characterResponse = new CharacterFullResponse();
		characterResponse.setCharacter(characterFullRestMapper.mapFull(Iterables.getOnlyElement(characterPage.getContent(), null)));
		return characterResponse;
	}

}
