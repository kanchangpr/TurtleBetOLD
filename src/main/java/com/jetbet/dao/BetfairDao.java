package com.jetbet.dao;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.transaction.Transactional;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.validator.constraints.Length;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.jetbet.bean.FancyBean;
import com.jetbet.bean.MarketCatalogueBean;
import com.jetbet.bean.MatchBean;
import com.jetbet.bean.PartnershipBean;
import com.jetbet.bean.PlaceBetsBean;
import com.jetbet.bean.SeriesBean;
import com.jetbet.bean.SportsBean;
import com.jetbet.bean.UserBean;
import com.jetbet.betfair.ApiNgOperations;
import com.jetbet.betfair.ApiNgRescriptOperations;
import com.jetbet.betfair.entities.CompetitionResult;
import com.jetbet.betfair.entities.EventResult;
import com.jetbet.betfair.entities.EventTypeResult;
import com.jetbet.betfair.entities.MarketBook;
import com.jetbet.betfair.entities.MarketCatalogue;
import com.jetbet.betfair.entities.MarketFancyResult;
import com.jetbet.betfair.entities.MarketFilter;
import com.jetbet.betfair.entities.PriceProjection;
import com.jetbet.betfair.enums.MarketProjection;
import com.jetbet.betfair.enums.MarketSort;
import com.jetbet.betfair.enums.MatchProjection;
import com.jetbet.betfair.enums.OrderProjection;
import com.jetbet.betfair.enums.PriceData;
import com.jetbet.betfair.exceptions.APINGException;
import com.jetbet.dto.DashboardMatchListDto;
import com.jetbet.dto.FancyIdDto;
import com.jetbet.dto.MatchAndFancyDetailDto;
import com.jetbet.dto.RunnerPriceAndSize;
import com.jetbet.dto.SeriesMatchFancyResponseDto;
import com.jetbet.dto.SessionDetails;
import com.jetbet.dto.UserRoleDto;
import com.jetbet.repository.FancyRepository;
import com.jetbet.repository.MarketCatalogueRepository;
import com.jetbet.repository.MatchRepository;
import com.jetbet.repository.PartnershipRepository;
import com.jetbet.repository.PlaceBetsRepository;
import com.jetbet.repository.SeriesRepository;
import com.jetbet.repository.SportsRepository;
import com.jetbet.repository.UserRepository;
import com.jetbet.util.QueryListConstant;
import com.jetbet.util.ResourceConstants;
import com.sun.istack.FinalArrayList;

