package com.jetbet.dao;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.jetbet.bean.FancyBean;
import com.jetbet.bean.MatchBean;
import com.jetbet.bean.SeriesBean;
import com.jetbet.bean.SportsBean;
import com.jetbet.betfair.ApiNgOperations;
import com.jetbet.betfair.ApiNgRescriptOperations;
import com.jetbet.betfair.entities.CompetitionResult;
import com.jetbet.betfair.entities.EventResult;
import com.jetbet.betfair.entities.EventTypeResult;
import com.jetbet.betfair.entities.MarketFancyResult;
import com.jetbet.betfair.entities.MarketFilter;
import com.jetbet.betfair.exceptions.APINGException;
import com.jetbet.dto.SessionDetails;
import com.jetbet.repository.FancyRepository;
import com.jetbet.repository.MatchRepository;
import com.jetbet.repository.SeriesRepository;
import com.jetbet.repository.SportsRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Repository
public class BetfairDao {

	@Autowired
	SportsRepository sportsRepository;
	
	@Autowired
	SeriesRepository seriesRepository;
	
	@Autowired
	MatchRepository matchRepository;
	
	@Autowired
	FancyRepository fancyRepository;

	private ApiNgOperations rescriptOperations = ApiNgRescriptOperations.getInstance();
	private String applicationKey;
	private String sessionToken;

	public List<SportsBean> getlistOfEventType(String appKey, String ssoid, String userName, String transactionId) {
		this.applicationKey = appKey;
		this.sessionToken = ssoid;
		List<EventTypeResult> response = new ArrayList<EventTypeResult>();
		final List<SportsBean> sportsBeanList = new ArrayList<SportsBean>();
		List<SportsBean> sportsBeanResponseList = new ArrayList<SportsBean>();
		try {
			MarketFilter marketFilter;
			marketFilter = new MarketFilter();
			log.info("["+transactionId+"] (listEventTypes) Get all Event Types...\n");
			response = rescriptOperations.listEventTypes(marketFilter, applicationKey, sessionToken);
			response.stream().forEach(res -> {
				SportsBean sportsBean = new SportsBean();
				sportsBean.setSportsTypeId(res.getEventType().getId());
				sportsBean.setSportsName(res.getEventType().getName());
				sportsBean.setSportsMarketCount(res.getMarketCount());
				sportsBean.setSportsCreatedBy(userName);
				sportsBeanList.add(sportsBean);
			    });
			sportsBeanResponseList=storeListOfEventTypesInDB(sportsBeanList,transactionId);
		} catch (APINGException apiExc) {
			log.info("["+transactionId+"] "+apiExc.toString());
		}
		log.info("["+transactionId+"] sportsBeanResponseList:: "+sportsBeanResponseList);
		return sportsBeanResponseList;
	}

