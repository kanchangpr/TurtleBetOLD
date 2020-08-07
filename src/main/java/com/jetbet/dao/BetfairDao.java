package com.jetbet.dao;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.jetbet.bean.FancyBean;
import com.jetbet.bean.MarketCatalogueBean;
import com.jetbet.bean.MatchBean;
import com.jetbet.bean.SeriesBean;
import com.jetbet.bean.SportsBean;
import com.jetbet.betfair.ApiNgOperations;
import com.jetbet.betfair.ApiNgRescriptOperations;
import com.jetbet.betfair.entities.CompetitionResult;
import com.jetbet.betfair.entities.EventResult;
import com.jetbet.betfair.entities.EventTypeResult;
import com.jetbet.betfair.entities.MarketCatalogue;
import com.jetbet.betfair.entities.MarketFancyResult;
import com.jetbet.betfair.entities.MarketFilter;
import com.jetbet.betfair.enums.MarketProjection;
import com.jetbet.betfair.enums.MarketSort;
import com.jetbet.betfair.exceptions.APINGException;
import com.jetbet.dto.SessionDetails;
import com.jetbet.repository.FancyRepository;
import com.jetbet.repository.MarketCatalogueRepository;
import com.jetbet.repository.MatchRepository;
import com.jetbet.repository.SeriesRepository;
import com.jetbet.repository.SportsRepository;
import com.jetbet.util.ResourceConstants;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Repository
public class BetfairDao {

	@Autowired
	private SportsRepository sportsRepository;

	@Autowired
	private SeriesRepository seriesRepository;

	@Autowired
	private MatchRepository matchRepository;

	@Autowired
	private FancyRepository fancyRepository;

	@Autowired
	private MarketCatalogueRepository marketCatalogueRepository;
	
	@Autowired
	JdbcTemplate jdbcTemplate;

	private ApiNgOperations rescriptOperations = ApiNgRescriptOperations.getInstance();
	private String applicationKey;
	private String sessionToken;

	@Transactional
	public List<SportsBean> getlistOfEventType(String appKey, String ssoid, String userName, String transactionId) {
		this.applicationKey = appKey;
		this.sessionToken = ssoid;
		List<EventTypeResult> response = new ArrayList<EventTypeResult>();
		final List<SportsBean> sportsBeanList = new ArrayList<SportsBean>();
		List<SportsBean> sportsBeanResponseList = new ArrayList<SportsBean>();
		try {
			MarketFilter marketFilter;
			marketFilter = new MarketFilter();
			log.info("[" + transactionId + "] (listEventTypes) Get all Event Types...\n");
			response = rescriptOperations.listEventTypes(marketFilter, applicationKey, sessionToken);
			response.stream().forEach(res -> {
				SportsBean sportsBean = new SportsBean();
				sportsBean.setSportsTypeId(res.getEventType().getId());
				sportsBean.setSportsName(res.getEventType().getName());
				sportsBean.setSportsMarketCount(res.getMarketCount());
				sportsBean.setSportsCreatedBy(userName);
				sportsBeanList.add(sportsBean);
			});
			sportsBeanResponseList = storeListOfEventTypesInDB(sportsBeanList, transactionId);
		} catch (APINGException apiExc) {
			log.info("[" + transactionId + "] " + apiExc.toString());
		}
		log.info("[" + transactionId + "] sportsBeanResponseList:: " + sportsBeanResponseList);
		return sportsBeanResponseList;
	}