import lombok.extern.java.Log;
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
	private PlaceBetsRepository placeBetsRepository;

	@Autowired
	private PartnershipRepository partnershipRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	JdbcTemplate jdbcTemplate;
	
	public static String appKey;
	public static String ssToken;

	public static DecimalFormat df = new DecimalFormat("0.00");

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
			// log.info("[" + transactionId + "] (listEventTypes) Get all Event
			// Types...\n");
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
			// log.info("[" + transactionId + "] " + apiExc.toString());
		}
		// log.info("[" + transactionId + "] sportsBeanResponseList:: " +
		// sportsBeanResponseList);
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
				// log.info("[" + transactionId + "] ****************INSIDE OUTER
				// FOR*********");

				String eventTypeIdString = sportsBean.getSportsTypeId();
				// log.info("eventTypeIdString: " + eventTypeIdString);
				Set<String> eventTypeIdSet = new HashSet<String>();
				eventTypeIdSet.add(eventTypeIdString);
				marketFilter.setEventTypeIds(eventTypeIdSet);

				// log.info("[" + transactionId + "] (getlistOfComp) Get all
				// Competition(Series)...\n");
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
		// log.info("[" + transactionId + "] seriesBeanResponseList:: " +
		// seriesBeanResponseList);
		return seriesBeanResponseList;
	}
	
	
	public List<SeriesBean> updateListOfSeries(String userName, String sportId, String transactionId) {
		
		MarketFilter marketFilter;
		marketFilter = new MarketFilter();
		final List<SeriesBean> seriesBeanList = new ArrayList<SeriesBean>();
		List<SeriesBean> seriesBeanResponseList = new ArrayList<SeriesBean>();
		try {

			List<SportsBean> sportsBeansList = sportsRepository.findByIsActiveAndSportsTypeIdOrderBySportsName(sportId,"Y");
			for (SportsBean sportsBean : sportsBeansList) {
				// log.info("[" + transactionId + "] ****************INSIDE OUTER
				// FOR*********");

				String eventTypeIdString = sportsBean.getSportsTypeId();
				// log.info("eventTypeIdString: " + eventTypeIdString);
				Set<String> eventTypeIdSet = new HashSet<String>();
				eventTypeIdSet.add(eventTypeIdString);
				marketFilter.setEventTypeIds(eventTypeIdSet);

				// log.info("[" + transactionId + "] (getlistOfComp) Get all
				// Competition(Series)...\n");
				List<CompetitionResult> response = rescriptOperations.listComp(marketFilter, appKey,
						ssToken);

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
		// log.info("[" + transactionId + "] seriesBeanResponseList:: " +
		// seriesBeanResponseList);
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
				// log.info("[" + transactionId + "] ***********INSIDE SPORTS LOOP*********");

				String eventTypeIdString = sportsBean.getSportsTypeId();
				// log.info("eventTypeIdString: " + eventTypeIdString);

				Set<String> eventTypeIdSet = new HashSet<String>();
				eventTypeIdSet.add(eventTypeIdString);
				marketFilter.setEventTypeIds(eventTypeIdSet);

				List<SeriesBean> seriesBeanList = seriesRepository
						.findBySportIdAndIsActiveOrderBySportId(eventTypeIdString, "Y");

				for (SeriesBean seriesBean : seriesBeanList) {
					// log.info("[" + transactionId + "] ***********INSIDE SERIES LOOP*********");

					String seriesId = seriesBean.getSeriesId();
					// log.info("[" + transactionId + "] seriesId: " + seriesId + " of Sports Id: "
					// + eventTypeIdString);
					Set<String> seriesIdSet = new HashSet<String>();
					seriesIdSet.add(seriesId);
					marketFilter.setCompetitionIds(seriesIdSet);

					// log.info("[" + transactionId + "] ***********Fecting Matches of " +
					// sportsBean.getSportsName()
					// + " : " + seriesBean.getSeriesName() + " *********");
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

			// log.info("[" + transactionId + "] response: " + matchBeanResponseList);
		} catch (APINGException apiExc) {
			log.info(apiExc.toString());
		}
		// log.info("[" + transactionId + "] matchBeanResponseList:: " +
		// matchBeanResponseList);
		return matchBeanResponseList;
	}
	
	
	
	public List<MatchBean> updateListOfMatches(String userName,String seriesId, String transactionId) {

		MarketFilter marketFilter;
		marketFilter = new MarketFilter();
		final List<MatchBean> matchBeanList = new ArrayList<MatchBean>();
		List<MatchBean> matchBeanResponseList = new ArrayList<MatchBean>();
		try {

				List<SeriesBean> seriesBeanList = seriesRepository.findBySeriesIdAndIsActiveOrderBySportId(seriesId, "Y");

				for (SeriesBean seriesBean : seriesBeanList) {

					String sportsId = seriesBean.getSportId();
					String seriesIds  = seriesBean.getSeriesId();
					Set<String> seriesIdSet = new HashSet<String>();
					seriesIdSet.add(seriesIds);
					marketFilter.setCompetitionIds(seriesIdSet);

					List<EventResult> response = rescriptOperations.listEvents(marketFilter, appKey,
							ssToken);

					response.stream().forEach(res -> {
						MatchBean matchBean = new MatchBean();
						matchBean.setMatchId(res.getEvent().getId());
						matchBean.setMatchName(res.getEvent().getName());
						matchBean.setMatchCountryCode(res.getEvent().getCountryCode());
						matchBean.setMatchTimezone(res.getEvent().getTimezone());
						matchBean.setMatchVenue(res.getEvent().getVenue());
						matchBean.setMatchOpenDate(res.getEvent().getOpenDate());
						matchBean.setMatchMarketCount(res.getMarketCount());
						matchBean.setSportId(sportsId);
						matchBean.setSeriesId(seriesId);
						matchBean.setMatchCreatedBy(userName);
						matchBeanList.add(matchBean);
					});
				}
			matchBeanResponseList = storeListOfMatchDB(matchBeanList, transactionId);

		} catch (APINGException apiExc) {
			log.info(apiExc.toString());
		}
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
				// log.info("[" + transactionId + "] ***********INSIDE Match LOOP*********");

				String matchId = matchBean.getMatchId();
				// log.info("[" + transactionId + "]matchId: " + matchId);
				Set<String> matchIdSet = new HashSet<String>();
				matchIdSet.add(matchId);
				marketFilter.setEventIds(matchIdSet);

				List<MarketFancyResult> response = rescriptOperations.listFancy(marketFilter, applicationKey,
						sessionToken);

				response.stream().forEach(res -> {
					FancyBean fancyBean = new FancyBean();
					FancyIdDto fancyId = new FancyIdDto();
					fancyId.setMarketType(res.getMarketType());
					fancyId.setMatchId(matchId);
					fancyBean.setFancyId(fancyId);
					// fancyBean.getFancyId().setMarketType(res.getMarketType());
					// fancyBean.setMarketType(res.getMarketType());
					fancyBean.setMarketCount(res.getMarketCount());
					// fancyBean.getFancyId().setMatchId(matchId);
					// fancyBean.setMatchId(matchId);
					fancyBean.setFancyCreatedBy(userName);
					fancyBeanList.add(fancyBean);
				});
			}
			// log.info("fancyBeanList:: " + fancyBeanList);

			fancyBeanResponseList = storeListOfFancyDB(fancyBeanList, transactionId);
			// log.info("[" + transactionId + "] response after storeListOfFancyDB: " +
			// fancyBeanResponseList);

		} catch (APINGException apiExc) {
			log.info("[" + transactionId + "] " + apiExc.toString());
		}
		return fancyBeanResponseList;
	}
	
	
	public List<FancyBean> updateListOfOdds(String userName, String matchId, String transactionId) {

		MarketFilter marketFilter;
		marketFilter = new MarketFilter();
		final List<FancyBean> fancyBeanList = new ArrayList<FancyBean>();
		List<FancyBean> fancyBeanResponseList = new ArrayList<FancyBean>();
		try {

			List<MatchBean> matchBeanList = matchRepository.findByMatchIdAndIsActive(matchId,"Y");

			for (MatchBean matchBean : matchBeanList) {

				String matchIds = matchBean.getMatchId();
				Set<String> matchIdSet = new HashSet<String>();
				matchIdSet.add(matchIds);
				marketFilter.setEventIds(matchIdSet);

				List<MarketFancyResult> response = rescriptOperations.listFancy(marketFilter, appKey,
						ssToken);

				response.stream().forEach(res -> {
					FancyBean fancyBean = new FancyBean();
					FancyIdDto fancyId = new FancyIdDto();
					fancyId.setMarketType(res.getMarketType());
					fancyId.setMatchId(matchIds);
					fancyBean.setFancyId(fancyId);
					fancyBean.setMarketCount(res.getMarketCount());
					fancyBean.setFancyCreatedBy(userName);
					fancyBeanList.add(fancyBean);
				});
			}

			fancyBeanResponseList = storeListOfFancyDB(fancyBeanList, transactionId);

		} catch (APINGException apiExc) {
			log.info("[" + transactionId + "] " + apiExc.toString());
		}
		return fancyBeanResponseList;
	}

	@Transactional
	public List<SeriesMatchFancyResponseDto> getMarketCatalogue(String sportsId, String appKey, String ssoid,
			String userName, String transactionId) {
		this.applicationKey = appKey;
		this.sessionToken = ssoid;
		MarketFilter marketFilter;

		List<SeriesMatchFancyResponseDto> seriesMatchFancyResList = new ArrayList<SeriesMatchFancyResponseDto>();
		List<MarketCatalogue> marketCatalogueResult = null;

		final List<MarketCatalogueBean> marketCatalogueList = new ArrayList<MarketCatalogueBean>();
		// List<MarketCatalogueBean> marketCatalogueResList = new
		// ArrayList<MarketCatalogueBean>();
		try {
			String maxResults = "100";
			List<SeriesBean> seriesList= new ArrayList<SeriesBean>();
			if (StringUtils.isEmpty(sportsId)) {
				seriesList = seriesRepository.findByIsActiveOrderBySportId("Y");
			} else {
				seriesList = seriesRepository.findBySportIdAndIsActiveOrderBySportId(sportsId, "Y");
			}

			for (int k = 0; k < seriesList.size(); k++) {
				SeriesMatchFancyResponseDto seriesMatchFancyRes = new SeriesMatchFancyResponseDto();
				List<MatchAndFancyDetailDto> matchAndFancyDetailList = new ArrayList<MatchAndFancyDetailDto>();
				String seriesId = seriesList.get(k).getSeriesId();
				String seriesName = seriesList.get(k).getSeriesName();

				// log.info("seriesId: " + seriesId);
				// log.info("seriesName:: " + seriesName);

				seriesMatchFancyRes.setSeriesId(seriesId);
				seriesMatchFancyRes.setSeriesName(seriesName);

				List<MatchBean> matchList = matchRepository.findBySeriesIdAndIsActive(seriesId, "Y");
				for (int j = 0; j < matchList.size(); j++) {

					String matchIdString = matchList.get(j).getMatchId();
					String matchNameString = matchList.get(j).getMatchName();
					Date matchOpenDate = matchList.get(j).getMatchOpenDate();
					// log.info("Match ID for Fancy Details: " + matchIdString);
					List<FancyBean> marketTypeList = fancyRepository.findByFancyIdMatchIdAndIsActive(matchIdString,
							"Y");

					for (int i = 0; i < marketTypeList.size(); i++) {
						MatchAndFancyDetailDto matchAndFancyDetailDto = new MatchAndFancyDetailDto();
						String matchId = matchIdString;
						String matchName = matchNameString;
						Date matchDate = matchOpenDate;
						String marketType = marketTypeList.get(i).getFancyId().getMarketType();
						int marketCount = marketTypeList.get(i).getMarketCount();

						// log.info("matchId: " + matchId);
						// log.info("matchName:: " + matchName);
						// log.info("matchDate: " + matchDate);
						// log.info("marketType:: " + marketType);

						matchAndFancyDetailDto.setMatchId(matchId);
						matchAndFancyDetailDto.setMatchName(matchName);
						matchAndFancyDetailDto.setMatchDate(matchDate);
						matchAndFancyDetailDto.setMarketType(marketType);
						matchAndFancyDetailDto.setMarketCount(marketCount);

						Set<String> typesCode = new HashSet<String>();
						typesCode.add(marketType);
						Set<String> eventIds = new HashSet<String>();
						eventIds.add(matchId);

						// log.info("[" + transactionId + "] MatchID: " + matchId + " Market Type: " +
						// marketType);

						marketFilter = new MarketFilter();
						marketFilter.setEventIds(eventIds);
						marketFilter.setMarketTypeCodes(typesCode);
						Set<MarketProjection> marketProjection = new HashSet<MarketProjection>();
						// marketProjection.add(MarketProjection.COMPETITION);
						// marketProjection.add(MarketProjection.EVENT);
						// marketProjection.add(MarketProjection.EVENT_TYPE);
						marketProjection.add(MarketProjection.MARKET_START_TIME);
						// marketProjection.add(MarketProjection.MARKET_DESCRIPTION);
						marketProjection.add(MarketProjection.RUNNER_DESCRIPTION);
						// marketProjection.add(MarketProjection.RUNNER_METADATA);

						marketCatalogueResult = rescriptOperations.listMarketCatalogue(marketFilter, marketProjection,
								MarketSort.FIRST_TO_START, maxResults, applicationKey, sessionToken);
						if (marketCatalogueResult.size() == 0) {
							//log.info("INSIDE IF");
							matchAndFancyDetailDto.setMarketCatalogueRes(new ArrayList<MarketCatalogue>());
						} else {
							matchAndFancyDetailDto.setMarketCatalogueRes(marketCatalogueResult);
						}
						matchAndFancyDetailList.add(matchAndFancyDetailDto);
					}

					seriesMatchFancyRes.setMatchAndFancyDetail(matchAndFancyDetailList);
				}
				seriesMatchFancyResList.add(seriesMatchFancyRes);
			}
			//log.info("seriesMatchFancyResList: " + seriesMatchFancyResList);
		} catch (APINGException e) {
			e.printStackTrace();
		}
		return seriesMatchFancyResList;
	}

	@Transactional
	public List<SportsBean> storeListOfEventTypesInDB(List<SportsBean> sportsBeanList, String transactionId) {
		List<SportsBean> responseBeanList = new ArrayList<SportsBean>();
		for (SportsBean sportsBean : sportsBeanList) {
			String sportsId = sportsBean.getSportsTypeId();
			long getRowCount = sportsRepository.countBySportsTypeId(sportsId);
			if (getRowCount == 0) {
				SportsBean responseBean = new SportsBean();
				// log.info("[" + transactionId + "] inside if sportsId: " + sportsId);
				responseBean = sportsRepository.saveAndFlush(sportsBean);
				responseBeanList.add(responseBean);
			} else {
				// log.info("[" + transactionId + "] inside Else sportsId: " + sportsId);
			}
		}
		// log.info("responseBeanList:: " + responseBeanList);
		return responseBeanList;
	}

	@Transactional
	private List<SeriesBean> storeListOfCompDB(List<SeriesBean> seriesBeanList, String transactionId) {
		List<SeriesBean> responseBeanList = new ArrayList<SeriesBean>();
		for (SeriesBean seriesBean : seriesBeanList) {
			// String seriesId = seriesBean.getSeriesId();
			// long getRowCount = seriesRepository.countBySeriesId(seriesId);
			// if (getRowCount == 0) {
			SeriesBean responseBean = new SeriesBean();
			// log.info("[" + transactionId + "] inside if seriesId: " + seriesId);
			responseBean = seriesRepository.saveAndFlush(seriesBean);
			responseBeanList.add(responseBean);
			// } else {
			// log.info("[" + transactionId + "] inside Else seriesId: " + seriesId);
			// }
		}
		// log.info("responseBeanList:: " + responseBeanList);
		return responseBeanList;
	}

	@Transactional
	private List<MatchBean> storeListOfMatchDB(List<MatchBean> matchBeanList, String transactionId) {
		List<MatchBean> responseBeanList = new ArrayList<MatchBean>();
		for (MatchBean matchBean : matchBeanList) {
			// String matchId = matchBean.getMatchId();
			// long getRowCount = matchRepository.countByMatchId(matchId);
			// if (getRowCount == 0) {
			MatchBean responseBean = new MatchBean();
			// log.info("[" + transactionId + "] inside if matchId: " + matchId);
			responseBean = matchRepository.saveAndFlush(matchBean);
			responseBeanList.add(responseBean);
			// } else {
			// log.info("[" + transactionId + "] inside Else matchId: " + matchId);
			// }
		}
		// log.info("responseBeanList:: " + responseBeanList);
		return responseBeanList;
	}

	@Transactional
	private List<FancyBean> storeListOfFancyDB(List<FancyBean> fancyBeanList, String transactionId) {
		// log.info("[" + transactionId
		// + "]##############################Inside
		// storeListOfFancyDB#############################");
		List<FancyBean> responseBeanList = new ArrayList<FancyBean>();
		for (FancyBean fancyBean : fancyBeanList) {
			// log.info("marketType & Match ID " + fancyBean.getMarketType() + " : " +
			// fancyBean.getMatchId());
			// long getRowCount =
			// fancyRepository.countByMarketTypeAndMatchId(fancyBean.getMarketType(),
			// fancyBean.getMatchId());
			// if (getRowCount == 0) {
			// log.info("[" + transactionId + "] inside if marketType & Match ID " +
			// fancyBean.getMarketType() + " : "
			// + fancyBean.getMatchId());
			responseBeanList.add(fancyRepository.save(fancyBean));
			// } else {
			// log.info("[" + transactionId + "] inside Else marketType & Match ID " +
			// fancyBean.getMarketType()
			// + " : " + fancyBean.getMatchId());
			// }
		}
		log.info("responseBeanList:: " + responseBeanList);
		return responseBeanList;
	}

	@Transactional
	private List<MarketCatalogueBean> storeMarketCatalogueDB(List<MarketCatalogueBean> marketCatalogueList,
			String transactionId) {
		log.info("[" + transactionId + "]###############Inside  storeMarketCatalogueDB#########");
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
		appKey=response.getProduct();
		ssToken=response.getToken();
		log.info("applicationKey in Dao:: "+appKey);
		log.info("sessionToken in dao:: "+ssToken);
		return response;
	}

	public List<DashboardMatchListDto> dashboardMatchList(String applicationKey, String sessionToken, String userName,
			String transactionId) {
		log.info("[" + transactionId + "]###############Inside  storeMarketCatalogueDB#########");
		this.applicationKey = applicationKey;
		this.sessionToken = sessionToken;
		List<MarketCatalogue> marketCatalogueResult = new ArrayList<MarketCatalogue>();
		List<DashboardMatchListDto> resBeanList = new ArrayList<DashboardMatchListDto>();
		try {
			String maxResults = "100";
			MarketFilter marketFilter;

			List<FancyBean> fancyList = fancyRepository.findByIsActive("Y");
			Set<String> typesCode = new HashSet<String>();
			Set<String> eventIds = new HashSet<String>();
			for (int i = 0; i < fancyList.size(); i++) {
				typesCode.add(fancyList.get(i).getFancyId().getMarketType());
				eventIds.add(fancyList.get(i).getFancyId().getMatchId());
			}

			marketFilter = new MarketFilter();
			marketFilter.setEventIds(eventIds);
			marketFilter.setMarketTypeCodes(typesCode);
			Set<MarketProjection> marketProjection = new HashSet<MarketProjection>();
			marketProjection.add(MarketProjection.EVENT);
			marketProjection.add(MarketProjection.MARKET_START_TIME);
			marketProjection.add(MarketProjection.RUNNER_DESCRIPTION);

			marketCatalogueResult = rescriptOperations.listMarketCatalogue(marketFilter, marketProjection,
					MarketSort.FIRST_TO_START, maxResults, applicationKey, sessionToken);

		} catch (APINGException e) {
			e.printStackTrace();
		}
		return resBeanList;
	}

	public RunnerPriceAndSize getRunnersPrizeAndSize1(List<DashboardMatchListDto> data, String applicationKey,
			String sessionToken, String userName, String transactionId) throws APINGException {
		return null;
	}

	public Double getRunnersPrizeAndSize(String marketId, Long selectionId, String isBackLay, String transactionId) {
		Double runnerPrize=0.0;
		PriceProjection priceProjection = new PriceProjection();
		Set<PriceData> priceData = new HashSet<PriceData>();
		priceData.add(PriceData.EX_BEST_OFFERS);
		priceData.add(PriceData.EX_ALL_OFFERS);
		priceProjection.setPriceData(priceData);
		OrderProjection orderProjection = null;
		MatchProjection matchProjection = null;
		String currencyCode = null;

		try {
			runnerPrize=rescriptOperations.runnerPrizeAndSize(marketId, selectionId, priceProjection, orderProjection, matchProjection, currencyCode, isBackLay, appKey, ssToken);
		} catch (APINGException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return runnerPrize;
	}

	@Transactional
	public void declareResult(String applicationKey, String sessionToken, String userName, String transactionId) {
		log.info("[" + transactionId + "]##########Inside  declareResult#########");

		final String BET_STATUS = "ACTIVE";
		try {
			PriceProjection priceProjection = new PriceProjection();
			Set<PriceData> priceData = new HashSet<PriceData>();
			priceData.add(PriceData.EX_BEST_OFFERS);
			priceData.add(PriceData.EX_ALL_OFFERS);
			priceProjection.setPriceData(priceData);
			OrderProjection orderProjection = null;
			MatchProjection matchProjection = null;
			String currencyCode = null;

			List<PlaceBetsBean> placeBetsList = new ArrayList<PlaceBetsBean>();

			placeBetsList = placeBetsRepository.findByBetStatusOrderById(BET_STATUS);

			for (int i = 0; i < placeBetsList.size(); i++) {

				String marketIds = placeBetsList.get(i).getMarketId();
				Long selectionId = placeBetsList.get(i).getSelectionId();

				List<MarketBook> runnerBook = rescriptOperations.listRunnersBook1(marketIds, selectionId,
						priceProjection, orderProjection, matchProjection, currencyCode, applicationKey, sessionToken);

				String betResult = runnerBook.get(0).getRunners().get(0).getStatus();

				jdbcTemplate.update(QueryListConstant.UPDATE_BET_STATUS,
						new Object[] { betResult, selectionId.toString(), marketIds });

//				log.info("betResult:: "+betResult);
			}
			int count = jdbcTemplate.update(QueryListConstant.UPDATE_BET_RESULT);

//			log.info(count +" results updated");

		} catch (APINGException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		calculateProfitLoss();
	}

	
	public void calculateProfitLoss() {
		log.info("##########Inside  calculateProfitLoss#########");
		List<PlaceBetsBean> placeBetsList = new ArrayList<PlaceBetsBean>();

		List<String> betResultList = new ArrayList<String>();
		betResultList.add(ResourceConstants.WON);
		betResultList.add(ResourceConstants.LOST);

		String betSettlement = "NOT_INITIATED";

		placeBetsList = placeBetsRepository.findByBetResultInAndBetSettlementOrderByUserId(betResultList, betSettlement);

//		log.info("calculateProfitLoss:: "+placeBetsList);
//		boolean masterInsertFlag=true;
//		boolean smInsertFlag=true;
//		boolean adminInsertFlag=true;
		for (int i = 0; i < placeBetsList.size(); i++) {
			double profit = 0.0;
			double loss = 0.0;
			double netAmount = 0.0;
			double commision = 0.0;
			double adminStakes = 0.0;
			double smStakes = 0.0;
			double masterStakes = 0.0;
			
			// String userId=placeBetsList.get(i).getUserId() ;

			PlaceBetsBean placeBetsBean = new PlaceBetsBean();
			placeBetsBean = placeBetsList.get(i);
			String userId=placeBetsBean.getUserId().toUpperCase();
			UserBean userDetail = userRepository.findByUserId(userId);

			
			
			PartnershipBean psDetails = partnershipRepository.findByUserId(userId);

			int adminPer = psDetails.getAdminStake();
			int smPer = psDetails.getSupermasterStake();
			int masterPer = psDetails.getMastrerStake();

			double oddCommision = userDetail.getOddsCommission();
			double sessionCommision = userDetail.getSessionCommission();
			double liability = placeBetsList.get(i).getLiability();
			double stake = placeBetsList.get(i).getStake();
			double odds = placeBetsList.get(i).getOdds();
			double total=0.0;
			if (placeBetsBean.getBetResult().equalsIgnoreCase(ResourceConstants.WON)) {
				if (placeBetsBean.getMarketName().toUpperCase().contains("ODDS")) {
					total = odds * stake;
					profit = Double.parseDouble(df.format(total - stake));
					commision = calcuateCommision(profit, oddCommision);
					profit =  Double.parseDouble(df.format(profit - commision));
					netAmount = profit;
				} else {
					commision = calcuateCommision(stake, sessionCommision);
					profit = Double.parseDouble(df.format(stake + commision));
					netAmount = profit;
				}
				masterStakes = calcuateCommision(profit, masterPer);
				adminStakes = calcuateCommision(profit, adminPer);
				smStakes = calcuateCommision(profit, smPer);
				
			} else if (placeBetsBean.getBetResult().equalsIgnoreCase(ResourceConstants.LOST)) {
				if (placeBetsBean.getMarketName().toUpperCase().contains("ODDS")) {
					loss=liability;
					netAmount = -loss;
				}else {
					commision = calcuateCommision(liability, sessionCommision);
					loss = Double.parseDouble(df.format(liability-commision));
					netAmount = -loss;
				}
				adminStakes = -calcuateCommision(loss, adminPer);
				smStakes = -calcuateCommision(loss, smPer);
				masterStakes = -calcuateCommision(loss, masterPer);
			}
			List<UserRoleDto> userDetails= new ArrayList<UserRoleDto>();
			Map<String,String> userParentMap=new HashMap<String,String>();
			userDetails = jdbcTemplate.query(QueryListConstant.GET_PARENT_LIST,
					new Object[] { userId },
					(rs, rowNum) -> new UserRoleDto(
							rs.getString("USER_ID"), rs.getString("USER_ROLE")
							));
			for (int j = 0; j < userDetails.size(); j++) {
				userParentMap.put(userDetails.get(j).getUserRole().toUpperCase(),userDetails.get(j).getUserId().toUpperCase());
			}
			
			placeBetsBean.setCommision(commision);
			placeBetsBean.setProfit(profit);
			placeBetsBean.setLoss(loss);
			placeBetsBean.setNetAmount(netAmount);
			placeBetsBean.setAdminStakes(adminStakes);
			placeBetsBean.setSmStakes(smStakes);
			placeBetsBean.setMasterStakes(masterStakes);
			placeBetsBean.setBetSettlement("PENDING");

			placeBetsRepository.saveAndFlush(placeBetsBean);
			
//			final String NA="NA";
//			final String N="N";
//			final double ZERO_DOUBLE=0;
//			final long ZERO_LONG=0;
//			final int ZERO_INT=0;
//			
//			PlaceBetsBean betBeanForMaster = new PlaceBetsBean();
//			
//			betBeanForMaster.setSportsId(NA);
//			betBeanForMaster.setSportsName(NA);
//			betBeanForMaster.setSeriesId(NA);
//			betBeanForMaster.setSeriesName(NA);
//			betBeanForMaster.setMatchId(NA);
//			betBeanForMaster.setMatchName(NA);
//			betBeanForMaster.setMarketId(NA);
//			betBeanForMaster.setMarketName(NA);
//			betBeanForMaster.setSelectionId(ZERO_LONG);
//			betBeanForMaster.setRunnerName(NA);
//			betBeanForMaster.setOdds(ZERO_DOUBLE);
//			betBeanForMaster.setStake(ZERO_DOUBLE);
//			betBeanForMaster.setLiability(ZERO_DOUBLE);
//			betBeanForMaster.setIsback(N);
//			betBeanForMaster.setIsLay(N);
//			betBeanForMaster.setPsId(ZERO_INT);
//			betBeanForMaster.setCreatedBy(ResourceConstants.USER_NAME);
//			betBeanForMaster.setBetStatus(NA);
//			betBeanForMaster.setBetResult(NA);
//			
//			PlaceBetsBean betBeanForSM = new PlaceBetsBean();
//			
//			betBeanForSM.setSportsId(NA);
//			betBeanForSM.setSportsName(NA);
//			betBeanForSM.setSeriesId(NA);
//			betBeanForSM.setSeriesName(NA);
//			betBeanForSM.setMatchId(NA);
//			betBeanForSM.setMatchName(NA);
//			betBeanForSM.setMarketId(NA);
//			betBeanForSM.setMarketName(NA);
//			betBeanForSM.setSelectionId(ZERO_LONG);
//			betBeanForSM.setRunnerName(NA);
//			betBeanForSM.setOdds(ZERO_DOUBLE);
//			betBeanForSM.setStake(ZERO_DOUBLE);
//			betBeanForSM.setLiability(ZERO_DOUBLE);
//			betBeanForSM.setIsback(N);
//			betBeanForSM.setIsLay(N);
//			betBeanForSM.setPsId(ZERO_INT);
//			betBeanForSM.setCreatedBy(ResourceConstants.USER_NAME);
//			betBeanForSM.setBetStatus(NA);
//			betBeanForSM.setBetResult(NA);
//			
//			PlaceBetsBean betBeanForAdmin = new PlaceBetsBean();
//			
//			betBeanForAdmin.setSportsId(NA);
//			betBeanForAdmin.setSportsName(NA);
//			betBeanForAdmin.setSeriesId(NA);
//			betBeanForAdmin.setSeriesName(NA);
//			betBeanForAdmin.setMatchId(NA);
//			betBeanForAdmin.setMatchName(NA);
//			betBeanForAdmin.setMarketId(NA);
//			betBeanForAdmin.setMarketName(NA);
//			betBeanForAdmin.setSelectionId(ZERO_LONG);
//			betBeanForAdmin.setRunnerName(NA);
//			betBeanForAdmin.setOdds(ZERO_DOUBLE);
//			betBeanForAdmin.setStake(ZERO_DOUBLE);
//			betBeanForAdmin.setLiability(ZERO_DOUBLE);
//			betBeanForAdmin.setIsback(N);
//			betBeanForAdmin.setIsLay(N);
//			betBeanForAdmin.setPsId(ZERO_INT);
//			betBeanForAdmin.setCreatedBy(ResourceConstants.USER_NAME);
//			betBeanForAdmin.setBetStatus(NA);
//			betBeanForAdmin.setBetResult(NA);
//			
//			String admin=userParentMap.get("ADMIN");
//			String sm=userParentMap.get("SUPERMASTER");
//			String master=userParentMap.get("MASTER");
//			placeBetsBean.setBetSettlement("PENDING");
			
//			String prevUser;
//			String currUser = placeBetsList.get(i).getUserId();
//			if(i==0) {
//				prevUser=currUser;
//			}else {
//				prevUser=placeBetsList.get(i-1).getUserId();
//			}
//			
			
//			if(!prevUser.equalsIgnoreCase(currUser)){
//				masterInsertFlag=true;
//				smInsertFlag=true;
//				adminInsertFlag=true;
//			}
//			log.info("index:: "+i);
//			log.info("prevUser:: "+prevUser);
//			log.info("currUser:: "+currUser);
//			
//			Long masterPLExistCount = placeBetsRepository.countByUserIdAndBetSettlementOrderById(master, betSettlement);
//			Long smPLExistCount = placeBetsRepository.countByUserIdAndBetSettlementOrderById(sm, betSettlement);
//			Long adminPLExistCount = placeBetsRepository.countByUserIdAndBetSettlementOrderById(admin, betSettlement);
//			
//			log.info("masterPLExistCount:: "+masterPLExistCount);
//			log.info("smPLExistCount:: "+smPLExistCount);
//			log.info("adminPLExistCount:: "+adminPLExistCount);
//			
//			if(masterPLExistCount>0 ) {
//				log.info("inside master update:: ");
//				//update
//				jdbcTemplate.update(QueryListConstant.UPDATE_PARENT_PROFIT_LOSS,
//						new Object[] { masterStakes, 0,smStakes,adminStakes, userId });
//			}else if(masterPLExistCount<=0 ){
//				log.info("inside master insert:: ");
//				//insert
////				masterInsertFlag=false;
//				betBeanForMaster.setLoginId(master);
//				betBeanForMaster.setUserId(master);
//				betBeanForMaster.setParent(sm);
//				betBeanForMaster.setNetAmount(masterStakes);
//				betBeanForMaster.setAdminStakes(adminStakes);
//				betBeanForMaster.setSmStakes(smStakes);
//				placeBetsRepository.saveAndFlush(betBeanForMaster);
//			}
//			if(smPLExistCount>0 ) {
//				log.info("inside sm update:: ");
//				//update
//				jdbcTemplate.update(QueryListConstant.UPDATE_PARENT_PROFIT_LOSS,
//						new Object[] { smStakes, 0,0,adminStakes, master });
//			}else if(smPLExistCount<=0 ) {
//				log.info("inside sm insert:: ");
//				//insert
////				smInsertFlag=false;
//				betBeanForSM.setLoginId(sm);
//				betBeanForSM.setUserId(sm);
//				betBeanForSM.setParent(admin);
//				betBeanForSM.setNetAmount(smStakes);
//				betBeanForSM.setAdminStakes(adminStakes);
//				placeBetsRepository.saveAndFlush(betBeanForSM);
//			}
//			if(adminPLExistCount>0) {
//				log.info("inside admin update:: ");
//				//update
//				jdbcTemplate.update(QueryListConstant.UPDATE_PARENT_PROFIT_LOSS,
//						new Object[] { adminStakes, 0,0,0, sm });
//			}else if(adminPLExistCount<=0 ) {
//				log.info("inside admin insert:: ");
////				//insert
////				adminInsertFlag=false;
//				betBeanForAdmin.setLoginId(admin);
//				betBeanForAdmin.setUserId(admin);
//				betBeanForAdmin.setParent("1");
//				betBeanForAdmin.setNetAmount(adminStakes);
//				placeBetsRepository.saveAndFlush(betBeanForAdmin);
//			}
//			
//			
//			
//			jdbcTemplate.update(QueryListConstant.UPDATE_AVAIL_BAL_AND_PROFIT_LOSS,
//					new Object[] { netAmount,netAmount, netAmount, userId });
//			
//			jdbcTemplate.update(QueryListConstant.UPDATE_AVAIL_BAL_AND_PROFIT_LOSS,
//					new Object[] { masterStakes,masterStakes, masterStakes, master });
//			
//			jdbcTemplate.update(QueryListConstant.UPDATE_AVAIL_BAL_AND_PROFIT_LOSS,
//					new Object[] { smStakes,smStakes, smStakes, sm });
//			
//			jdbcTemplate.update(QueryListConstant.UPDATE_AVAIL_BAL_AND_PROFIT_LOSS,
//					new Object[] { adminStakes,adminStakes, adminStakes, admin });
//			
			
//			double availBalUser = 0.0;
//			double availBalMaster = 0.0;
//			double availBalSM = 0.0;
//			double availBalAdmin = 0.0;
			// User
			/*	profitLossUser=profitLossUser+netAmount;
			if(availBalUser+netAmount<0) {
				availBalUser=0.0;
			}else {
				availBalUser=availBalUser+netAmount;
			}
			
			
			// Master
			profitLossMaster=profitLossAdmin+masterStakes;
			if(availBalMaster+masterStakes<0) {
				availBalMaster=0.0;
			}else {
				availBalMaster=availBalMaster+masterStakes;
			}
			
			
			// Super Master
			profitLossSM=smStakes;
			if(availBalSM+smStakes<0) {
				availBalSM=0.0;
			}else {
				availBalSM=availBalSM+smStakes;
			}
			
			
			// Admin
			profitLossAdmin=adminStakes;
			if(availBalAdmin+adminStakes<0) {
				availBalAdmin=0.0;
			}else {
				availBalAdmin=availBalAdmin+adminStakes;
			}
			*/
			/*double availBalUser=userDetail.getAvailBalance();
			
			UserBean masterDetail = userRepository.findByUserId(master);
			double availBalMaster=masterDetail.getAvailBalance();
			
			UserBean smDetail = userRepository.findByUserId(sm);
			double availBalSM=smDetail.getAvailBalance();
			
			UserBean adminDetail = userRepository.findByUserId(admin);
			double availBalAdmin=adminDetail.getAvailBalance();*/
			
			

		}

	}

	public double calcuateCommision(double price, double percentage) {
//		log.info("price:: "+price);
//		log.info("percentage:: "+percentage);
		double amount = Double.parseDouble(df.format(Double.parseDouble(df.format(percentage * price)) / 100));
//		log.info("amount:: "+amount);
		return amount;
	}

	public List<MarketCatalogue> dashboardDetails(String appKey, String ssoid, String matchId, String marketType,
			String transactionId) {
		this.applicationKey = appKey;
		this.sessionToken = ssoid;
		MarketFilter marketFilter;
		String maxResults = "100";

		List<MarketCatalogue> marketCatalogueResult = new ArrayList<MarketCatalogue>();
		try {

			Set<String> typesCode = new HashSet<String>();
			typesCode.add(marketType);
			Set<String> eventIds = new HashSet<String>();
			eventIds.add(matchId);

			marketFilter = new MarketFilter();
			marketFilter.setEventIds(eventIds);
			marketFilter.setMarketTypeCodes(typesCode);
			Set<MarketProjection> marketProjection = new HashSet<MarketProjection>();
			marketProjection.add(MarketProjection.MARKET_START_TIME);
			marketProjection.add(MarketProjection.RUNNER_DESCRIPTION);

			marketCatalogueResult = rescriptOperations.listMarketCatalogue(marketFilter, marketProjection,
					MarketSort.FIRST_TO_START, maxResults, applicationKey, sessionToken);
		} catch (APINGException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return marketCatalogueResult;
	}

}
