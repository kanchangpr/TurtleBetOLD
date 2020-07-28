package com.jetbet.betfair;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import com.jetbet.bean.SportsBean;
import com.jetbet.betfair.entities.CompetitionResult;
import com.jetbet.betfair.entities.EventResult;
import com.jetbet.betfair.entities.EventTypeResult;
import com.jetbet.betfair.entities.MarketFilter;
import com.jetbet.betfair.exceptions.APINGException;
import com.jetbet.util.QueryListConstant;

import lombok.extern.slf4j.Slf4j;

/**
 * This is a demonstration class to show a quick demo of the new Betfair API-NG.
 * When you execute the class will:
 * <li>find a market (next horse race in the UK)</li>
 * <li>get prices and runners on this market</li>
 * <li>place a bet on 1 runner</li>
 * <li>handle the error</li>
 * 
 */
@Slf4j
public class ApiNGJRescriptDemo {

	private ApiNgOperations rescriptOperations = ApiNgRescriptOperations.getInstance();
	private String applicationKey;
	private String sessionToken;

	public List<EventTypeResult> getlistOfEventType(String appKey, String ssoid) {

		this.applicationKey = appKey;
		this.sessionToken = ssoid;
		List<EventTypeResult> r = new ArrayList<EventTypeResult>();
		try {
			MarketFilter marketFilter;
			marketFilter = new MarketFilter();

			Set<String> eventTypeIdSet = new HashSet<String>();
			eventTypeIdSet.add("1");
			eventTypeIdSet.add("4");
			marketFilter.setEventTypeIds(eventTypeIdSet);

			log.info("(listEventTypes) Get all Event Types...\n");
			r = rescriptOperations.listEventTypes(marketFilter, applicationKey, sessionToken);
			for (int i = 0; i < r.size(); i++) {
				log.info("Event Type: " + r.get(i).getEventType());
				log.info("Market Count:: " + r.get(i).getMarketCount());
			}
		} catch (APINGException apiExc) {
			log.info(apiExc.toString());
		}
		return r;
	}

	public List<CompetitionResult> getlistOfComp(List<SportsBean> sportsBeans,String appKey, String ssoid) {
		this.applicationKey = appKey;
		this.sessionToken = ssoid;
		List<CompetitionResult> response = new ArrayList<CompetitionResult>();
		try {
			MarketFilter marketFilter;
			marketFilter = new MarketFilter();
			for (SportsBean item : sportsBeans) {
				String eventTypeIdString=item.getSportsTypeId();
				System.out.println("eventTypeIdString: "+eventTypeIdString);
				Set<String> eventTypeIdSet1 = new HashSet<String>();
				eventTypeIdSet1.add(eventTypeIdString);
				marketFilter.setEventTypeIds(eventTypeIdSet1);
				log.info(marketFilter.getEventTypeIds().toString());

				log.info("(getlistOfComp) Get all Competition(Series)...\n");
				response = rescriptOperations.listComp(marketFilter, applicationKey, sessionToken);
				for (int i = 0; i < response.size(); i++) {
					response.get(i).setEventTypeId(eventTypeIdString);
					log.info("EventTypeId : " + response.get(i).getEventTypeId());
					log.info("Competiton : " + response.get(i).getCompetition());
					log.info("Market Count:: " + response.get(i).getMarketCount());
					log.info("Competition Region:: " + response.get(i).getCompetitionRegion());
				}
				
			}

		} catch (APINGException apiExc) {
			log.info(apiExc.toString());
		}
		return response;
	}

	public List<EventResult> getlistOfEvent(String appKey, String ssoid) {

		this.applicationKey = appKey;
		this.sessionToken = ssoid;
		List<EventResult> response = new ArrayList<EventResult>();
		try {
			MarketFilter marketFilter;
			marketFilter = new MarketFilter();

			Set<String> eventTypeIdSet = new HashSet<String>();
			eventTypeIdSet.add("4");
			marketFilter.setEventTypeIds(eventTypeIdSet);

			Set<String> eventIdSet = new HashSet<String>();
			eventIdSet.add("10058381");
			// marketFilter.setEventIds(eventIdSet);

			log.info("(listEvents) Get all Events...\n");
			response = rescriptOperations.listEvents(marketFilter, applicationKey, sessionToken);
			for (int i = 0; i < response.size(); i++) {
				log.info("Event : " + response.get(i).getEvent());
				log.info("Market Count:: " + response.get(i).getMarketCount());
			}
		} catch (APINGException apiExc) {
			log.info(apiExc.toString());
		}
		return response;
	}

}