	@Transactional
	public List<SeriesBean> getlistOfComp(String appKey, String ssoid, String userName, String transactionId) {
		this.applicationKey = appKey;
		this.sessionToken = ssoid;
		MarketFilter marketFilter;
		marketFilter = new MarketFilter();
		final List<SeriesBean> seriesBeanList = new ArrayList<SeriesBean>();
		List<SeriesBean> seriesBeanResponseList = new ArrayList<SeriesBean>();
		try {

			List<SportsBean> sportsBeansList = sportsRepository.findByIsActiveOrderBySportsName("Y");
			for (SportsBean sportsBean : sportsBeansList) {
				log.info("[" + transactionId + "] ****************INSIDE OUTER FOR*********");

				String eventTypeIdString = sportsBean.getSportsTypeId();
				log.info("eventTypeIdString: " + eventTypeIdString);
				Set<String> eventTypeIdSet = new HashSet<String>();
				eventTypeIdSet.add(eventTypeIdString);
				marketFilter.setEventTypeIds(eventTypeIdSet);

				log.info("[" + transactionId + "] (getlistOfComp) Get all Competition(Series)...\n");
				List<CompetitionResult> response = rescriptOperations.listComp(marketFilter, applicationKey,
						sessionToken);

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

			}
			seriesBeanResponseList = storeListOfCompDB(seriesBeanList, transactionId);
		} catch (APINGException apiExc) {
			log.info(apiExc.toString());
		}
		log.info("[" + transactionId + "] seriesBeanResponseList:: " + seriesBeanResponseList);
		return seriesBeanResponseList;
	}

	@Transactional
	public List<MatchBean> getlistOfMatches(String appKey, String ssoid, String userName, String transactionId) {

		this.applicationKey = appKey;
		this.sessionToken = ssoid;
		MarketFilter marketFilter;
		marketFilter = new MarketFilter();
		final List<MatchBean> matchBeanList = new ArrayList<MatchBean>();
		List<MatchBean> matchBeanResponseList = new ArrayList<MatchBean>();
		try {
			List<SportsBean> sportsBeansList = sportsRepository.findByIsActiveOrderBySportsName("Y");
			for (SportsBean sportsBean : sportsBeansList) {
				log.info("[" + transactionId + "] ***********INSIDE SPORTS LOOP*********");

				String eventTypeIdString = sportsBean.getSportsTypeId();
				log.info("eventTypeIdString: " + eventTypeIdString);

				Set<String> eventTypeIdSet = new HashSet<String>();
				eventTypeIdSet.add(eventTypeIdString);
				marketFilter.setEventTypeIds(eventTypeIdSet);

				List<SeriesBean> seriesBeanList = seriesRepository
						.findBySportIdAndIsActiveOrderBySportId(eventTypeIdString, "Y");

				for (SeriesBean seriesBean : seriesBeanList) {
					log.info("[" + transactionId + "] ***********INSIDE SERIES LOOP*********");

					String seriesId = seriesBean.getSeriesId();
					log.info("[" + transactionId + "] seriesId: " + seriesId + " of Sports Id: " + eventTypeIdString);
					Set<String> seriesIdSet = new HashSet<String>();
					seriesIdSet.add(seriesId);
					marketFilter.setCompetitionIds(seriesIdSet);

					log.info("[" + transactionId + "] ***********Fecting Matches of " + sportsBean.getSportsName()
							+ " : " + seriesBean.getSeriesName() + " *********");
					List<EventResult> response = rescriptOperations.listEvents(marketFilter, applicationKey,
							sessionToken);

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
				}
			}
			matchBeanResponseList = storeListOfMatchDB(matchBeanList, transactionId);

			log.info("[" + transactionId + "] response: " + matchBeanResponseList);
		} catch (APINGException apiExc) {
			log.info(apiExc.toString());
		}
		log.info("[" + transactionId + "] matchBeanResponseList:: " + matchBeanResponseList);
		return matchBeanResponseList;
	}

