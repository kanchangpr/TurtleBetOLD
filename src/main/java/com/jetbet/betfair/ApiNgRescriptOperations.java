package com.jetbet.betfair;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.reflect.TypeToken;
import com.jetbet.betfair.entities.CompetitionResult;
import com.jetbet.betfair.entities.EventResult;
import com.jetbet.betfair.entities.EventTypeResult;
import com.jetbet.betfair.entities.MarketFancyResult;
import com.jetbet.betfair.entities.MarketFilter;
import com.jetbet.betfair.enums.ApiNgOperation;
import com.jetbet.betfair.exceptions.APINGException;
import com.jetbet.betfair.util.JsonConverter;
import com.jetbet.dto.BetfailLoginRequestDto;
import com.jetbet.dto.SessionDetails;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ApiNgRescriptOperations extends ApiNgOperations {

    private static ApiNgRescriptOperations instance = null;

    private ApiNgRescriptOperations(){}

    public static ApiNgRescriptOperations getInstance(){
        if (instance == null){
            instance = new ApiNgRescriptOperations();
        }
        return instance;
    }

    public List<EventTypeResult> listEventTypes(MarketFilter filter, String appKey, String ssoId) throws APINGException {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put(FILTER, filter);
        params.put(LOCALE, locale);
        String result = getInstance().makeRequest(ApiNgOperation.LISTEVENTTYPES.getOperationName(), params, appKey, ssoId);
        if(ApiNGDemo.isDebug())
            System.out.println("\nResponse: "+result);
        List<EventTypeResult> container = JsonConverter.convertFromJson(result, new TypeToken<List<EventTypeResult>>() {}.getType());
        return container;

    }
  
    
    public List<CompetitionResult> listComp(MarketFilter filter, String appKey, String ssoId) throws APINGException {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put(FILTER, filter);
        params.put(LOCALE, locale);
        String result = getInstance().makeRequest(ApiNgOperation.LISTCOMPETITIONS.getOperationName(), params, appKey, ssoId);
        if(ApiNGDemo.isDebug())
            System.out.println("\nResponse: "+result);
        List<CompetitionResult> container = JsonConverter.convertFromJson(result, new TypeToken<List<CompetitionResult>>() {}.getType());
        return container;

    }
    
    public List<EventResult> listEvents(MarketFilter filter, String appKey, String ssoId) throws APINGException {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put(FILTER, filter);
        params.put(LOCALE, locale);
        String result = getInstance().makeRequest(ApiNgOperation.LISTEVENTS.getOperationName(), params, appKey, ssoId);
        if(ApiNGDemo.isDebug())
            System.out.println("\nResponse: "+result);
        List<EventResult> container = JsonConverter.convertFromJson(result, new TypeToken<List<EventResult>>() {}.getType());
        return container;

    }
    
    public List<MarketFancyResult> listFancy(MarketFilter filter, String appKey, String ssoId) throws APINGException {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put(FILTER, filter);
        params.put(LOCALE, locale);
        String result = getInstance().makeRequest(ApiNgOperation.LISTMARKETTYPES.getOperationName(), params, appKey, ssoId);
        if(ApiNGDemo.isDebug())
            System.out.println("\nResponse: "+result);
        List<MarketFancyResult> container = JsonConverter.convertFromJson(result, new TypeToken<List<MarketFancyResult>>() {}.getType());
        return container;

    }
    
    @Override
	public SessionDetails getSessionToken(String userName, String password, String transactionId) {
    	Map<String, Object> params = new HashMap<String, Object>();
    	String requestString;
    	List<SessionDetails> container = null;
    	BetfailLoginRequestDto requestDto= new BetfailLoginRequestDto();
        params.put(FILTER, requestDto);
        params.put(LOCALE, locale);
        
        requestString =  JsonConverter.convertToJson(params);
        
        HttpUtil requester = new HttpUtil();
        SessionDetails response = null;
		try {
			response = requester.sendPostRequestRescriptForLogin(requestString, ApiNgOperation.LOGIN.getOperationName());
			log.info("response: "+response);
			

		} catch (APINGException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
      	return response;
	}

    protected String makeRequest(String operation, Map<String, Object> params, String appKey, String ssoToken) throws APINGException {
        String requestString;
        //Handling the Rescript request
        params.put("id", 1);

        requestString =  JsonConverter.convertToJson(params);
        if(ApiNGDemo.isDebug())
            System.out.println("\nRequest: "+requestString);

        //We need to pass the "sendPostRequest" method a string in util format:  requestString
        HttpUtil requester = new HttpUtil();
        String response = requester.sendPostRequestRescript(requestString, operation, appKey, ssoToken);
        if(response != null)
            return response;
        else
            throw new APINGException();
    }

	

}

