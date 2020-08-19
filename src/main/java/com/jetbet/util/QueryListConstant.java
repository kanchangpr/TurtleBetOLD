package com.jetbet.util;

public class QueryListConstant {
	public static final String UPDATE_FROM_USER_ACC_CHIPS_SQL="UPDATE JETBET.JB_USER_DETAILS SET CHIPS= ?, LASTUPDATEDDATE=CURRENT_TIMESTAMP, LASTUPDATEBY=? WHERE USER_ID= ?";

	public static final String UPDATE_TO_USER_ACC_CHIPS_SQL="UPDATE JETBET.JB_USER_DETAILS SET CHIPS= ?, LASTUPDATEDDATE=CURRENT_TIMESTAMP, LASTUPDATEBY=? WHERE USER_ID= ?";

	//public static final String GET_PARENT_LIST_SQL="SELECT USER_ID FROM JETBET.JB_USER_DETAILS WHERE USER_ROLE = (SELECT USER_ROLE FROM JETBET.JB_USER_ROLES WHERE USER_LEVEL< (SELECT USER_LEVEL FROM JETBET.JB_USER_ROLES WHERE UPPER(USER_ROLE)= ? ) ORDER BY ID DESC LIMIT 1) ";
	
	public static final String GET_PARENT_LIST_SQL="WITH RECURSIVE TAB AS( SELECT USER_ID, PARENT FROM JETBET.JB_USER_DETAILS WHERE UPPER(USER_ID) = ? UNION ALL	SELECT J.USER_ID, J.PARENT FROM JETBET.JB_USER_DETAILS J, TAB WHERE J.PARENT = TAB.USER_ID ) SELECT USER_ID FROM JETBET.JB_USER_DETAILS J1 WHERE J1.USER_ID IN ( SELECT USER_ID FROM TAB) AND USER_ROLE IN (SELECT USER_ROLE FROM JETBET.JB_USER_ROLES WHERE USER_LEVEL< (SELECT USER_LEVEL FROM JETBET.JB_USER_ROLES WHERE UPPER(USER_ROLE)= ? ) ORDER BY ID DESC LIMIT 1)";
	
	public static final String GET_USER_ROLES_SQL="SELECT USER_ROLE FROM JETBET.JB_USER_ROLES WHERE USER_LEVEL > (SELECT DISTINCT USER_LEVEL FROM JETBET.JB_USER_ROLES WHERE UPPER(USER_ROLE)= ? )";
	
	public static final String GET_CHIPS_BALANCE_SQL="SELECT CASE WHEN USER_ID =? THEN 'USER' ELSE 'PARENT' END USER_ROLE, USER_ID, CHIPS FROM JETBET.JB_USER_DETAILS WHERE USER_ID = ( SELECT DISTINCT PARENT FROM JETBET.JB_USER_DETAILS WHERE USER_ID=?) OR USER_ID =? ORDER BY USER_ROLE  ";
	
	public static final String UPDATE_PASSWORD_SQL="UPDATE JETBET.JB_USER_DETAILS SET PASSWORD= ?, LASTUPDATEDDATE=CURRENT_TIMESTAMP, LASTUPDATEBY=? WHERE USER_ID= ?";
	
	public static final String GET_SERIES_LIST="SELECT SPORTS.SPORTS_NAME AS SPORTS_NAME, SERIES.* FROM JETBET.JB_SPORTS_DETAILS SPORTS , JETBET.JB_SERIES_DETAILS SERIES  WHERE  SERIES.SPORTS_ID = SPORTS.SPORTS_ID order by series_name";

	public static final String GET_SERIES_LIST_BY_SPORTS_ID="SELECT SPORTS.SPORTS_NAME AS SPORTS_NAME, SERIES.* FROM JETBET.JB_SPORTS_DETAILS SPORTS , JETBET.JB_SERIES_DETAILS SERIES  WHERE  SERIES.SPORTS_ID = SPORTS.SPORTS_ID AND SPORTS.SPORTS_ID = ? order by series_name";

	public static final String GET_MATCHES_LIST="SELECT SPORTS.SPORTS_NAME AS SPORTS_NAME,SERIES.SERIES_NAME AS SERIES_NAME, MATCH.* FROM JETBET.JB_SPORTS_DETAILS SPORTS , JETBET.JB_SERIES_DETAILS SERIES , JETBET.JB_MATCH_DETAILS MATCH  WHERE  SERIES.SPORTS_ID = SPORTS.SPORTS_ID AND SERIES.SERIES_ID =MATCH.SERIES_ID order by match_name";