	public List<SeriesBean> getlistOfComp(String appKey, String ssoid, String userName, String transactionId) {
		this.applicationKey = appKey;
		this.sessionToken = ssoid;
		MarketFilter marketFilter;
		marketFilter = new MarketFilter();
		final List<SeriesBean> seriesBeanList = new ArrayList<SeriesBean>();
		List<SeriesBean> seriesBeanResponseList = new ArrayList<SeriesBean>();
		try {
			
			List<SportsBean> sportsBeansList = sportsRepository.findByIsActive("Y");
			for (SportsBean sportsBean : sportsBeansList) {
				log.info("["+transactionId+"] ****************INSIDE OUTER FOR*********");
				
				String eventTypeIdString = sportsBean.getSportsTypeId();
				log.info("eventTypeIdString: " + eventTypeIdString);
				Set<String> eventTypeIdSet = new HashSet<String>();
				eventTypeIdSet.add(eventTypeIdString);
				marketFilter.setEventTypeIds(eventTypeIdSet);

				log.info("["+transactionId+"] (getlistOfComp) Get all Competition(Series)...\n");
				List<CompetitionResult> response=rescriptOperations.listComp(marketFilter, applicationKey, sessionToken);
				
				response.stream().forEach(res -> {
					SeriesBean seriesBean = new SeriesBean();
					seriesBean.setSportId(eventTypeIdString);
					seriesBean.setSeriesId(res.getCompetition().getId());
					seriesBean.setSeriesName(res.getCompetition().getName());
					seriesBean.setSeriesMarketCount(res.getMarketCount());
					seriesBean.setSeriesCompRegion(res.getCompetitionRegion());
					seriesBean.setSeriesCreatedBy(userName);
					seriesBeanList.add(seriesBean);
				    });
				
				seriesBeanResponseList=storeListOfCompDB(seriesBeanList,transactionId);
			
			}

		} catch (APINGException apiExc) {
			log.info(apiExc.toString());
		}
		log.info("["+transactionId+"] seriesBeanResponseList:: "+seriesBeanResponseList);
		return seriesBeanResponseList;
	}

	
	public List<MatchBean> getlistOfMatches(String appKey, String ssoid, String userName, String transactionId) {

		this.applicationKey = appKey;
		this.sessionToken = ssoid;
		MarketFilter marketFilter;
		marketFilter = new MarketFilter();
		final List<MatchBean> matchBeanList = new ArrayList<MatchBean>();
		List<MatchBean> matchBeanResponseList = new ArrayList<MatchBean>();
		try {
			List<SportsBean> sportsBeansList = sportsRepository.findByIsActive("Y");
			for (SportsBean sportsBean : sportsBeansList) {
				log.info("[" + transactionId + "] ***********INSIDE SPORTS LOOP*********");

				String eventTypeIdString = sportsBean.getSportsTypeId();
				log.info("eventTypeIdString: " + eventTypeIdString);

				Set<String> eventTypeIdSet = new HashSet<String>();
				eventTypeIdSet.add(eventTypeIdString);
				marketFilter.setEventTypeIds(eventTypeIdSet);

				List<SeriesBean> seriesBeanList = seriesRepository.findBySportIdAndIsActive(eventTypeIdString,"Y");

				for (SeriesBean seriesBean : seriesBeanList) {
					log.info("[" + transactionId + "] ***********INSIDE SERIES LOOP*********");

					String seriesId = seriesBean.getSeriesId();
					log.info("[" + transactionId + "] seriesId: " + seriesId + " of Sports Id: " + eventTypeIdString);
					Set<String> seriesIdSet = new HashSet<String>();
					seriesIdSet.add(seriesId);
					marketFilter.setCompetitionIds(seriesIdSet);

					log.info("[" + transactionId + "] ***********Fecting Matches of " + sportsBean.getSportsName()
							+ " : " + seriesBean.getSeriesName() + " *********");
					List<EventResult> response = rescriptOperations.listEvents(marketFilter, applicationKey, sessionToken);
					
					response.stream().forEach(res -> {
						MatchBean matchBean = new MatchBean();
						matchBean.setMatchId(res.getEvent().getId());
						matchBean.setMatchName(res.getEvent().getName());
						matchBean.setMatchCountryCode(res.getEvent().getCountryCode());
						matchBean.setMatchTimezone(res.getEvent().getTimezone());
						matchBean.setMatchVenue(res.getEvent().getVenue());
						matchBean.setMatchOpenDate(res.getEvent().getOpenDate());
						matchBean.setMatchMarketCount(res.getMarketCount());
						matchBean.setSportId(eventTypeIdString);
						matchBean.setSeriesId(seriesId);
						matchBean.setMatchCreatedBy(userName);
						matchBeanList.add(matchBean);
					    });
					
					matchBeanResponseList=storeListOfMatchDB(matchBeanList,transactionId);
					
					log.info("[" + transactionId + "] response: " + response);
				}

			}
		} catch (APINGException apiExc) {
			log.info(apiExc.toString());
		}
		log.info("["+transactionId+"] matchBeanResponseList:: "+matchBeanResponseList);
		return matchBeanResponseList;
	}

	
	public List<FancyBean> getListOfOdds(String appKey, String ssoid, String userName, String transactionId) {

		this.applicationKey = appKey;
		this.sessionToken = ssoid;
		MarketFilter marketFilter;
		marketFilter = new MarketFilter();
		final List<FancyBean> fancyBeanList = new ArrayList<FancyBean>();
		List<FancyBean> fancyBeanResponseList = new ArrayList<FancyBean>();
		try {
			List<SportsBean> sportsBeansList = sportsRepository.findByIsActive("Y");
			for (SportsBean sportsBean : sportsBeansList) {
				log.info("[" + transactionId + "] ***********INSIDE SPORTS LOOP*********");

				String eventTypeIdString = sportsBean.getSportsTypeId();
				log.info("eventTypeIdString: " + eventTypeIdString);

				Set<String> eventTypeIdSet = new HashSet<String>();
				eventTypeIdSet.add(eventTypeIdString);
				marketFilter.setEventTypeIds(eventTypeIdSet);

				List<SeriesBean> seriesBeanList = seriesRepository.findBySportIdAndIsActive(eventTypeIdString,"Y");

				for (SeriesBean seriesBean : seriesBeanList) {
					log.info("[" + transactionId + "] ***********INSIDE SERIES LOOP*********");

					String seriesId = seriesBean.getSeriesId();
					log.info("[" + transactionId + "] seriesId: " + seriesId + " of Sports Id: " + eventTypeIdString);
					Set<String> seriesIdSet = new HashSet<String>();
					seriesIdSet.add(seriesId);
					marketFilter.setCompetitionIds(seriesIdSet);
					
					log.info("[" + transactionId + "] ***********Fecting Matches of " + sportsBean.getSportsName()
							+ " : " + seriesBean.getSeriesName() + " *********");
					List<MarketFancyResult> response = rescriptOperations.listFancy(marketFilter, applicationKey, sessionToken);
					
					response.stream().forEach(res -> {
						FancyBean fancyBean = new FancyBean();
						fancyBean.setMarketType(res.getMarketType());
						fancyBean.setMarketCount(res.getMarketCount());
						fancyBean.setSportId(eventTypeIdString);
						fancyBean.setSeriesId(seriesId);
						fancyBean.setFancyCreatedBy(userName);
						fancyBeanList.add(fancyBean);
					    });
					
					fancyBeanResponseList=storeListOfFancyDB(fancyBeanList,transactionId);
					
					log.info("[" + transactionId + "] response: " + response);
				}

			}
		} catch (APINGException apiExc) {
			log.info("["+transactionId+"] "+apiExc.toString());
		}
		return fancyBeanResponseList;
	}



