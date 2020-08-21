package com.jetbet.betfair;

import java.util.List;
import java.util.Locale;
import java.util.Set;

import com.jetbet.betfair.entities.CompetitionResult;
import com.jetbet.betfair.entities.EventResult;
import com.jetbet.betfair.entities.EventTypeResult;
import com.jetbet.betfair.entities.MarketBook;
import com.jetbet.betfair.entities.MarketCatalogue;
import com.jetbet.betfair.entities.MarketFancyResult;
import com.jetbet.betfair.entities.MarketFilter;
import com.jetbet.betfair.entities.PriceProjection;
import com.jetbet.betfair.enums.MarketSort;
import com.jetbet.betfair.enums.MatchProjection;
import com.jetbet.betfair.enums.OrderProjection;
import com.jetbet.betfair.exceptions.APINGException;
import com.jetbet.betfair.enums.MarketProjection;
import com.jetbet.dto.SessionDetails;


public abstract class ApiNgOperations {
	protected final String FILTER = "filter";
    protected final String LOCALE = "locale";
    protected final String SORT = "sort";
    protected final String MAX_RESULT = "maxResults";
    protected final String MARKET_IDS = "marketIds";
    protected final String MARKET_ID = "marketId";
    protected final String SELECTION_ID="selectionId";
    protected final String INSTRUCTIONS = "instructions";
    protected final String CUSTOMER_REF = "customerRef";
    protected final String MARKET_PROJECTION = "marketProjection";
    protected final String PRICE_PROJECTION = "priceProjection";
    protected final String MATCH_PROJECTION = "matchProjection";
    protected final String ORDER_PROJECTION = "orderProjection";
    protected final String locale = Locale.getDefault().toString();

	public abstract List<EventTypeResult> listEventTypes(MarketFilter filter, String appKey, String ssoId) throws APINGException;
	
	public abstract List<CompetitionResult> listComp(MarketFilter filter, String appKey, String ssoId) throws APINGException;
	
	public abstract List<EventResult> listEvents(MarketFilter filter, String appKey, String ssoId) throws APINGException;
	
	public abstract List<MarketFancyResult> listFancy(MarketFilter filter, String appKey, String ssoId) throws APINGException;

	public abstract SessionDetails getSessionToken(String userName, String password, String transactionId) ;

	public abstract List<MarketBook> listMarketBook(List<String> marketIds, PriceProjection priceProjection, OrderProjection orderProjection,
						MatchProjection matchProjection, String currencyCode, String appKey, String ssoId) throws APINGException;
	
	public abstract List<MarketBook> listRunnersBook1(String marketIds,Long selectionId, PriceProjection priceProjection, OrderProjection orderProjection,
			MatchProjection matchProjection, String currencyCode, String appKey, String ssoId) throws APINGException;
	
	public abstract Double runnerPrizeAndSize(String marketIds,Long selectionId, PriceProjection priceProjection, OrderProjection orderProjection,
			MatchProjection matchProjection, String currencyCode,String isBackLay, String appKey, String ssoId) throws APINGException;

    public abstract List<MarketCatalogue> listMarketCatalogue(MarketFilter filter, Set<MarketProjection> marketProjection,
        MarketSort sort, String maxResult, String appKey, String ssoId) throws APINGException;
//
//	public abstract PlaceExecutionReport placeOrders(String marketId, List<PlaceInstruction> instructions, String customerRef , String appKey, String ssoId) throws APINGException;
//
//    protected abstract String makeRequest(String operation, Map<String, Object> params, String appKey, String ssoToken) throws  APINGException;

}

