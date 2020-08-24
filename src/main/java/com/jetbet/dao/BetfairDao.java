package com.jetbet.dao;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
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
	
	@Transactional
	public List<SeriesBean> updateListOfSeries(String userName, String transactionId) {
		
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
	
	
	@Transactional
	public List<MatchBean> updateListOfMatches(String userName, String transactionId) {

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
	
	@Transactional
	public List<FancyBean> updateListOfOdds(String userName, String transactionId) {

		MarketFilter marketFilter;
		marketFilter = new MarketFilter();
		final List<FancyBean> fancyBeanList = new ArrayList<FancyBean>();
		List<FancyBean> fancyBeanResponseList = new ArrayList<FancyBean>();
		try {

			List<MatchBean> matchBeanList = matchRepository.findByIsActive("Y");

			for (MatchBean matchBean : matchBeanList) {

				String matchId = matchBean.getMatchId();
				Set<String> matchIdSet = new HashSet<String>();
				matchIdSet.add(matchId);
				marketFilter.setEventIds(matchIdSet);

				List<MarketFancyResult> response = rescriptOperations.listFancy(marketFilter, appKey,
						ssToken);

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
							log.info("INSIDE IF");
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
			log.info("seriesMatchFancyResList: " + seriesMatchFancyResList);
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

			// log.info("[" + transactionId + "] MatchID: " + matchId + " Market Type: " +
			// marketType);

			marketFilter = new MarketFilter();
			marketFilter.setEventIds(eventIds);
			marketFilter.setMarketTypeCodes(typesCode);
			Set<MarketProjection> marketProjection = new HashSet<MarketProjection>();
			marketProjection.add(MarketProjection.EVENT);
			marketProjection.add(MarketProjection.MARKET_START_TIME);
			marketProjection.add(MarketProjection.RUNNER_DESCRIPTION);

			marketCatalogueResult = rescriptOperations.listMarketCatalogue(marketFilter, marketProjection,
					MarketSort.FIRST_TO_START, maxResults, applicationKey, sessionToken);

			log.info("marketCatalogueResult:: " + marketCatalogueResult);
			for (int j = 0; j < marketCatalogueResult.size(); j++) {
				DashboardMatchListDto resBean = new DashboardMatchListDto();

				resBean.setMatchId(marketCatalogueResult.get(j).getEvent().getId());
				resBean.setMatchName(marketCatalogueResult.get(j).getEvent().getName());
				resBean.setMatchOpenDate(marketCatalogueResult.get(j).getEvent().getOpenDate());
				resBean.setMarketId(marketCatalogueResult.get(j).getMarketId());
				resBean.setMarketType(marketCatalogueResult.get(j).getMarketName());
				System.out.println("INSIDE IF marketCatalogueResult.get(j).getRunners().size(): "
						+ marketCatalogueResult.get(j).getRunners().size());
				if (marketCatalogueResult.get(j).getRunners().size() == 3) {
					System.out.println("INSIDE IF marketCatalogueResult.get(j).getRunners().size(): "
							+ marketCatalogueResult.get(j).getRunners().size());
					resBean.setTeamABackPrize(marketCatalogueResult.get(j).getRunners().get(0).getRunner().getEx()
							.getAvailableToBack().get(0).getPrice());
					resBean.setTeamBBackPrize(marketCatalogueResult.get(j).getRunners().get(1).getRunner().getEx()
							.getAvailableToBack().get(0).getPrice());
					resBean.setDrawBackPrize(marketCatalogueResult.get(j).getRunners().get(2).getRunner().getEx()
							.getAvailableToBack().get(0).getPrice());

					resBean.setTeamABackSize(marketCatalogueResult.get(j).getRunners().get(0).getRunner().getEx()
							.getAvailableToLay().get(0).getSize());
					resBean.setTeamBBackSize(marketCatalogueResult.get(j).getRunners().get(1).getRunner().getEx()
							.getAvailableToLay().get(0).getSize());
					resBean.setDrawBackSize(marketCatalogueResult.get(j).getRunners().get(2).getRunner().getEx()
							.getAvailableToLay().get(0).getSize());

					resBean.setTeamALayPrize(marketCatalogueResult.get(j).getRunners().get(0).getRunner().getEx()
							.getAvailableToBack().get(0).getPrice());
					resBean.setTeamBLayPrize(marketCatalogueResult.get(j).getRunners().get(1).getRunner().getEx()
							.getAvailableToBack().get(0).getPrice());
					resBean.setDrawLayPrize(marketCatalogueResult.get(j).getRunners().get(2).getRunner().getEx()
							.getAvailableToBack().get(0).getPrice());

					resBean.setTeamALaySize(marketCatalogueResult.get(j).getRunners().get(0).getRunner().getEx()
							.getAvailableToLay().get(0).getSize());
					resBean.setTeamBLaySize(marketCatalogueResult.get(j).getRunners().get(1).getRunner().getEx()
							.getAvailableToLay().get(0).getSize());
					resBean.setDrawLaySize(marketCatalogueResult.get(j).getRunners().get(2).getRunner().getEx()
							.getAvailableToLay().get(0).getSize());

					resBean.setTeamAId(marketCatalogueResult.get(j).getRunners().get(0).getSelectionId());
					resBean.setTeamBId(marketCatalogueResult.get(j).getRunners().get(1).getSelectionId());
					resBean.setDrawId(marketCatalogueResult.get(j).getRunners().get(2).getSelectionId());

					resBean.setTeamA(marketCatalogueResult.get(j).getRunners().get(0).getRunnerName());
					resBean.setTeamB(marketCatalogueResult.get(j).getRunners().get(1).getRunnerName());
					resBean.setDraw(marketCatalogueResult.get(j).getRunners().get(2).getRunnerName());

				} else if (marketCatalogueResult.get(j).getRunners().size() == 2) {
					System.out.println("INSIDE ELSE marketCatalogueResult.get(j).getRunners().size(): "
							+ marketCatalogueResult.get(j).getRunners().size());
					resBean.setTeamABackPrize(marketCatalogueResult.get(j).getRunners().get(0).getRunner().getEx()
							.getAvailableToBack().get(0).getPrice());
					resBean.setTeamBBackPrize(marketCatalogueResult.get(j).getRunners().get(1).getRunner().getEx()
							.getAvailableToBack().get(0).getPrice());

					resBean.setTeamABackSize(marketCatalogueResult.get(j).getRunners().get(0).getRunner().getEx()
							.getAvailableToLay().get(0).getSize());
					resBean.setTeamBBackSize(marketCatalogueResult.get(j).getRunners().get(1).getRunner().getEx()
							.getAvailableToLay().get(0).getSize());

					resBean.setTeamALayPrize(marketCatalogueResult.get(j).getRunners().get(0).getRunner().getEx()
							.getAvailableToBack().get(0).getPrice());
					resBean.setTeamBLayPrize(marketCatalogueResult.get(j).getRunners().get(1).getRunner().getEx()
							.getAvailableToBack().get(0).getPrice());

					resBean.setTeamALaySize(marketCatalogueResult.get(j).getRunners().get(0).getRunner().getEx()
							.getAvailableToLay().get(0).getSize());
					resBean.setTeamBLaySize(marketCatalogueResult.get(j).getRunners().get(1).getRunner().getEx()
							.getAvailableToLay().get(0).getSize());

					resBean.setTeamAId(marketCatalogueResult.get(j).getRunners().get(0).getSelectionId());
					resBean.setTeamBId(marketCatalogueResult.get(j).getRunners().get(1).getSelectionId());

					resBean.setTeamA(marketCatalogueResult.get(j).getRunners().get(0).getRunnerName());
					resBean.setTeamB(marketCatalogueResult.get(j).getRunners().get(1).getRunnerName());
				}
				resBeanList.add(resBean);
			}

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

	}

	@Transactional
	public void calculateProfitLoss() {
		log.info("##########Inside  calculateProfitLoss#########");
		List<PlaceBetsBean> placeBetsList = new ArrayList<PlaceBetsBean>();

		List<String> betResultList = new ArrayList<String>();
		betResultList.add(ResourceConstants.WON);
		betResultList.add(ResourceConstants.LOST);

		String betSettlement = "NOT_INITIATED";

		placeBetsList = placeBetsRepository.findByBetResultInAndBetSettlementOrderById(betResultList, betSettlement);

//		log.info("calculateProfitLoss:: "+placeBetsList);

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
			UserBean userDetail = userRepository.findByUserId(placeBetsBean.getUserId());

			PartnershipBean psDetails = partnershipRepository.findByUserId(placeBetsBean.getUserId());

			int adminPer = psDetails.getAdminStake();
			int smPer = psDetails.getSupermasterStake();
			int masterPer = psDetails.getMastrerStake();

			double oddCommision = userDetail.getOddsCommission();
			double sessionCommision = userDetail.getSessionCommission();
			double liability = placeBetsList.get(i).getLiability();
			double stake = placeBetsList.get(i).getStake();

//			log.info("oddCommision:: "+oddCommision);
//			log.info("sessionCommision:: "+sessionCommision);
//			log.info("liability:: "+liability);
//			log.info("stake:: "+stake);

//			log.info("Market Name:: "+placeBetsBean.getMarketName().toUpperCase());
//			log.info("Bet Result:: "+placeBetsBean.getBetResult());

			if (placeBetsBean.getBetResult().equalsIgnoreCase(ResourceConstants.WON)) {
//				log.info("INSIDE WON IF");
				if (placeBetsBean.getMarketName().toUpperCase().contains("ODDS")) {
//					log.info("INSIDE ODDs IF");
					commision = calcuateCommision(liability, oddCommision);
					profit = calcuateCommision(liability, Double.parseDouble(df.format(100.0 - oddCommision)));
					netAmount = profit;
					masterStakes = calcuateCommision(profit, masterPer);
//					log.info("commision:: "+commision);
//					log.info("profit:: "+profit);
				} else {
//					log.info("INSIDE WON IF");
					commision = calcuateCommision(liability, sessionCommision);
					profit = calcuateCommision(liability, Double.parseDouble(df.format(100.0 - oddCommision)));
					netAmount = profit;
					masterStakes = calcuateCommision(profit, masterPer);
					masterStakes=masterStakes+commision;
//					log.info("commision:: "+commision);
//					log.info("profit:: "+profit);
				}

				adminStakes = calcuateCommision(profit, adminPer);
				smStakes = calcuateCommision(profit, smPer);
				

			} else if (placeBetsBean.getBetResult().equalsIgnoreCase(ResourceConstants.LOST)) {
//				log.info("INSIDE LOST IF");
				loss = stake;
				netAmount = -loss;
//				log.info("loss:: "+loss);
				adminStakes = -calcuateCommision(loss, adminPer);
				smStakes = -calcuateCommision(loss, smPer);
				masterStakes = -calcuateCommision(loss, masterPer);
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
