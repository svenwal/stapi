package com.cezarykluczynski.stapi.etl.configuration.job

import com.cezarykluczynski.stapi.etl.character.creation.processor.CharacterProcessor
import com.cezarykluczynski.stapi.etl.character.creation.processor.CharacterReader
import com.cezarykluczynski.stapi.etl.character.creation.processor.CharacterWriter
import com.cezarykluczynski.stapi.etl.common.listener.CommonStepExecutionListener
import com.cezarykluczynski.stapi.etl.configuration.job.properties.StepProperties
import com.cezarykluczynski.stapi.etl.configuration.job.properties.StepsProperties
import com.cezarykluczynski.stapi.etl.episode.creation.processor.EpisodeProcessor
import com.cezarykluczynski.stapi.etl.episode.creation.processor.EpisodeReader
import com.cezarykluczynski.stapi.etl.episode.creation.processor.EpisodeWriter
import com.cezarykluczynski.stapi.etl.movie.creation.processor.MovieProcessor
import com.cezarykluczynski.stapi.etl.movie.creation.processor.MovieReader
import com.cezarykluczynski.stapi.etl.movie.creation.processor.MovieWriter
import com.cezarykluczynski.stapi.etl.performer.creation.processor.PerformerProcessor
import com.cezarykluczynski.stapi.etl.performer.creation.processor.PerformerReader
import com.cezarykluczynski.stapi.etl.performer.creation.processor.PerformerWriter
import com.cezarykluczynski.stapi.etl.series.creation.processor.SeriesProcessor
import com.cezarykluczynski.stapi.etl.series.creation.processor.SeriesReader
import com.cezarykluczynski.stapi.etl.series.creation.processor.SeriesWriter
import com.cezarykluczynski.stapi.etl.staff.creation.processor.StaffProcessor
import com.cezarykluczynski.stapi.etl.staff.creation.processor.StaffReader
import com.cezarykluczynski.stapi.etl.staff.creation.processor.StaffWriter
import com.cezarykluczynski.stapi.etl.util.constant.StepName
import org.apache.commons.lang3.RandomUtils
import org.springframework.batch.core.Job
import org.springframework.batch.core.Step
import org.springframework.batch.core.StepExecutionListener
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory
import org.springframework.batch.core.step.builder.SimpleStepBuilder
import org.springframework.batch.core.step.builder.StepBuilder
import org.springframework.batch.core.step.tasklet.TaskletStep
import org.springframework.batch.item.ItemProcessor
import org.springframework.batch.item.ItemReader
import org.springframework.batch.item.ItemWriter
import org.springframework.beans.factory.FactoryBean
import org.springframework.context.ApplicationContext
import spock.lang.Specification

class EtlJobConfigurationTest extends Specification {

	private static final Integer STEP_SIZE = RandomUtils.nextInt(10, 20)

	private JobBuilder jobBuilderMock

	private ApplicationContext applicationContextMock

	private StepBuilderFactory stepBuilderFactoryMock

	private StepsProperties stepsPropertiesMock

	private EtlJobConfiguration etlJobConfiguration

	private StepBuilder stepBuilderMock

	private SimpleStepBuilder simpleStepBuilderMock

	private ItemReader itemReaderMock

	private ItemProcessor itemProcessorMock

	private ItemWriter itemWriterMock

	private StepExecutionListener stepExecutionListenerMock

	private TaskletStep taskletStepMock

	private StepProperties stepProperties

	def setup() {
		jobBuilderMock = Mock(JobBuilder)
		applicationContextMock = Mock(ApplicationContext)
		stepBuilderFactoryMock = Mock(StepBuilderFactory)
		stepsPropertiesMock = Mock(StepsProperties)
		etlJobConfiguration = new EtlJobConfiguration(
				jobBuilder: jobBuilderMock,
				applicationContext: applicationContextMock,
				stepBuilderFactory: stepBuilderFactoryMock,
				stepsProperties: stepsPropertiesMock)

		stepBuilderMock = Mock(StepBuilder)
		simpleStepBuilderMock = Mock(SimpleStepBuilder)
		itemReaderMock = Mock(ItemReader)
		itemProcessorMock = Mock(ItemProcessor)
		itemWriterMock = Mock(ItemWriter)
		stepExecutionListenerMock = Mock(StepExecutionListener)
		taskletStepMock = Mock(TaskletStep)
		stepProperties = Mock(StepProperties)
	}