	public List<SportsBean> storeListOfEventTypesInDB(List<SportsBean> sportsBeanList, String transactionId) {
		List<SportsBean> responseBeanList = new ArrayList<SportsBean>();
		for (SportsBean sportsBean : sportsBeanList) {
			String sportsId = sportsBean.getSportsTypeId();
			long getRowCount = sportsRepository.countBySportsTypeId(sportsId);
			if (getRowCount == 0) {
				SportsBean responseBean = new SportsBean();
				log.info("["+transactionId+"] inside if sportsId: " + sportsId);
				responseBean=sportsRepository.saveAndFlush(sportsBean);
				responseBeanList.add(responseBean);
			} else {
				log.info("["+transactionId+"] inside Else sportsId: " + sportsId);
			}
		}
		log.info("responseBeanList:: "+responseBeanList);
		return responseBeanList;
	}
	
	private List<SeriesBean> storeListOfCompDB(List<SeriesBean> seriesBeanList, String transactionId) {
		List<SeriesBean> responseBeanList = new ArrayList<SeriesBean>();
		for (SeriesBean seriesBean : seriesBeanList) {
			String seriesId = seriesBean.getSeriesId();
			long getRowCount = seriesRepository.countBySeriesId(seriesId);
			if (getRowCount == 0) {
				SeriesBean responseBean = new SeriesBean();
				log.info("["+transactionId+"] inside if seriesId: " + seriesId);
				responseBean=seriesRepository.saveAndFlush(seriesBean);
				responseBeanList.add(responseBean);
			} else {
				log.info("["+transactionId+"] inside Else seriesId: " + seriesId);
			}
		}
		log.info("responseBeanList:: "+responseBeanList);
		return responseBeanList;
	}
	
	private List<MatchBean> storeListOfMatchDB(List<MatchBean> matchBeanList, String transactionId) {
		List<MatchBean> responseBeanList = new ArrayList<MatchBean>();
		for (MatchBean matchBean : matchBeanList) {
			String matchId = matchBean.getMatchId();
			long getRowCount = matchRepository.countByMatchId(matchId);
			if (getRowCount == 0) {
				MatchBean responseBean = new MatchBean();
				log.info("["+transactionId+"] inside if matchId: " + matchId);
				responseBean=matchRepository.saveAndFlush(matchBean);
				responseBeanList.add(responseBean);
			} else {
				log.info("["+transactionId+"] inside Else matchId: " + matchId);
			}
		}
		log.info("responseBeanList:: "+responseBeanList);
		return responseBeanList;
	}
	
	private List<FancyBean> storeListOfFancyDB(List<FancyBean> fancyBeanList, String transactionId) {
		List<FancyBean> responseBeanList = new ArrayList<FancyBean>();
		for (FancyBean fancyBean : fancyBeanList) {
			String marketType = fancyBean.getMarketType();
			long getRowCount = fancyRepository.countByMarketType(marketType);
			if (getRowCount == 0) {
				FancyBean responseBean = new FancyBean();
				log.info("["+transactionId+"] inside if marketType: " + marketType);
				responseBean=fancyRepository.saveAndFlush(fancyBean);
				responseBeanList.add(responseBean);
			} else {
				log.info("["+transactionId+"] inside Else marketType: " + marketType);
			}
		}
		log.info("responseBeanList:: "+responseBeanList);
		return responseBeanList;
	}

	public SessionDetails getSessionToken(String userName, String password, String transactionId) {
		SessionDetails response = rescriptOperations.getSessionToken(userName, password,transactionId);
		return response;
	}

	
}
