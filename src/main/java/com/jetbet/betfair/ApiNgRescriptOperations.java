package com.jetbet.betfair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import com.google.gson.reflect.TypeToken;
import com.jetbet.bean.PlaceBetsBean;
import com.jetbet.betfair.entities.CompetitionResult;
import com.jetbet.betfair.entities.EventResult;
import com.jetbet.betfair.entities.EventTypeResult;
import com.jetbet.betfair.entities.ExchangePrices;
import com.jetbet.betfair.entities.MarketBook;
import com.jetbet.betfair.entities.MarketCatalogue;
import com.jetbet.betfair.entities.MarketFancyResult;
import com.jetbet.betfair.entities.MarketFilter;
import com.jetbet.betfair.entities.PriceProjection;
import com.jetbet.betfair.entities.PriceSize;
import com.jetbet.betfair.entities.Runner;
import com.jetbet.betfair.entities.RunnerCatalog;
import com.jetbet.betfair.enums.ApiNgOperation;
import com.jetbet.betfair.enums.MarketProjection;
import com.jetbet.betfair.enums.MarketSort;
import com.jetbet.betfair.enums.MatchProjection;
import com.jetbet.betfair.enums.OrderProjection;
import com.jetbet.betfair.enums.PriceData;
import com.jetbet.betfair.exceptions.APINGException;
import com.jetbet.betfair.util.JsonConverter;
import com.jetbet.dto.BetfailLoginRequestDto;
import com.jetbet.dto.SessionDetails;
import com.jetbet.util.QueryListConstant;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ApiNgRescriptOperations extends ApiNgOperations {
	
	@Autowired
	JdbcTemplate jdbcTemplate;

	private static ApiNgRescriptOperations instance = null;

	private ApiNgRescriptOperations() {
	}

	public static ApiNgRescriptOperations getInstance() {
		if (instance == null) {
			instance = new ApiNgRescriptOperations();
		}
		return instance;
	}

	public List<EventTypeResult> listEventTypes(MarketFilter filter, String appKey, String ssoId)
			throws APINGException {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put(FILTER, filter);
		params.put(LOCALE, locale);
		String result = getInstance().makeRequest(ApiNgOperation.LISTEVENTTYPES.getOperationName(), params, appKey,
				ssoId);
		if (ApiNGDemo.isDebug())
			System.out.println("\nResponse: " + result);
		List<EventTypeResult> container = JsonConverter.convertFromJson(result, new TypeToken<List<EventTypeResult>>() {
		}.getType());
		return container;

	}

	public List<CompetitionResult> listComp(MarketFilter filter, String appKey, String ssoId) throws APINGException {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put(FILTER, filter);
		params.put(LOCALE, locale);
		String result = getInstance().makeRequest(ApiNgOperation.LISTCOMPETITIONS.getOperationName(), params, appKey,
				ssoId);
		if (ApiNGDemo.isDebug())
			System.out.println("\nResponse: " + result);
		List<CompetitionResult> container = JsonConverter.convertFromJson(result,
				new TypeToken<List<CompetitionResult>>() {
				}.getType());
		return container;

	}

	public List<EventResult> listEvents(MarketFilter filter, String appKey, String ssoId) throws APINGException {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put(FILTER, filter);
		params.put(LOCALE, locale);
		String result = getInstance().makeRequest(ApiNgOperation.LISTEVENTS.getOperationName(), params, appKey, ssoId);
		if (ApiNGDemo.isDebug())
			System.out.println("\nResponse: " + result);
		List<EventResult> container = JsonConverter.convertFromJson(result, new TypeToken<List<EventResult>>() {
		}.getType());
		return container;

	}

	public List<MarketFancyResult> listFancy(MarketFilter filter, String appKey, String ssoId) throws APINGException {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put(FILTER, filter);
		params.put(LOCALE, locale);
		String result = getInstance().makeRequest(ApiNgOperation.LISTMARKETTYPES.getOperationName(), params, appKey,
				ssoId);
		if (ApiNGDemo.isDebug())
			System.out.println("\nResponse: " + result);
		List<MarketFancyResult> container = JsonConverter.convertFromJson(result,
				new TypeToken<List<MarketFancyResult>>() {
				}.getType());
		return container;

	}

	/*public List<MarketCatalogue> listMarketCatalogue(MarketFilter filter, Set<MarketProjection> marketProjection,
			MarketSort sort, String maxResult, String appKey, String ssoId) throws APINGException {
		Map<String, Object> params = new HashMap<String, Object>();
		List<MarketBook> marketBookList= new ArrayList<MarketBook>();
		params.put(LOCALE, locale);
		params.put(FILTER, filter);
		params.put(SORT, sort);
		params.put(MAX_RESULT, maxResult);
		params.put(MARKET_PROJECTION, marketProjection);
		String result = getInstance().makeRequest(ApiNgOperation.LISTMARKETCATALOGUE.getOperationName(), params, appKey,
				ssoId);
		log.info("MarketCatalogue: " + result);
		if (ApiNGDemo.isDebug())
			System.out.println("\nResponse: " + result);

		List<MarketCatalogue> container = JsonConverter.convertFromJson(result, new TypeToken<List<MarketCatalogue>>() {
		}.getType());

		for (int i = 0; i < container.size(); i++) {

			PriceProjection priceProjection = new PriceProjection();
			Set<PriceData> priceData = new HashSet<PriceData>();
			priceData.add(PriceData.EX_BEST_OFFERS);
			priceData.add(PriceData.EX_ALL_OFFERS);
			priceProjection.setPriceData(priceData);
			OrderProjection orderProjection = null;
			MatchProjection matchProjection = null;
			String currencyCode = null;

			String marketId = container.get(i).getMarketId();
			int runnersSize = container.get(i).getRunners().size();
			for (int j = 0; j < runnersSize; j++) {
				String selectionId = container.get(i).getRunners().get(j).getSelectionId().toString();
				MarketBook marketBook = listRunnersBook(marketId, selectionId, priceProjection, orderProjection,
						matchProjection, currencyCode, appKey, ssoId);
				container.get(i).getRunners().get(j).setMarketBook(marketBook);
			}
		}

		return container;

	}*/
	
	public List<MarketCatalogue> listMarketCatalogue(MarketFilter filter, Set<MarketProjection> marketProjection,
			MarketSort sort, String maxResult, String appKey, String ssoId) throws APINGException {
		Map<String, Object> params = new HashMap<String, Object>();
		
		params.put(LOCALE, locale);
		params.put(FILTER, filter);
		params.put(SORT, sort);
		params.put(MAX_RESULT, maxResult);
		params.put(MARKET_PROJECTION, marketProjection);
		String result = getInstance().makeRequest(ApiNgOperation.LISTMARKETCATALOGUE.getOperationName(), params, appKey,
				ssoId);
		//log.info("MarketCatalogue: " + result);
		if (ApiNGDemo.isDebug())
			System.out.println("\nResponse: " + result);

		List<MarketCatalogue> container = JsonConverter.convertFromJson(result, new TypeToken<List<MarketCatalogue>>() {
		}.getType());
		
		Map<Long,String> runnerNameMap= new HashMap<Long,String>();
		
		List<MarketBook> marketBook = new ArrayList<MarketBook>();
		for (int i = 0; i < container.size(); i++) {
			List<MarketBook> marketBookList= new ArrayList<MarketBook>();
			List<String> marketIds =new ArrayList<String>();
			PriceProjection priceProjection = new PriceProjection();
			Set<PriceData> priceData = new HashSet<PriceData>();
			priceData.add(PriceData.EX_BEST_OFFERS);
			priceData.add(PriceData.EX_ALL_OFFERS);
			priceProjection.setPriceData(priceData);
			OrderProjection orderProjection = null;
			MatchProjection matchProjection = null;
			String currencyCode = null;
			int runnersSize = container.get(i).getRunners().size();
			
			
			
			for (int j = 0; j < runnersSize; j++) {
				
				//String selectionId = container.get(i).getRunners().get(j).getSelectionId().toString();
				runnerNameMap.put(container.get(i).getRunners().get(j).getSelectionId(), container.get(i).getRunners().get(j).getRunnerName());
				
			}
			//log.info("runnerNameMap:: "+runnerNameMap);
			String marketId = container.get(i).getMarketId();
			//log.info("marketId:: "+marketId);
			marketIds.add(marketId);
			//log.info("marketIds:: "+marketIds);
			marketBook =listMarketBook(marketIds, priceProjection, orderProjection, matchProjection, currencyCode, appKey, ssoId);
		//	log.info("Iteration i: "+i+ "  "+container.get(i).getMarketName());
			//log.info("marketBook:: "+marketBook);
			for (int k = 0; k < marketBook.size(); k++) {
				List<Runner> runnerList= new ArrayList<Runner>();
				//log.info("Iteration k: "+k+ "  "+marketBook.get(k).getMarketId());
				//log.info("getRunners Size:: "+marketBook.get(k).getRunners().size());
				//log.info(" BEFORE FOR runnerList:: "+runnerList);
				for (int j = 0; j < marketBook.get(k).getRunners().size(); j++) {
					//marketBook.get(0).getRunners().get(j).getSelectionId();
					//log.info("Iteration j: "+j+ "  "+marketBook.get(k).getRunners().get(j).getSelectionId());
					if(marketBook.get(k).getRunners().get(j).getEx().getAvailableToBack().size()>0 || marketBook.get(k).getRunners().get(j).getEx().getAvailableToLay().size()>0) {
					//	log.info("Runner NAme:: "+runnerNameMap.get(marketBook.get(k).getRunners().get(j).getSelectionId()));
						marketBook.get(k).getRunners().get(j).setRunnerName(runnerNameMap.get(marketBook.get(k).getRunners().get(j).getSelectionId()));
						runnerList.add(marketBook.get(k).getRunners().get(j));
					//	log.info(" inside IF runnerList:: "+runnerList);
					}
					
					//log.info(" OUTSIDE IF runnerList:: "+runnerList);
				}
				//log.info(" AFTER FOR runnerList:: "+runnerList);
				marketBook.get(k).setRunners(runnerList);
				//log.info("marketBook.get(k).getRunners():: "+marketBook.get(k).getRunners());
				
				//log.info("marketBook.get(k):: "+marketBook.get(k));
				 if(marketBook.get(k).getRunners().size()>0) {
				//	log.info("marketBook.get(k):: "+marketBook.get(k));
				 marketBookList.add(marketBook.get(k));
				// log.info("marketBook.get(k):: "+marketBookList); 
				 }
				
			}
			
				
			//int runnersSize = container.get(i).getRunners().size();
			//for (int j = 0; j < runnersSize; j++) {
			//	String selectionId = container.get(i).getRunners().get(j).getSelectionId().toString();
			//	MarketBook marketBook = listRunnersBook(marketId, selectionId, priceProjection, orderProjection,
			//			matchProjection, currencyCode, appKey, ssoId);
			//	container.get(i).getRunners().get(j).setMarketBook(marketBook);
			//}
			container.get(i).setMarketBook(marketBookList);
			container.get(i).setRunners(null);
		}

		return container;

	}
	
	public List<RunnerCatalog> updateRunnerData(MarketFilter filter, Set<MarketProjection> marketProjection,
			MarketSort sort, String maxResult, String appKey, String ssoId) throws APINGException {
		Map<String, Object> params = new HashMap<String, Object>();
		List<RunnerCatalog> runnerList = new ArrayList<RunnerCatalog>();
		params.put(LOCALE, locale);
		params.put(FILTER, filter);
		params.put(SORT, sort);
		params.put(MAX_RESULT, maxResult);
		params.put(MARKET_PROJECTION, marketProjection);
		String result = getInstance().makeRequest(ApiNgOperation.LISTMARKETCATALOGUE.getOperationName(), params, appKey,
				ssoId);
		// log.info("MarketCatalogue: " + result);
		if (ApiNGDemo.isDebug())
			System.out.println("\nResponse: " + result);

		List<MarketCatalogue> container = JsonConverter.convertFromJson(result, new TypeToken<List<MarketCatalogue>>() {
		}.getType());

		if (container.size()>0 && container.get(0).getRunners().size() > 0) {
			runnerList = container.get(0).getRunners();
		}

		return runnerList;
	}
	
	/*
	 public MarketCatalogue updateRunnerData(MarketFilter filter, Set<MarketProjection> marketProjection,
			MarketSort sort, String maxResult, String appKey, String ssoId) throws APINGException {
		Map<String, Object> params = new HashMap<String, Object>();
		List<RunnerCatalog> runnerList = new ArrayList<RunnerCatalog>();
		MarketCatalogue response=new MarketCatalogue();
		params.put(LOCALE, locale);
		params.put(FILTER, filter);
		params.put(SORT, sort);
		params.put(MAX_RESULT, maxResult);
		params.put(MARKET_PROJECTION, marketProjection);
		String result = getInstance().makeRequest(ApiNgOperation.LISTMARKETCATALOGUE.getOperationName(), params, appKey,
				ssoId);
		// log.info("MarketCatalogue: " + result);
		if (ApiNGDemo.isDebug())
			System.out.println("\nResponse: " + result);

		List<MarketCatalogue> container = JsonConverter.convertFromJson(result, new TypeToken<List<MarketCatalogue>>() {
		}.getType());

		if (container.size()>0 && container.get(0).getRunners().size() > 0) {
			//container.get(0).setMarketId(marketId);
			response = container.get(0);
		}

		return response;
	}
	 * */
	
	@Override
	public List<MarketBook> getMatchOdds(MarketFilter filter, Set<MarketProjection> marketProjection,
			MarketSort sort, String maxResult, String appKey, String ssoId, String userName) throws APINGException {
	Map<String, Object> params = new HashMap<String, Object>();
		
		params.put(LOCALE, locale);
		params.put(FILTER, filter);
		params.put(SORT, sort);
		params.put(MAX_RESULT, maxResult);
		params.put(MARKET_PROJECTION, marketProjection);
		String result = getInstance().makeRequest(ApiNgOperation.LISTMARKETCATALOGUE.getOperationName(), params, appKey,
				ssoId);
		//log.info("MarketCatalogue: " + result);
		if (ApiNGDemo.isDebug())
			System.out.println("\nResponse: " + result);

		List<MarketCatalogue> container = JsonConverter.convertFromJson(result, new TypeToken<List<MarketCatalogue>>() {
		}.getType());
		
		Map<Long,String> runnerNameMap= new HashMap<Long,String>();
		List<MarketBook> marketBookList= new ArrayList<MarketBook>();
		List<MarketBook> marketBook = new ArrayList<MarketBook>();
		for (int i = 0; i < container.size(); i++) {
			
			List<String> marketIds =new ArrayList<String>();
			PriceProjection priceProjection = new PriceProjection();
			Set<PriceData> priceData = new HashSet<PriceData>();
			priceData.add(PriceData.EX_BEST_OFFERS);
			priceData.add(PriceData.EX_ALL_OFFERS);
			priceProjection.setPriceData(priceData);
			OrderProjection orderProjection = null;
			MatchProjection matchProjection = null;
			String currencyCode = null;
			int runnersSize = container.get(i).getRunners().size();
			
			for (int j = 0; j < runnersSize; j++) {
				runnerNameMap.put(container.get(i).getRunners().get(j).getSelectionId(), container.get(i).getRunners().get(j).getRunnerName());
				
			}
			String marketId = container.get(i).getMarketId();
			marketIds.add(marketId);
			marketBook =listMarketBook(marketIds, priceProjection, orderProjection, matchProjection, currencyCode, appKey, ssoId);
			for (int k = 0; k < marketBook.size(); k++) {
				MarketBook mBook=new MarketBook();
				mBook=marketBook.get(k);
				mBook.setMarketName(container.get(i).getMarketName());
				List<Runner> runnerList = new ArrayList<Runner>();
				for (int j = 0; j < mBook.getRunners().size(); j++) {
					if (mBook.getRunners().get(j).getEx().getAvailableToBack().size() > 0
							|| mBook.getRunners().get(j).getEx().getAvailableToLay().size() > 0) {
						mBook.getRunners().get(j)
								.setRunnerName(runnerNameMap.get(mBook.getRunners().get(j).getSelectionId()));
						log.info("GET_USER_PL_BY_SELECTION_ID: " + QueryListConstant.GET_USER_PL_BY_SELECTION_ID);

						double userPl = 0.0;
						int rowCount = jdbcTemplate.queryForObject(QueryListConstant.COUNT_USER_PL_BY_SELECTION_ID,
								new Object[] { mBook.getMarketId(), mBook.getRunners().get(j).getSelectionId(),
										userName },
								Integer.class);

						if (rowCount > 0) {
							List<PlaceBetsBean> userPlDouble = jdbcTemplate.query(
									QueryListConstant.GET_USER_PL_BY_SELECTION_ID, new Object[] { mBook.getMarketId(),
											mBook.getRunners().get(j).getSelectionId(), userName },
									(rs, rowNum) -> new PlaceBetsBean(rs.getDouble("USER_PL")));

							if (userPlDouble.size() > 0) {
								userPl = userPlDouble.get(0).getUserPl();
							} else {
								userPl = 0.0;
							}

							log.info("###########################userPlDouble###############################");
							log.info("userPlDouble:: " + userPl);
						}

						mBook.getRunners().get(j).setUserPl(userPl);

						runnerList.add(mBook.getRunners().get(j));
					}

				}
				mBook.setRunners(runnerList);
				 if(mBook.getRunners().size()>0) {
				 marketBookList.add(mBook);
				 }
				
			}
			//container.get(i).setMarketBook(marketBookList);
			//container.get(i).setRunners(null);
		}

		return marketBookList;
	}
	

	public MarketBook listRunnersBook(String marketId, String selectionId, PriceProjection priceProjection,
			OrderProjection orderProjection, MatchProjection matchProjection, String currencyCode, String appKey,
			String ssoId) throws APINGException {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put(LOCALE, locale);
		params.put(MARKET_ID, marketId);
		params.put(SELECTION_ID, selectionId);
		params.put(PRICE_PROJECTION, priceProjection);
		params.put(ORDER_PROJECTION, orderProjection);
		params.put(MATCH_PROJECTION, matchProjection);
		String result = getInstance().makeRequest(ApiNgOperation.LISTRUNNERSBOOK.getOperationName(), params, appKey,
				ssoId);
		if (ApiNGDemo.isDebug())
			System.out.println("\nResponse: " + result);

		List<MarketBook> container = JsonConverter.convertFromJson(result, new TypeToken<List<MarketBook>>() {
		}.getType());
		List<PriceSize> priceSizeList=new ArrayList<PriceSize>();
		PriceSize priceSize=new PriceSize();
		priceSize.setPrice(0.0);
		priceSize.setSize(0.0);
		priceSizeList.add(priceSize);
		Runner runner = new Runner();
		ExchangePrices exPrice = new ExchangePrices();
		for (int i = 0; i < container.size(); i++) {
			
			if(container.get(i).getRunners().get(0).getEx().getAvailableToBack().size()==0) {
				exPrice.setAvailableToBack(priceSizeList);
			}else {
				exPrice.setAvailableToBack(container.get(i).getRunners().get(0).getEx().getAvailableToBack());
			}
			
			if(container.get(i).getRunners().get(0).getEx().getAvailableToLay().size()==0) {
				exPrice.setAvailableToLay(priceSizeList);
			}else {
				exPrice.setAvailableToLay(container.get(i).getRunners().get(0).getEx().getAvailableToLay());
			}
			
			if(container.get(i).getRunners().get(0).getEx().getTradedVolume().size()==0) {
				exPrice.setTradedVolume(priceSizeList);
			}else {
				exPrice.setTradedVolume(container.get(i).getRunners().get(0).getEx().getTradedVolume());
			}
			runner.setStatus(container.get(i).getStatus());
			runner.setEx(exPrice);
		}

		return container.get(0);

	}

	public List<MarketCatalogue> listMarketCatalogue1(MarketFilter filter, Set<MarketProjection> marketProjection,
			MarketSort sort, String maxResult, String appKey, String ssoId) throws APINGException {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put(LOCALE, locale);
		params.put(FILTER, filter);
		params.put(SORT, sort);
		params.put(MAX_RESULT, maxResult);
		params.put(MARKET_PROJECTION, marketProjection);
		String result = getInstance().makeRequest(ApiNgOperation.LISTMARKETCATALOGUE.getOperationName(), params, appKey,
				ssoId);
		if (ApiNGDemo.isDebug())
			System.out.println("\nResponse: " + result);

		List<MarketCatalogue> container = JsonConverter.convertFromJson(result, new TypeToken<List<MarketCatalogue>>() {
		}.getType());

		return container;

	}

	public List<MarketBook> listMarketBook(List<String> marketIds, PriceProjection priceProjection,
			OrderProjection orderProjection, MatchProjection matchProjection, String currencyCode, String appKey,
			String ssoId) throws APINGException {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put(LOCALE, locale);
		params.put(MARKET_IDS, marketIds);
		params.put(PRICE_PROJECTION, priceProjection);
		params.put(ORDER_PROJECTION, orderProjection);
		params.put(MATCH_PROJECTION, matchProjection);
		String result = getInstance().makeRequest(ApiNgOperation.LISTMARKETBOOK.getOperationName(), params, appKey,
				ssoId);
		if (ApiNGDemo.isDebug())
			System.out.println("\nResponse: " + result);

		List<MarketBook> container = JsonConverter.convertFromJson(result, new TypeToken<List<MarketBook>>() {
		}.getType());

		return container;

	}
	
	@Override
	public List<MarketBook> listRunnersBook1(String marketIds, Long selectionId, PriceProjection priceProjection,
			OrderProjection orderProjection, MatchProjection matchProjection, String currencyCode, String appKey,
			String ssoId) throws APINGException {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put(LOCALE, locale);
		params.put(MARKET_ID, marketIds);
		params.put(SELECTION_ID, selectionId);
		params.put(PRICE_PROJECTION, priceProjection);
		params.put(ORDER_PROJECTION, orderProjection);
		params.put(MATCH_PROJECTION, matchProjection);
		String result = getInstance().makeRequest(ApiNgOperation.LISTRUNNERSBOOK.getOperationName(), params, appKey,
				ssoId);
		if (ApiNGDemo.isDebug())
			System.out.println("\nResponse: " + result);

		List<MarketBook> container = JsonConverter.convertFromJson(result, new TypeToken<List<MarketBook>>() {
		}.getType());

		return container;

	}
	
	@Override
	public Double runnerPrizeAndSize(String marketIds, Long selectionId, PriceProjection priceProjection,
			OrderProjection orderProjection, MatchProjection matchProjection, String currencyCode, String isBackLay, String appKey,
			String ssoId) throws APINGException {
		Double runnerPrize=0.0;
		Map<String, Object> params = new HashMap<String, Object>();
		params.put(LOCALE, locale);
		params.put(MARKET_ID, marketIds);
		params.put(SELECTION_ID, selectionId);
		params.put(PRICE_PROJECTION, priceProjection);
		params.put(ORDER_PROJECTION, orderProjection);
		params.put(MATCH_PROJECTION, matchProjection);
		String result = getInstance().makeRequest(ApiNgOperation.LISTRUNNERSBOOK.getOperationName(), params, appKey,
				ssoId);
		if (ApiNGDemo.isDebug())
			System.out.println("\nResponse: " + result);

		List<MarketBook> container = JsonConverter.convertFromJson(result, new TypeToken<List<MarketBook>>() {
		}.getType());
		
		if(container.size()>0) {
			if(container.get(0).getRunners().size()>0) {
				if(container.get(0).getRunners().get(0).getEx().getAvailableToBack().size()>0) {
					if(isBackLay.equalsIgnoreCase("BACK")) {
						runnerPrize=container.get(0).getRunners().get(0).getEx().getAvailableToBack().get(0).getPrice();
					}else if(isBackLay.equalsIgnoreCase("LAY")) {
						runnerPrize=container.get(0).getRunners().get(0).getEx().getAvailableToLay().get(0).getPrice();
					}
				}
			}
		}

		return runnerPrize;

	}

	@Override
	public SessionDetails getSessionToken(String userName, String password, String transactionId) {
		Map<String, Object> params = new HashMap<String, Object>();
		String requestString;
		List<SessionDetails> container = null;
		BetfailLoginRequestDto requestDto = new BetfailLoginRequestDto();
		params.put(FILTER, requestDto);
		params.put(LOCALE, locale);

		requestString = JsonConverter.convertToJson(params);

		HttpUtil requester = new HttpUtil();
		SessionDetails response = null;
		try {
			response = requester.sendPostRequestRescriptForLogin(requestString,
					ApiNgOperation.LOGIN.getOperationName());
			log.info("response: " + response);

		} catch (APINGException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return response;
	}

	protected String makeRequest(String operation, Map<String, Object> params, String appKey, String ssoToken)
			throws APINGException {
		String requestString;
		// Handling the Rescript request
		params.put("id", 1);

		requestString = JsonConverter.convertToJson(params);
		if (ApiNGDemo.isDebug())
			System.out.println("\nRequest: " + requestString);

		// We need to pass the "sendPostRequest" method a string in util format:
		// requestString
		HttpUtil requester = new HttpUtil();
		String response = requester.sendPostRequestRescript(requestString, operation, appKey, ssoToken);
		if (response != null)
			return response;
		else
			throw new APINGException();
	}


}