	def "passed JOB_CREATE bean creation to JobBuilder, and returns FactoryBean"() {
		given:
		Job job = Mock(Job)

		when:
		FactoryBean<Job> factoryBean = etlJobConfiguration.jobCreate()

		then:
		1 * jobBuilderMock.build() >> job
		factoryBean.object == job
		0 * _
	}

	def "CREATE_SERIES step is created"() {
		when:
		Step step = etlJobConfiguration.stepCreateSeries()

		then: 'StepBuilder is retrieved'
		1 * stepBuilderFactoryMock.get(StepName.CREATE_SERIES) >> stepBuilderMock

		then: 'commit interval is configured'
		1 * stepsPropertiesMock.getCreateSeries() >> stepProperties
		1 * stepProperties.getCommitInterval() >> STEP_SIZE
		1 * stepBuilderMock.chunk(STEP_SIZE) >> simpleStepBuilderMock

		then: 'beans are retrieved from application context, then passed to builder'
		1 * applicationContextMock.getBean(SeriesReader) >> itemReaderMock
		1 * simpleStepBuilderMock.reader(itemReaderMock) >> simpleStepBuilderMock
		1 * applicationContextMock.getBean(SeriesProcessor) >> itemProcessorMock
		1 * simpleStepBuilderMock.processor(itemProcessorMock) >> simpleStepBuilderMock
		1 * applicationContextMock.getBean(SeriesWriter) >> itemWriterMock
		1 * simpleStepBuilderMock.writer(itemWriterMock) >> simpleStepBuilderMock
		1 * applicationContextMock.getBean(CommonStepExecutionListener) >> stepExecutionListenerMock
		1 * simpleStepBuilderMock.listener(stepExecutionListenerMock) >> simpleStepBuilderMock

		then: 'step is configured to run only once'
		1 * simpleStepBuilderMock.startLimit(1) >> simpleStepBuilderMock
		1 * simpleStepBuilderMock.allowStartIfComplete(false) >> simpleStepBuilderMock

		then: 'tasklet step is returned'
		1 * simpleStepBuilderMock.build() >> taskletStepMock

		then: 'step is being returned'
		step == taskletStepMock
	}

	def "CREATE_PERFORMERS step is created"() {
		when:
		Step step = etlJobConfiguration.stepCreatePerformers()

		then: 'StepBuilder is retrieved'
		1 * stepBuilderFactoryMock.get(StepName.CREATE_PERFORMERS) >> stepBuilderMock

		then: 'commit interval is configured'
		1 * stepsPropertiesMock.getCreatePerformers() >> stepProperties
		1 * stepProperties.getCommitInterval() >> STEP_SIZE
		1 * stepBuilderMock.chunk(STEP_SIZE) >> simpleStepBuilderMock

		then: 'beans are retrieved from application context, then passed to builder'
		1 * applicationContextMock.getBean(PerformerReader) >> itemReaderMock
		1 * simpleStepBuilderMock.reader(itemReaderMock) >> simpleStepBuilderMock
		1 * applicationContextMock.getBean(PerformerProcessor) >> itemProcessorMock
		1 * simpleStepBuilderMock.processor(itemProcessorMock) >> simpleStepBuilderMock
		1 * applicationContextMock.getBean(PerformerWriter) >> itemWriterMock
		1 * simpleStepBuilderMock.writer(itemWriterMock) >> simpleStepBuilderMock
		1 * applicationContextMock.getBean(CommonStepExecutionListener) >> stepExecutionListenerMock
		1 * simpleStepBuilderMock.listener(stepExecutionListenerMock) >> simpleStepBuilderMock

		then: 'step is configured to run only once'
		1 * simpleStepBuilderMock.startLimit(1) >> simpleStepBuilderMock
		1 * simpleStepBuilderMock.allowStartIfComplete(false) >> simpleStepBuilderMock

		then: 'tasklet step is returned'
		1 * simpleStepBuilderMock.build() >> taskletStepMock

		then: 'step is being returned'
		step == taskletStepMock
	}