	@Transactional
	public List<FancyBean> getListOfOdds(String appKey, String ssoid, String userName, String transactionId) {

		this.applicationKey = appKey;
		this.sessionToken = ssoid;
		MarketFilter marketFilter;
		marketFilter = new MarketFilter();
		final List<FancyBean> fancyBeanList = new ArrayList<FancyBean>();
		List<FancyBean> fancyBeanResponseList = new ArrayList<FancyBean>();
		try {

			List<MatchBean> matchBeanList = matchRepository.findByIsActive("Y");

			for (MatchBean matchBean : matchBeanList) {
				log.info("[" + transactionId + "] ***********INSIDE Match LOOP*********");

				String matchId = matchBean.getMatchId();
				log.info("[" + transactionId + "]matchId: " + matchId);
				Set<String> matchIdSet = new HashSet<String>();
				matchIdSet.add(matchId);
				marketFilter.setEventIds(matchIdSet);

				List<MarketFancyResult> response = rescriptOperations.listFancy(marketFilter, applicationKey,
						sessionToken);

				response.stream().forEach(res -> {
					FancyBean fancyBean = new FancyBean();
					fancyBean.setMarketType(res.getMarketType());
					fancyBean.setMarketCount(res.getMarketCount());
					fancyBean.setMatchId(matchId);
					fancyBean.setFancyCreatedBy(userName);
					fancyBeanList.add(fancyBean);
				});
			}
			log.info("fancyBeanList:: "+fancyBeanList);
			
			fancyBeanResponseList = storeListOfFancyDB(fancyBeanList, transactionId);
			log.info("[" + transactionId + "] response after storeListOfFancyDB: " + fancyBeanResponseList);

		} catch (APINGException apiExc) {
			log.info("[" + transactionId + "] " + apiExc.toString());
		}
		return fancyBeanResponseList;
	}

