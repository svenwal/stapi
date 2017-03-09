package com.cezarykluczynski.stapi.server.character.mapper;

import com.cezarykluczynski.stapi.model.character.entity.CharacterSpecies;
import com.cezarykluczynski.stapi.server.configuration.MapstructConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.ReportingPolicy;

@Mapper(config = MapstructConfiguration.class, unmappedTargetPolicy = ReportingPolicy.WARN)
public interface CharacterSpeciesSoapMapper {

	@Mappings({
			@Mapping(target = "guid", source = "species.guid"),
			@Mapping(target = "name", source = "species.name")
	})
	com.cezarykluczynski.stapi.client.v1.soap.CharacterSpecies map(CharacterSpecies characterSpecies);

}