	public static final String GET_MATCHES_LIST_BY_SPORTS_ID="SELECT SPORTS.SPORTS_NAME AS SPORTS_NAME,SERIES.SERIES_NAME AS SERIES_NAME, MATCH.* FROM JETBET.JB_SPORTS_DETAILS SPORTS , JETBET.JB_SERIES_DETAILS SERIES , JETBET.JB_MATCH_DETAILS MATCH  WHERE  SERIES.SPORTS_ID = SPORTS.SPORTS_ID AND SERIES.SERIES_ID =MATCH.SERIES_ID and match.sports_id=? order by match_name";

	public static final String GET_MATCHES_LIST_BY_SERIES_ID="SELECT SPORTS.SPORTS_NAME AS SPORTS_NAME,SERIES.SERIES_NAME AS SERIES_NAME, MATCH.* FROM JETBET.JB_SPORTS_DETAILS SPORTS , JETBET.JB_SERIES_DETAILS SERIES , JETBET.JB_MATCH_DETAILS MATCH  WHERE  SERIES.SPORTS_ID = SPORTS.SPORTS_ID AND SERIES.SERIES_ID =MATCH.SERIES_ID and match.series_id=? order by match_name";
	
	public static final String GET_MATCHES_LIST_BY_SPORTS_AND_SERIES_ID="SELECT SPORTS.SPORTS_NAME AS SPORTS_NAME,SERIES.SERIES_NAME AS SERIES_NAME, MATCH.* FROM JETBET.JB_SPORTS_DETAILS SPORTS , JETBET.JB_SERIES_DETAILS SERIES , JETBET.JB_MATCH_DETAILS MATCH  WHERE  SERIES.SPORTS_ID = SPORTS.SPORTS_ID AND SERIES.SERIES_ID =MATCH.SERIES_ID and match.sports_id=? and match.series_id=? order by match_name";

	public static final String GET_USER_DETAILS_BY_MASTER="WITH RECURSIVE TAB AS(	SELECT USER_ID, PARENT FROM JETBET.JB_USER_DETAILS WHERE USER_ID = ? UNION ALL	SELECT J.USER_ID, J.PARENT FROM JETBET.JB_USER_DETAILS J, TAB	WHERE J.PARENT = TAB.USER_ID ) SELECT * FROM JETBET.JB_USER_DETAILS J1 WHERE J1.USER_ID IN ( SELECT USER_ID FROM TAB) AND USER_ID <> ? ";
	
	public static final String SPORTS_CONTROL_FOR_SPORTS_PAGE="UPDATE JETBET.JB_SPORTS_DETAILS SET IS_ACTIVE= ? , SPORTS_UPDATED_BY = ? , SPORTS_UPDATED_DATE   = CURRENT_TIMESTAMP WHERE SPORTS_ID = ? ";
	
	public static final String SPORTS_CONTROL_FOR_SERIES_PAGE="UPDATE JETBET.JB_SERIES_DETAILS SET IS_ACTIVE= ? , SERIES_UPDATED_BY = ? , SERIES_UPDATED_DATE   = CURRENT_TIMESTAMP WHERE SPORTS_ID = ? ";
	
	public static final String SPORTS_CONTROL_FOR_MATCH_PAGE="UPDATE JETBET.JB_MATCH_DETAILS SET IS_ACTIVE= ? , MATCH_UPDATED_BY = ? , MATCH_UPDATED_DATE   = CURRENT_TIMESTAMP WHERE SPORTS_ID = ?";
	
	public static final String SERIES_CONTROL_FOR_SERIES_PAGE="UPDATE JETBET.JB_SERIES_DETAILS SET IS_ACTIVE= ? , SERIES_UPDATED_BY = ? , SERIES_UPDATED_DATE   = CURRENT_TIMESTAMP WHERE SERIES_ID = ? ";
	
	public static final String SERIES_CONTROL_FOR_MATCH_PAGE="UPDATE JETBET.JB_MATCH_DETAILS SET IS_ACTIVE= ? , MATCH_UPDATED_BY = ? , MATCH_UPDATED_DATE   = CURRENT_TIMESTAMP WHERE SERIES_ID = ?";
	
	public static final String MATCH_CONTROL_FOR_MATCH_PAGE="UPDATE JETBET.JB_MATCH_DETAILS SET IS_ACTIVE= ? , MATCH_UPDATED_BY = ? , MATCH_UPDATED_DATE   = CURRENT_TIMESTAMP WHERE MATCH_ID = ?";
	
