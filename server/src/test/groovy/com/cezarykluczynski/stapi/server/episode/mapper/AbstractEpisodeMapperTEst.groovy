package com.cezarykluczynski.stapi.server.episode.mapper

import com.cezarykluczynski.stapi.util.tool.LogicUtil
import com.sun.org.apache.xerces.internal.jaxp.datatype.XMLGregorianCalendarImpl
import spock.lang.Specification

import javax.xml.datatype.DatatypeConstants
import javax.xml.datatype.XMLGregorianCalendar
import java.time.LocalDate

abstract class AbstractEpisodeMapperTest extends Specification {

	protected static final String GUID = 'GUID'
	protected static final String TITLE = 'TITLE'
	protected static final Integer SEASON_NUMBER = 1
	protected static final Integer SEASON_NUMBER_FROM = 2
	protected static final Integer SEASON_NUMBER_TO = 3
	protected static final Integer EPISODE_NUMBER = 4
	protected static final Integer EPISODE_NUMBER_FROM = 5
	protected static final Integer EPISODE_NUMBER_TO = 6
	protected static final String PRODUCTION_SERIAL_NUMBER = 'PRODUCTION_SERIAL_NUMBER'
	protected static final Boolean FEATURE_LENGTH = LogicUtil.nextBoolean()
	protected static final Float STARDATE = (Float) 1515.9
	protected static final Float STARDATE_FROM = (Float) 1514.2
	protected static final Float STARDATE_TO = (Float) 1517.5
	protected static final Integer YEAR = 2370
	protected static final Integer YEAR_FROM = 2350
	protected static final Integer YEAR_TO = 2390
	protected static final LocalDate US_AIR_DATE = LocalDate.of(1990, 8, 4)
	protected static final LocalDate US_AIR_DATE_FROM = LocalDate.of(1991, 1, 2)
	protected static final LocalDate US_AIR_DATE_TO = LocalDate.of(1993, 3, 4)
	protected static final LocalDate FINAL_SCRIPT_DATE = LocalDate.of(1989, 4, 2)
	protected static final LocalDate FINAL_SCRIPT_DATE_FROM = LocalDate.of(1996, 5, 6)
	protected static final LocalDate FINAL_SCRIPT_DATE_TO = LocalDate.of(1998, 7, 8)
	protected static final XMLGregorianCalendar US_AIR_DATE_XML = XMLGregorianCalendarImpl
			.createDate(1990, 8, 4, DatatypeConstants.FIELD_UNDEFINED)
	protected static final XMLGregorianCalendar US_AIR_DATE_FROM_XML = XMLGregorianCalendarImpl
			.createDate(1991, 1, 2, DatatypeConstants.FIELD_UNDEFINED)
	protected static final XMLGregorianCalendar US_AIR_DATE_TO_XML = XMLGregorianCalendarImpl
			.createDate(1993, 3, 4, DatatypeConstants.FIELD_UNDEFINED)
	protected static final XMLGregorianCalendar FINAL_SCRIPT_DATE_XML = XMLGregorianCalendarImpl
			.createDate(1989, 4, 2, DatatypeConstants.FIELD_UNDEFINED)
	protected static final XMLGregorianCalendar FINAL_SCRIPT_DATE_FROM_XML = XMLGregorianCalendarImpl
			.createDate(1996, 5, 6, DatatypeConstants.FIELD_UNDEFINED)
	protected static final XMLGregorianCalendar FINAL_SCRIPT_DATE_TO_XML = XMLGregorianCalendarImpl
			.createDate(1998, 7, 8, DatatypeConstants.FIELD_UNDEFINED)

}