	def "CREATE_STAFF step is created"() {
		when:
		Step step = etlJobConfiguration.stepCreateStaff()

		then: 'StepBuilder is retrieved'
		1 * stepBuilderFactoryMock.get(StepName.CREATE_STAFF) >> stepBuilderMock

		then: 'commit interval is configured'
		1 * stepsPropertiesMock.getCreateStaff() >> stepProperties
		1 * stepProperties.getCommitInterval() >> STEP_SIZE
		1 * stepBuilderMock.chunk(STEP_SIZE) >> simpleStepBuilderMock

		then: 'beans are retrieved from application context, then passed to builder'
		1 * applicationContextMock.getBean(StaffReader) >> itemReaderMock
		1 * simpleStepBuilderMock.reader(itemReaderMock) >> simpleStepBuilderMock
		1 * applicationContextMock.getBean(StaffProcessor) >> itemProcessorMock
		1 * simpleStepBuilderMock.processor(itemProcessorMock) >> simpleStepBuilderMock
		1 * applicationContextMock.getBean(StaffWriter) >> itemWriterMock
		1 * simpleStepBuilderMock.writer(itemWriterMock) >> simpleStepBuilderMock
		1 * applicationContextMock.getBean(CommonStepExecutionListener) >> stepExecutionListenerMock
		1 * simpleStepBuilderMock.listener(stepExecutionListenerMock) >> simpleStepBuilderMock

		then: 'step is configured to run only once'
		1 * simpleStepBuilderMock.startLimit(1) >> simpleStepBuilderMock
		1 * simpleStepBuilderMock.allowStartIfComplete(false) >> simpleStepBuilderMock

		then: 'tasklet step is returned'
		1 * simpleStepBuilderMock.build() >> taskletStepMock

		then: 'step is being returned'
		step == taskletStepMock
	}

	def "CREATE_CHARACTERS step is created"() {
		when:
		Step step = etlJobConfiguration.stepCreateCharacters()

		then: 'StepBuilder is retrieved'
		1 * stepBuilderFactoryMock.get(StepName.CREATE_CHARACTERS) >> stepBuilderMock

		then: 'commit interval is configured'
		1 * stepsPropertiesMock.getCreateCharacters() >> stepProperties
		1 * stepProperties.getCommitInterval() >> STEP_SIZE
		1 * stepBuilderMock.chunk(STEP_SIZE) >> simpleStepBuilderMock

		then: 'beans are retrieved from application context, then passed to builder'
		1 * applicationContextMock.getBean(CharacterReader) >> itemReaderMock
		1 * simpleStepBuilderMock.reader(itemReaderMock) >> simpleStepBuilderMock
		1 * applicationContextMock.getBean(CharacterProcessor) >> itemProcessorMock
		1 * simpleStepBuilderMock.processor(itemProcessorMock) >> simpleStepBuilderMock
		1 * applicationContextMock.getBean(CharacterWriter) >> itemWriterMock
		1 * simpleStepBuilderMock.writer(itemWriterMock) >> simpleStepBuilderMock
		1 * applicationContextMock.getBean(CommonStepExecutionListener) >> stepExecutionListenerMock
		1 * simpleStepBuilderMock.listener(stepExecutionListenerMock) >> simpleStepBuilderMock

		then: 'step is configured to run only once'
		1 * simpleStepBuilderMock.startLimit(1) >> simpleStepBuilderMock
		1 * simpleStepBuilderMock.allowStartIfComplete(false) >> simpleStepBuilderMock

		then: 'tasklet step is returned'
		1 * simpleStepBuilderMock.build() >> taskletStepMock

		then: 'step is being returned'
		step == taskletStepMock
	}