	public static final String MATCH_CONTROL_FOR_FANCY_PAGE="UPDATE JETBET.JB_FANCY_DETAILS SET IS_ACTIVE= ? , FANCY_UPDATED_BY= ? , FANCY_UPDATED_DATE=CURRENT_TIMESTAMP WHERE MATCH_ID= ? ";
	
	public static final String GET_FANCY_LIST="SELECT MATCH.MATCH_NAME AS MATCH_NAME, FANCY.* FROM JETBET.JB_MATCH_DETAILS MATCH , JETBET.JB_FANCY_DETAILS FANCY WHERE  FANCY.MATCH_ID = MATCH.MATCH_ID ";

	public static final String UPDATE_USER_CHIPS="UPDATE JETBET.JB_USER_DETAILS SET CHIPS=? , LASTUPDATEBY =? , LASTUPDATEDDATE=CURRENT_TIMESTAMP WHERE USER_ID=?";

	public static final String BET_HISTORY_BY_DATE_RANGE="SELECT * FROM JETBET.JB_BET_DETAILS WHERE BET_PLACE_DATE BETWEEN (? ::DATE) AND (? ::DATE) AND USER_ID=? ORDER BY ID DESC ";

	public static final String ACCOUNT_HISTORY_BY_DATE_RANGE="SELECT * FROM JETBET.JB_CHIPS_DETAILS WHERE CREATED_ON BETWEEN (? ::DATE) AND (? ::DATE) AND USER_ID=? ORDER BY ID DESC";
	
	public static final String LOGIN_HISTORY_BY_DATE_RANGE="SELECT * FROM JETBET.JB_LOGIN_DETAILS WHERE LOGIN_TIME BETWEEN (? ::DATE) AND (? ::DATE) AND USER_ID=? ORDER BY ID DESC";
	
	public static final String UPDATE_FANCY_DETAIL="UPDATE JETBET.JB_FANCY_DETAILS SET IS_ACTIVE= ? , FANCY_UPDATED_BY= ? , FANCY_UPDATED_DATE=CURRENT_TIMESTAMP WHERE MARKET_TYPE= ? AND MATCH_ID = (SELECT DISTINCT MATCH_ID FROM JETBET.JB_MATCH_DETAILS WHERE MATCH_NAME= ? )";

	public static final String UPDATE_BET_STATUS="UPDATE JETBET.JB_BET_DETAILS SET BET_STATUS = ? , LAST_UPDATED_DATE=CURRENT_TIMESTAMP , LAST_UPDATED_BY='TURTLE_BETS' WHERE SELECTION_ID=? AND MARKET_ID=? AND BET_STATUS='ACTIVE'";
	
	public static final String UPDATE_BET_RESULT="UPDATE JETBET.JB_BET_DETAILS T SET BET_RESULT = (CASE WHEN ISBACK = 'Y' AND ISLAY ='N' AND BET_STATUS = 'WINNER' THEN 'WON' WHEN ISBACK = 'Y' AND ISLAY ='N' AND BET_STATUS = 'LOSER' THEN 'LOST' WHEN ISBACK = 'N' AND ISLAY ='Y' AND BET_STATUS = 'LOSER' THEN 'WON' WHEN ISBACK = 'N' AND ISLAY ='Y' AND BET_STATUS = 'WINNER' THEN 'LOST' WHEN BET_STATUS ='REMOVED' THEN 'REMOVED'  ELSE BET_RESULT END) WHERE BET_RESULT NOT IN ('WON','LOST') AND BET_STATUS <> 'ACTIVE'";

	public static final String OPEN_BET_FOR_MASTERS="SELECT USER_ID,MATCH_ID,MATCH_NAME, MARKET_ID,MARKET_NAME, SELECTION_ID,RUNNER_NAME, ISBACK,ISLAY,ODDS,STAKE,LIABILITY,BET_PLACE_DATE FROM JETBET.JB_BET_DETAILS WHERE USER_ID IN ( WITH RECURSIVE TAB AS(	SELECT USER_ID, PARENT FROM JETBET.JB_USER_DETAILS WHERE USER_ID =  ?  UNION ALL	SELECT J.USER_ID, J.PARENT FROM JETBET.JB_USER_DETAILS J, TAB  WHERE J.PARENT = TAB.USER_ID ) SELECT USER_ID FROM JETBET.JB_USER_DETAILS J1 WHERE J1.USER_ID IN ( SELECT USER_ID FROM TAB) AND UPPER(USER_ROLE)= 'USER' ) AND 	BET_RESULT='OPEN' AND MATCH_ID=? AND MARKET_ID=?";

}