	@Transactional
	public List<MarketCatalogueBean> getMarketCatalogue(String appKey, String ssoid, String userName,
			String transactionId) {
		this.applicationKey = appKey;
		this.sessionToken = ssoid;
		MarketFilter marketFilter;
		List<MarketCatalogue> marketCatalogueResult = null;
		// List<MarketBook> marketBookResult = null;
		// MarketBookCatalogue marketBookCatalogue = new MarketBookCatalogue();
		final List<MarketCatalogueBean> marketCatalogueList = new ArrayList<MarketCatalogueBean>();
		List<MarketCatalogueBean> marketCatalogueResList = new ArrayList<MarketCatalogueBean>();
		try {
			String maxResults = "100";

			List<FancyBean> marketTypeList = fancyRepository.findByIsActive("Y");
			for (int i = 0; i < marketTypeList.size(); i++) {
				Set<String> typesCode = new HashSet<String>();
				typesCode.add(marketTypeList.get(i).getMarketType());
				Set<String> eventIds = new HashSet<String>();
				eventIds.add(marketTypeList.get(i).getMatchId());

				log.info("[" + transactionId + "] MatchID: " + marketTypeList.get(i).getMatchId() + " Market Type: "
						+ marketTypeList.get(i).getMarketType());

				marketFilter = new MarketFilter();
				marketFilter.setEventIds(eventIds);
				marketFilter.setMarketTypeCodes(typesCode);
				Set<MarketProjection> marketProjection = new HashSet<MarketProjection>();
				marketProjection.add(MarketProjection.COMPETITION);
				marketProjection.add(MarketProjection.EVENT);
				marketProjection.add(MarketProjection.EVENT_TYPE);
				marketProjection.add(MarketProjection.MARKET_START_TIME);
				marketProjection.add(MarketProjection.MARKET_DESCRIPTION);
				marketProjection.add(MarketProjection.RUNNER_DESCRIPTION);
				marketProjection.add(MarketProjection.RUNNER_METADATA);

				marketCatalogueResult = rescriptOperations.listMarketCatalogue(marketFilter, marketProjection,
						MarketSort.FIRST_TO_START, maxResults, applicationKey, sessionToken);

				marketCatalogueResult.stream().forEach(res -> {
					for (int j = 0; j < res.getRunners().size(); j++) {
						MarketCatalogueBean marketCatalogueBean = new MarketCatalogueBean();
						marketCatalogueBean.setMarketId(res.getMarketId());
						marketCatalogueBean.setMarketType(res.getDescription().getMarketType());
						marketCatalogueBean.setMarketName(res.getMarketName());
						marketCatalogueBean.setMarketStartTime(res.getMarketStartTime());
						marketCatalogueBean.setTotalMatched(res.getTotalMatched());
						marketCatalogueBean.setRunnerSelectionId(res.getRunners().get(j).getSelectionId());
						marketCatalogueBean.setRunnerName(res.getRunners().get(j).getRunnerName());
						marketCatalogueBean.setHandicap(res.getRunners().get(j).getHandicap());
						marketCatalogueBean.setSortPriority(res.getRunners().get(j).getSortPriority());
						marketCatalogueBean.setSportsId(res.getEventType().getId());
						marketCatalogueBean.setSprotsName(res.getEventType().getName());
						marketCatalogueBean.setSeriesId(res.getCompetition().getId());
						marketCatalogueBean.setSeriesName(res.getCompetition().getName());
						marketCatalogueBean.setMatchId(res.getEvent().getId());
						marketCatalogueBean.setMatchName(res.getEvent().getName());
						marketCatalogueBean.setMatchCountryCode(res.getEvent().getCountryCode());
						marketCatalogueBean.setMatchTimeZone(res.getEvent().getTimezone());
						marketCatalogueBean.setMatchVenue(res.getEvent().getVenue());
						marketCatalogueBean.setMatchOpenDate(res.getEvent().getOpenDate());
						marketCatalogueBean.setCreatedBy(ResourceConstants.USER_NAME);
						marketCatalogueList.add(marketCatalogueBean);
					}
				});
				marketCatalogueResList = storeMarketCatalogueDB(marketCatalogueList, transactionId);

			}

			log.info("Market Catalogue inseted in  DB: " + marketCatalogueList);

			/*
			 * String marketId = marketCatalogueResult.get(0).getMarketId();
			 * 
			 * PriceProjection priceProjection = new PriceProjection(); Set<PriceData>
			 * priceData = new HashSet<PriceData>();
			 * priceData.add(PriceData.EX_BEST_OFFERS);
			 * priceProjection.setPriceData(priceData);
			 * 
			 * // In this case we don't need these objects so they are declared null
			 * OrderProjection orderProjection = null; MatchProjection matchProjection =
			 * null; String currencyCode = null;
			 * 
			 * List<String> marketIds = new ArrayList<String>(); marketIds.add(marketId);
			 * 
			 * marketBookResult = rescriptOperations.listMarketBook(marketIds,
			 * priceProjection, orderProjection, matchProjection, currencyCode,
			 * applicationKey, sessionToken);
			 * 
			 * marketBookCatalogue.setMarketCatalogue(marketCatalogueResult);
			 * marketBookCatalogue.setMarketBook(marketBookResult);
			 */

		} catch (APINGException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return marketCatalogueList;

	}

	@Transactional
	public List<SportsBean> storeListOfEventTypesInDB(List<SportsBean> sportsBeanList, String transactionId) {
		List<SportsBean> responseBeanList = new ArrayList<SportsBean>();
		for (SportsBean sportsBean : sportsBeanList) {
			String sportsId = sportsBean.getSportsTypeId();
			long getRowCount = sportsRepository.countBySportsTypeId(sportsId);
			if (getRowCount == 0) {
				SportsBean responseBean = new SportsBean();
				log.info("[" + transactionId + "] inside if sportsId: " + sportsId);
				responseBean = sportsRepository.saveAndFlush(sportsBean);
				responseBeanList.add(responseBean);
			} else {
				log.info("[" + transactionId + "] inside Else sportsId: " + sportsId);
			}
		}
		log.info("responseBeanList:: " + responseBeanList);
		return responseBeanList;
	}

	@Transactional
	private List<SeriesBean> storeListOfCompDB(List<SeriesBean> seriesBeanList, String transactionId) {
		List<SeriesBean> responseBeanList = new ArrayList<SeriesBean>();
		for (SeriesBean seriesBean : seriesBeanList) {
			String seriesId = seriesBean.getSeriesId();
			long getRowCount = seriesRepository.countBySeriesId(seriesId);
			if (getRowCount == 0) {
				SeriesBean responseBean = new SeriesBean();
				log.info("[" + transactionId + "] inside if seriesId: " + seriesId);
				responseBean = seriesRepository.saveAndFlush(seriesBean);
				responseBeanList.add(responseBean);
			} else {
				log.info("[" + transactionId + "] inside Else seriesId: " + seriesId);
			}
		}
		log.info("responseBeanList:: " + responseBeanList);
		return responseBeanList;
	}

	@Transactional
	private List<MatchBean> storeListOfMatchDB(List<MatchBean> matchBeanList, String transactionId) {
		List<MatchBean> responseBeanList = new ArrayList<MatchBean>();
		for (MatchBean matchBean : matchBeanList) {
			String matchId = matchBean.getMatchId();
			long getRowCount = matchRepository.countByMatchId(matchId);
			if (getRowCount == 0) {
				MatchBean responseBean = new MatchBean();
				log.info("[" + transactionId + "] inside if matchId: " + matchId);
				responseBean = matchRepository.saveAndFlush(matchBean);
				responseBeanList.add(responseBean);
			} else {
				log.info("[" + transactionId + "] inside Else matchId: " + matchId);
			}
		}
		log.info("responseBeanList:: " + responseBeanList);
		return responseBeanList;
	}

	@Transactional
	private List<FancyBean> storeListOfFancyDB(List<FancyBean> fancyBeanList, String transactionId) {
		log.info("[" + transactionId+ "]##############################Inside  storeListOfFancyDB#############################");
		List<FancyBean> responseBeanList = new ArrayList<FancyBean>();
		for (FancyBean fancyBean : fancyBeanList) {
			log.info("marketType & Match ID " + fancyBean.getMarketType() + " : " + fancyBean.getMatchId());
			long getRowCount = fancyRepository.countByMarketTypeAndMatchId(fancyBean.getMarketType(),fancyBean.getMatchId());
			if (getRowCount == 0) {
				log.info("[" + transactionId + "] inside if marketType & Match ID " + fancyBean.getMarketType() + " : "+ fancyBean.getMatchId());
				responseBeanList.add(fancyRepository.save(fancyBean));
			} else {
				log.info("[" + transactionId + "] inside Else marketType & Match ID " + fancyBean.getMarketType()+ " : " + fancyBean.getMatchId());
			}
		}
		log.info("responseBeanList:: " + responseBeanList);
		return responseBeanList;
	}

	@Transactional
	private List<MarketCatalogueBean> storeMarketCatalogueDB(List<MarketCatalogueBean> marketCatalogueList,
			String transactionId) {
		log.info("[" + transactionId
				+ "]##############################Inside  storeMarketCatalogueDB#############################");
		List<MarketCatalogueBean> responseBeanList = new ArrayList<MarketCatalogueBean>();
		for (MarketCatalogueBean marketCatalogueBean : marketCatalogueList) {
			String marketId = marketCatalogueBean.getMarketId();
			Long selectionId = marketCatalogueBean.getRunnerSelectionId();
			long getRowCount = marketCatalogueRepository.countByMarketIdAndRunnerSelectionId(marketId, selectionId);
			if (getRowCount == 0) {
				MarketCatalogueBean responseBean = new MarketCatalogueBean();
				log.info("[" + transactionId + "] inside if MarketId: " + marketId);
				responseBean = marketCatalogueRepository.saveAndFlush(marketCatalogueBean);
				responseBeanList.add(responseBean);
			} else {
				log.info("[" + transactionId + "] inside Else MarketId: " + marketId);
			}
		}
		log.info("responseBeanList:: " + responseBeanList);
		return responseBeanList;
	}

	@Transactional
	public SessionDetails getSessionToken(String userName, String password, String transactionId) {
		SessionDetails response = rescriptOperations.getSessionToken(userName, password, transactionId);
		return response;
	}

}