	def "CREATE_EPISODES step is created"() {
		when:
		Step step = etlJobConfiguration.stepCreateEpisodes()

		then: 'StepBuilder is retrieved'
		1 * stepBuilderFactoryMock.get(StepName.CREATE_EPISODES) >> stepBuilderMock

		then: 'commit interval is configured'
		1 * stepsPropertiesMock.getCreateEpisodes() >> stepProperties
		1 * stepProperties.getCommitInterval() >> STEP_SIZE
		1 * stepBuilderMock.chunk(STEP_SIZE) >> simpleStepBuilderMock

		then: 'beans are retrieved from application context, then passed to builder'
		1 * applicationContextMock.getBean(EpisodeReader) >> itemReaderMock
		1 * simpleStepBuilderMock.reader(itemReaderMock) >> simpleStepBuilderMock
		1 * applicationContextMock.getBean(EpisodeProcessor) >> itemProcessorMock
		1 * simpleStepBuilderMock.processor(itemProcessorMock) >> simpleStepBuilderMock
		1 * applicationContextMock.getBean(EpisodeWriter) >> itemWriterMock
		1 * simpleStepBuilderMock.writer(itemWriterMock) >> simpleStepBuilderMock
		1 * applicationContextMock.getBean(CommonStepExecutionListener) >> stepExecutionListenerMock
		1 * simpleStepBuilderMock.listener(stepExecutionListenerMock) >> simpleStepBuilderMock

		then: 'step is configured to run only once'
		1 * simpleStepBuilderMock.startLimit(1) >> simpleStepBuilderMock
		1 * simpleStepBuilderMock.allowStartIfComplete(false) >> simpleStepBuilderMock

		then: 'tasklet step is returned'
		1 * simpleStepBuilderMock.build() >> taskletStepMock

		then: 'step is being returned'
		step == taskletStepMock
	}

	def "CREATE_MOVIES step is created"() {
		when:
		Step step = etlJobConfiguration.stepCreateMovies()

		then: 'StepBuilder is retrieved'
		1 * stepBuilderFactoryMock.get(StepName.CREATE_MOVIES) >> stepBuilderMock

		then: 'commit interval is configured'
		1 * stepsPropertiesMock.getCreateMovies() >> stepProperties
		1 * stepProperties.getCommitInterval() >> STEP_SIZE
		1 * stepBuilderMock.chunk(STEP_SIZE) >> simpleStepBuilderMock

		then: 'beans are retrieved from application context, then passed to builder'
		1 * applicationContextMock.getBean(MovieReader) >> itemReaderMock
		1 * simpleStepBuilderMock.reader(itemReaderMock) >> simpleStepBuilderMock
		1 * applicationContextMock.getBean(MovieProcessor) >> itemProcessorMock
		1 * simpleStepBuilderMock.processor(itemProcessorMock) >> simpleStepBuilderMock
		1 * applicationContextMock.getBean(MovieWriter) >> itemWriterMock
		1 * simpleStepBuilderMock.writer(itemWriterMock) >> simpleStepBuilderMock
		1 * applicationContextMock.getBean(CommonStepExecutionListener) >> stepExecutionListenerMock
		1 * simpleStepBuilderMock.listener(stepExecutionListenerMock) >> simpleStepBuilderMock

		then: 'step is configured to run only once'
		1 * simpleStepBuilderMock.startLimit(1) >> simpleStepBuilderMock
		1 * simpleStepBuilderMock.allowStartIfComplete(false) >> simpleStepBuilderMock

		then: 'tasklet step is returned'
		1 * simpleStepBuilderMock.build() >> taskletStepMock

		then: 'step is being returned'
		step == taskletStepMock
	}

}