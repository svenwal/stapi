package com.cezarykluczynski.stapi.etl.configuration.job.properties;

import com.cezarykluczynski.stapi.etl.util.constant.StepName;
import com.google.common.collect.Maps;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.Map;

@Service
public class StepToStepPropertiesProvider {

	private Map<String, StepProperties> stepPropertiesMap;

	private StepsProperties stepsProperties;

	@Inject
	public StepToStepPropertiesProvider(StepsProperties stepsProperties) {
		this.stepsProperties = stepsProperties;
		addAllToMap();
	}

	public synchronized Map<String, StepProperties> provide() {
		return stepPropertiesMap;
	}

	private void addAllToMap() {
		stepPropertiesMap = Maps.newHashMap();
		stepPropertiesMap.put(StepName.CREATE_SERIES, stepsProperties.getCreateSeries());
		stepPropertiesMap.put(StepName.CREATE_PERFORMERS, stepsProperties.getCreatePerformers());
		stepPropertiesMap.put(StepName.CREATE_STAFF, stepsProperties.getCreateStaff());
		stepPropertiesMap.put(StepName.CREATE_CHARACTERS, stepsProperties.getCreateCharacters());
		stepPropertiesMap.put(StepName.CREATE_EPISODES, stepsProperties.getCreateEpisodes());
		stepPropertiesMap.put(StepName.CREATE_MOVIES, stepsProperties.getCreateMovies());
	}

}