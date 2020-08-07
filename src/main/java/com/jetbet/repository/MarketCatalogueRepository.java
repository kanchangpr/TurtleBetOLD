package com.jetbet.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jetbet.bean.MarketCatalogueBean;

public interface MarketCatalogueRepository extends JpaRepository<MarketCatalogueBean, Long>{
	
	Long countByMarketIdAndRunnerSelectionId(String marketId,Long runnerSelectionId);

}
