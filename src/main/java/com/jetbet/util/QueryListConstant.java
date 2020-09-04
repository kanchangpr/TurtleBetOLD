package com.jetbet.util;

public class QueryListConstant {
	public static final String UPDATE_FROM_USER_ACC_CHIPS_SQL="UPDATE JETBET.JB_USER_DETAILS SET avail_limit= ?,AVAIL_BALANCE=?, LASTUPDATEDDATE=CURRENT_TIMESTAMP, LASTUPDATEBY=? WHERE USER_ID= ?";

	public static final String UPDATE_TO_USER_ACC_CHIPS_SQL="UPDATE JETBET.JB_USER_DETAILS SET avail_limit= ?,AVAIL_BALANCE=?, LASTUPDATEDDATE=CURRENT_TIMESTAMP, LASTUPDATEBY=? WHERE USER_ID= ?";

	//public static final String GET_PARENT_LIST_SQL="SELECT USER_ID FROM JETBET.JB_USER_DETAILS WHERE USER_ROLE = (SELECT USER_ROLE FROM JETBET.JB_USER_ROLES WHERE USER_LEVEL< (SELECT USER_LEVEL FROM JETBET.JB_USER_ROLES WHERE UPPER(USER_ROLE)= ? ) ORDER BY ID DESC LIMIT 1) ";
	
	public static final String GET_PARENT_LIST_SQL="WITH RECURSIVE TAB AS( SELECT USER_ID, PARENT FROM JETBET.JB_USER_DETAILS WHERE UPPER(USER_ID) = ? UNION ALL	SELECT J.USER_ID, J.PARENT FROM JETBET.JB_USER_DETAILS J, TAB WHERE J.PARENT = TAB.USER_ID ) SELECT USER_ID FROM JETBET.JB_USER_DETAILS J1 WHERE J1.USER_ID IN ( SELECT USER_ID FROM TAB) AND USER_ROLE IN (SELECT USER_ROLE FROM JETBET.JB_USER_ROLES WHERE USER_LEVEL< (SELECT USER_LEVEL FROM JETBET.JB_USER_ROLES WHERE UPPER(USER_ROLE)= ? ) ORDER BY ID DESC LIMIT 1)";
	
	public static final String GET_USER_ROLES_SQL="SELECT USER_ROLE FROM JETBET.JB_USER_ROLES WHERE USER_LEVEL > (SELECT DISTINCT USER_LEVEL FROM JETBET.JB_USER_ROLES WHERE UPPER(USER_ROLE)= ? )";
	
	public static final String GET_CHIPS_BALANCE_SQL="SELECT CASE WHEN USER_ID =? THEN 'USER' ELSE 'PARENT' END USER_ROLE, USER_ID, AVAIL_BALANCE FROM JETBET.JB_USER_DETAILS WHERE USER_ID = ( SELECT DISTINCT PARENT FROM JETBET.JB_USER_DETAILS WHERE USER_ID=?) OR USER_ID =? ORDER BY USER_ROLE  ";
	
	public static final String RESET_PASSWORD_SQL="UPDATE JETBET.JB_USER_DETAILS SET PASSWORD= ?, IS_PWD_UPDATED='N', LASTUPDATEDDATE=CURRENT_TIMESTAMP, LASTUPDATEBY=? WHERE USER_ID= ?";
	
	public static final String UPDATE_PASSWORD_SQL="UPDATE JETBET.JB_USER_DETAILS SET PASSWORD= ?, IS_PWD_UPDATED='Y', LASTUPDATEDDATE=CURRENT_TIMESTAMP, LASTUPDATEBY=? WHERE USER_ID= ?";
	
	public static final String GET_SERIES_LIST="SELECT SPORTS.SPORTS_NAME AS SPORTS_NAME, SERIES.* FROM JETBET.JB_SPORTS_DETAILS SPORTS , JETBET.JB_SERIES_DETAILS SERIES  WHERE  SERIES.SPORTS_ID = SPORTS.SPORTS_ID order by series_name";

	public static final String GET_SERIES_LIST_BY_SPORTS_ID="SELECT SPORTS.SPORTS_NAME AS SPORTS_NAME, SERIES.* FROM JETBET.JB_SPORTS_DETAILS SPORTS , JETBET.JB_SERIES_DETAILS SERIES  WHERE  SERIES.SPORTS_ID = SPORTS.SPORTS_ID AND SPORTS.SPORTS_ID = ? order by series_name";

	public static final String GET_MATCHES_LIST="SELECT SPORTS.SPORTS_NAME AS SPORTS_NAME,SERIES.SERIES_NAME AS SERIES_NAME, MATCH.* FROM JETBET.JB_SPORTS_DETAILS SPORTS , JETBET.JB_SERIES_DETAILS SERIES , JETBET.JB_MATCH_DETAILS MATCH  WHERE  SERIES.SPORTS_ID = SPORTS.SPORTS_ID AND SERIES.SERIES_ID =MATCH.SERIES_ID order by match_name";

	public static final String GET_MATCHES_LIST_BY_SPORTS_ID="SELECT SPORTS.SPORTS_NAME AS SPORTS_NAME,SERIES.SERIES_NAME AS SERIES_NAME, MATCH.* FROM JETBET.JB_SPORTS_DETAILS SPORTS , JETBET.JB_SERIES_DETAILS SERIES , JETBET.JB_MATCH_DETAILS MATCH  WHERE  SERIES.SPORTS_ID = SPORTS.SPORTS_ID AND SERIES.SERIES_ID =MATCH.SERIES_ID and match.sports_id=? order by match_name";

	public static final String GET_MATCHES_LIST_BY_SERIES_ID="SELECT SPORTS.SPORTS_NAME AS SPORTS_NAME,SERIES.SERIES_NAME AS SERIES_NAME, MATCH.* FROM JETBET.JB_SPORTS_DETAILS SPORTS , JETBET.JB_SERIES_DETAILS SERIES , JETBET.JB_MATCH_DETAILS MATCH  WHERE  SERIES.SPORTS_ID = SPORTS.SPORTS_ID AND SERIES.SERIES_ID =MATCH.SERIES_ID and match.series_id=? order by match_name";
	
	public static final String GET_MATCHES_LIST_BY_SPORTS_AND_SERIES_ID="SELECT SPORTS.SPORTS_NAME AS SPORTS_NAME,SERIES.SERIES_NAME AS SERIES_NAME, MATCH.* FROM JETBET.JB_SPORTS_DETAILS SPORTS , JETBET.JB_SERIES_DETAILS SERIES , JETBET.JB_MATCH_DETAILS MATCH  WHERE  SERIES.SPORTS_ID = SPORTS.SPORTS_ID AND SERIES.SERIES_ID =MATCH.SERIES_ID and match.sports_id=? and match.series_id=? order by match_name";

	public static final String GET_USER_DETAILS_BY_MASTER="WITH RECURSIVE TAB AS(	SELECT USER_ID, PARENT FROM JETBET.JB_USER_DETAILS WHERE USER_ID = ? UNION ALL	SELECT J.USER_ID, J.PARENT FROM JETBET.JB_USER_DETAILS J, TAB	WHERE J.PARENT = TAB.USER_ID ) SELECT * FROM JETBET.JB_USER_DETAILS J1 WHERE J1.USER_ID IN ( SELECT USER_ID FROM TAB) AND USER_ID <> ? order by user_id";
	
	public static final String SPORTS_CONTROL_FOR_SPORTS_PAGE="UPDATE JETBET.JB_SPORTS_DETAILS SET IS_ACTIVE= ? , SPORTS_UPDATED_BY = ? , SPORTS_UPDATED_DATE   = CURRENT_TIMESTAMP WHERE SPORTS_ID = ? ";
	
	public static final String SPORTS_CONTROL_FOR_SERIES_PAGE="UPDATE JETBET.JB_SERIES_DETAILS SET IS_ACTIVE= ? , SERIES_UPDATED_BY = ? , SERIES_UPDATED_DATE   = CURRENT_TIMESTAMP WHERE SPORTS_ID = ? ";
	
	public static final String SPORTS_CONTROL_FOR_MATCH_PAGE="UPDATE JETBET.JB_MATCH_DETAILS SET IS_ACTIVE= ? , MATCH_UPDATED_BY = ? , MATCH_UPDATED_DATE   = CURRENT_TIMESTAMP WHERE SPORTS_ID = ?";
	
	public static final String SPORTS_CONTROL_FOR_FANCY_PAGE="UPDATE JETBET.JB_FANCY_DETAILS SET IS_ACTIVE= ? , FANCY_UPDATED_BY = ? , FANCY_UPDATED_DATE   = CURRENT_TIMESTAMP WHERE SPORTS_ID = ?";
	
	public static final String SERIES_CONTROL_FOR_SERIES_PAGE="UPDATE JETBET.JB_SERIES_DETAILS SET IS_ACTIVE= ? , SERIES_UPDATED_BY = ? , SERIES_UPDATED_DATE   = CURRENT_TIMESTAMP WHERE SERIES_ID = ? ";
	
	public static final String SERIES_CONTROL_FOR_MATCH_PAGE="UPDATE JETBET.JB_MATCH_DETAILS SET IS_ACTIVE= ? , MATCH_UPDATED_BY = ? , MATCH_UPDATED_DATE   = CURRENT_TIMESTAMP WHERE SERIES_ID = ?";
	
	public static final String SERIES_CONTROL_FOR_FANCY_PAGE="UPDATE JETBET.JB_FANCY_DETAILS SET IS_ACTIVE= ? , FANCY_UPDATED_BY = ? , FANCY_UPDATED_DATE   = CURRENT_TIMESTAMP WHERE SERIES_ID = ?";
	
	public static final String MATCH_CONTROL_FOR_MATCH_PAGE="UPDATE JETBET.JB_MATCH_DETAILS SET IS_ACTIVE= ? , MATCH_UPDATED_BY = ? , MATCH_UPDATED_DATE   = CURRENT_TIMESTAMP WHERE MATCH_ID = ?";
	
	public static final String MATCH_CONTROL_FOR_FANCY_PAGE="UPDATE JETBET.JB_FANCY_DETAILS SET IS_ACTIVE= ? , FANCY_UPDATED_BY= ? , FANCY_UPDATED_DATE  =  CURRENT_TIMESTAMP WHERE MATCH_ID= ? ";
	
	//public static final String GET_FANCY_LIST="SELECT MATCH.MATCH_NAME AS MATCH_NAME, FANCY.* FROM JETBET.JB_MATCH_DETAILS MATCH , JETBET.JB_FANCY_DETAILS FANCY WHERE  FANCY.MATCH_ID = MATCH.MATCH_ID ";
	
	public static final String GET_FANCY_LIST="SELECT MATCH.MATCH_NAME AS MATCH_NAME,SERIES.SERIES_NAME AS SERIES_NAME, FANCY.* FROM JETBET.JB_MATCH_DETAILS MATCH , JETBET.JB_FANCY_DETAILS FANCY, JETBET.JB_SERIES_DETAILS SERIES  WHERE  FANCY.MATCH_ID = MATCH.MATCH_ID AND SERIES.SERIES_ID=FANCY.SERIES_ID ";
	
	public static final String UPDATE_USER_CHIPS="UPDATE JETBET.JB_USER_DETAILS SET avail_balance=? ,liability=?, LASTUPDATEBY =? , LASTUPDATEDDATE=CURRENT_TIMESTAMP WHERE USER_ID=?";

	public static final String BET_HISTORY_BY_DATE_RANGE="SELECT * FROM JETBET.JB_BET_DETAILS WHERE BET_PLACE_DATE BETWEEN (? ::TIMESTAMP) AND (? ::TIMESTAMP) AND USER_ID=? ORDER BY ID DESC ";

	public static final String ACCOUNT_HISTORY_BY_DATE_RANGE="SELECT * FROM JETBET.JB_CHIPS_DETAILS WHERE CREATED_ON BETWEEN (? ::TIMESTAMP) AND (? ::TIMESTAMP) AND USER_ID=? ORDER BY ID DESC";
	
	public static final String LOGIN_HISTORY_BY_DATE_RANGE="SELECT * FROM JETBET.JB_LOGIN_DETAILS WHERE LOGIN_TIME BETWEEN (? ::TIMESTAMP) AND (? ::TIMESTAMP) AND USER_ID=? ORDER BY ID DESC";
	
	public static final String PROFIT_AND_LOSS_HISTORY_BY_DATE_RANGE="SELECT * FROM JETBET.JB_BET_DETAILS WHERE BET_RESULT<>'OPEN' AND BET_PLACE_DATE BETWEEN (? ::TIMESTAMP) AND (? ::TIMESTAMP) AND USER_ID=? ORDER BY ID DESC ";
	
	//public static final String UPDATE_FANCY_DETAIL="UPDATE JETBET.JB_FANCY_DETAILS SET IS_ACTIVE= ? , FANCY_UPDATED_BY= ? , FANCY_UPDATED_DATE=CURRENT_TIMESTAMP WHERE MARKET_TYPE= ? AND MATCH_ID = (SELECT DISTINCT MATCH_ID FROM JETBET.JB_MATCH_DETAILS WHERE MATCH_NAME= ? )";

	public static final String UPDATE_FANCY_DETAIL="UPDATE JETBET.JB_FANCY_DETAILS SET IS_ACTIVE= ? , FANCY_UPDATED_BY= ? , FANCY_UPDATED_DATE=CURRENT_TIMESTAMP WHERE MARKET_TYPE= ? AND MATCH_ID =  ? ";
	
	public static final String UPDATE_BET_STATUS="UPDATE JETBET.JB_BET_DETAILS SET BET_STATUS = ? , LAST_UPDATED_DATE=CURRENT_TIMESTAMP , LAST_UPDATED_BY='TURTLE_BETS' WHERE SELECTION_ID=? AND MARKET_ID=? AND BET_STATUS='ACTIVE'";
	
	public static final String UPDATE_BET_RESULT="UPDATE JETBET.JB_BET_DETAILS T SET BET_RESULT = (CASE WHEN ISBACK = 'Y' AND ISLAY ='N' AND BET_STATUS = 'WINNER' THEN 'WON' WHEN ISBACK = 'Y' AND ISLAY ='N' AND BET_STATUS = 'LOSER' THEN 'LOST' WHEN ISBACK = 'N' AND ISLAY ='Y' AND BET_STATUS = 'LOSER' THEN 'WON' WHEN ISBACK = 'N' AND ISLAY ='Y' AND BET_STATUS = 'WINNER' THEN 'LOST' WHEN BET_STATUS ='REMOVED' THEN 'REMOVED'  ELSE BET_RESULT END) WHERE BET_RESULT NOT IN ('WON','LOST') AND BET_STATUS <> 'ACTIVE'";

	public static final String OPEN_BET_FOR_MASTERS="SELECT ID, USER_ID,MATCH_ID,MATCH_NAME, MARKET_ID,MARKET_NAME, SELECTION_ID,RUNNER_NAME, ISBACK,ISLAY,ODDS,STAKE,LIABILITY,BET_PLACE_DATE FROM JETBET.JB_BET_DETAILS WHERE USER_ID IN ( WITH RECURSIVE TAB AS(	SELECT USER_ID, PARENT FROM JETBET.JB_USER_DETAILS WHERE USER_ID =  ?  UNION ALL	SELECT J.USER_ID, J.PARENT FROM JETBET.JB_USER_DETAILS J, TAB  WHERE J.PARENT = TAB.USER_ID ) SELECT USER_ID FROM JETBET.JB_USER_DETAILS J1 WHERE J1.USER_ID IN ( SELECT USER_ID FROM TAB) AND UPPER(USER_ROLE)= 'USER' ) AND 	BET_RESULT='OPEN' AND MATCH_ID=?  AND SPORTS_ID= ? order by id desc";

	public static final String GET_BET_SETTLEMENT_DATA_MINUS="SELECT USER_ID , SUM(STAKE) STAKES, SUM(LIABILITY) LIABILITY, SUM(PROFIT) PROFIT,SUM(LOSS) LOSS,SUM(NET_AMOUNT) AMOUNT,SUM(COMMISION) COMMISION,SUM(ADMIN_STAKES) ADMIN_STAKES ,SUM(SM_STAKES) SM_STAKES,SUM(MASTER_STAKES) MASTER_STAKES FROM JETBET.JB_BET_DETAILS WHERE BET_SETTLEMENT='PENDING' AND USER_ID IN ( WITH RECURSIVE TAB AS(	SELECT USER_ID, PARENT FROM JETBET.JB_USER_DETAILS WHERE USER_ID =  ?  UNION ALL	SELECT J.USER_ID, J.PARENT FROM JETBET.JB_USER_DETAILS J, TAB  WHERE J.PARENT = TAB.USER_ID ) SELECT USER_ID FROM JETBET.JB_USER_DETAILS J1 WHERE J1.USER_ID IN ( SELECT USER_ID FROM TAB) AND UPPER(USER_ROLE)= 'USER' ) GROUP BY USER_ID HAVING SUM(NET_AMOUNT) < 0";	
	
	public static final String GET_BET_SETTLEMENT_DATA_PLUS="SELECT USER_ID , SUM(STAKE) STAKES, SUM(LIABILITY) LIABILITY, SUM(PROFIT) PROFIT,SUM(LOSS) LOSS,SUM(NET_AMOUNT) AMOUNT,SUM(COMMISION) COMMISION,SUM(ADMIN_STAKES) ADMIN_STAKES ,SUM(SM_STAKES) SM_STAKES,SUM(MASTER_STAKES) MASTER_STAKES FROM JETBET.JB_BET_DETAILS WHERE BET_SETTLEMENT='PENDING' AND USER_ID IN ( WITH RECURSIVE TAB AS(	SELECT USER_ID, PARENT FROM JETBET.JB_USER_DETAILS WHERE USER_ID =  ?  UNION ALL	SELECT J.USER_ID, J.PARENT FROM JETBET.JB_USER_DETAILS J, TAB  WHERE J.PARENT = TAB.USER_ID ) SELECT USER_ID FROM JETBET.JB_USER_DETAILS J1 WHERE J1.USER_ID IN ( SELECT USER_ID FROM TAB) AND UPPER(USER_ROLE)= 'USER' ) GROUP BY USER_ID HAVING SUM(NET_AMOUNT) > 0";	
	
	public static final String GET_PARENT_LIST="WITH RECURSIVE TAB AS( SELECT USER_ID, PARENT FROM JETBET.JB_USER_DETAILS WHERE UPPER(USER_ID) = ?  UNION ALL	SELECT J.USER_ID, J.PARENT FROM JETBET.JB_USER_DETAILS J, TAB WHERE J.USER_ID = TAB.PARENT ) SELECT USER_ID ,USER_ROLE FROM JETBET.JB_USER_DETAILS J1 WHERE J1.USER_ID IN ( SELECT USER_ID FROM TAB)";

	public static final String GET_SETTLEMENT_FOR_MASTER_PLUS="SELECT USER_ID AS USER_ID , PARENT, SUM(STAKE) STAKES, SUM(LIABILITY) LIABILITY, SUM(PROFIT) PROFIT,SUM(LOSS) LOSS,SUM(NET_AMOUNT) AMOUNT,SUM(COMMISION) COMMISION, SUM(ADMIN_STAKES) ADMIN_STAKES ,SUM(SM_STAKES) SM_STAKES,SUM(MASTER_STAKES) MASTER_STAKES  FROM ( SELECT T2.PARENT AS AD, T1.PARENT AS SUPER_MASTER, T.*  FROM JETBET.JB_BET_DETAILS T, JETBET.JB_USER_DETAILS T1, JETBET.JB_USER_DETAILS T2 WHERE T.PARENT = T1.USER_ID AND  T1.PARENT = T2.USER_ID AND ADMIN_SETTLE='N' ) A GROUP BY USER_ID , PARENT HAVING PARENT = ? AND  SUM(NET_AMOUNT) > 0";

	public static final String GET_SETTLEMENT_FOR_MASTER_MINUS="SELECT USER_ID AS USER_ID , PARENT, SUM(STAKE) STAKES, SUM(LIABILITY) LIABILITY, SUM(PROFIT) PROFIT,SUM(LOSS) LOSS,SUM(NET_AMOUNT) AMOUNT,SUM(COMMISION) COMMISION, SUM(ADMIN_STAKES) ADMIN_STAKES ,SUM(SM_STAKES) SM_STAKES,SUM(MASTER_STAKES) MASTER_STAKES  FROM ( SELECT T2.PARENT AS AD, T1.PARENT AS SUPER_MASTER, T.*  FROM JETBET.JB_BET_DETAILS T, JETBET.JB_USER_DETAILS T1, JETBET.JB_USER_DETAILS T2 WHERE T.PARENT = T1.USER_ID AND  T1.PARENT = T2.USER_ID AND ADMIN_SETTLE='N' ) A GROUP BY USER_ID , PARENT HAVING PARENT = ? AND  SUM(NET_AMOUNT) < 0";

	public static final String GET_SETTLEMENT_FOR_SM_PLUS="SELECT PARENT AS USER_ID , SUPER_MASTER, SUM(STAKE) STAKES, SUM(LIABILITY) LIABILITY, SUM(PROFIT) PROFIT,SUM(LOSS) LOSS,SUM(NET_AMOUNT) AMOUNT,SUM(COMMISION) COMMISION, SUM(ADMIN_STAKES) ADMIN_STAKES ,SUM(SM_STAKES) SM_STAKES,SUM(MASTER_STAKES) MASTER_STAKES  FROM ( SELECT T2.PARENT AS AD, T1.PARENT AS SUPER_MASTER, T.*  FROM JETBET.JB_BET_DETAILS T, JETBET.JB_USER_DETAILS T1, JETBET.JB_USER_DETAILS T2 WHERE T.PARENT = T1.USER_ID AND  T1.PARENT = T2.USER_ID AND  MASTER_SETTLE='N' ) A GROUP BY PARENT,SUPER_MASTER HAVING SUPER_MASTER = ? AND SUM(NET_AMOUNT) > 0";

	public static final String GET_SETTLEMENT_FOR_SM_MINUS="SELECT PARENT AS USER_ID, SUPER_MASTER, SUM(STAKE) STAKES, SUM(LIABILITY) LIABILITY, SUM(PROFIT) PROFIT,SUM(LOSS) LOSS,SUM(NET_AMOUNT) AMOUNT,SUM(COMMISION) COMMISION, SUM(ADMIN_STAKES) ADMIN_STAKES ,SUM(SM_STAKES) SM_STAKES,SUM(MASTER_STAKES) MASTER_STAKES  FROM ( SELECT T2.PARENT AS AD, T1.PARENT AS SUPER_MASTER, T.*  FROM JETBET.JB_BET_DETAILS T, JETBET.JB_USER_DETAILS T1, JETBET.JB_USER_DETAILS T2 WHERE T.PARENT = T1.USER_ID AND  T1.PARENT = T2.USER_ID AND  MASTER_SETTLE='N' ) A GROUP BY PARENT,SUPER_MASTER HAVING SUPER_MASTER = ? AND SUM(NET_AMOUNT) < 0";

	public static final String GET_SETTLEMENT_FOR_ADMIN_PLUS="SELECT SUPER_MASTER  AS USER_ID, AD, SUM(STAKE) STAKES, SUM(LIABILITY) LIABILITY, SUM(PROFIT) PROFIT,SUM(LOSS) LOSS,SUM(NET_AMOUNT) AMOUNT,SUM(COMMISION) COMMISION, SUM(ADMIN_STAKES) ADMIN_STAKES ,SUM(SM_STAKES) SM_STAKES,SUM(MASTER_STAKES) MASTER_STAKES  FROM ( SELECT T2.PARENT AS AD, T1.PARENT AS SUPER_MASTER, T.*   FROM JETBET.JB_BET_DETAILS T, JETBET.JB_USER_DETAILS T1, JETBET.JB_USER_DETAILS T2 WHERE T.PARENT = T1.USER_ID AND  T1.PARENT = T2.USER_ID AND SM_SETTLE='N'  ) A GROUP BY SUPER_MASTER ,AD HAVING AD = ? AND SUM(NET_AMOUNT) > 0";

	public static final String GET_SETTLEMENT_FOR_ADMIN_MINUS="SELECT SUPER_MASTER   AS USER_ID, AD, SUM(STAKE) STAKES, SUM(LIABILITY) LIABILITY, SUM(PROFIT) PROFIT,SUM(LOSS) LOSS,SUM(NET_AMOUNT) AMOUNT,SUM(COMMISION) COMMISION, SUM(ADMIN_STAKES) ADMIN_STAKES ,SUM(SM_STAKES) SM_STAKES,SUM(MASTER_STAKES) MASTER_STAKES  FROM ( SELECT T2.PARENT AS AD, T1.PARENT AS SUPER_MASTER, T.*   FROM JETBET.JB_BET_DETAILS T, JETBET.JB_USER_DETAILS T1, JETBET.JB_USER_DETAILS T2 WHERE T.PARENT = T1.USER_ID AND  T1.PARENT = T2.USER_ID AND SM_SETTLE='N'  ) A GROUP BY SUPER_MASTER ,AD HAVING AD = ? AND SUM(NET_AMOUNT) < 0";

	public static final String GET_SETTLEMENT_PLUS="SELECT USER_ID, SUM(NET_AMOUNT) AMOUNT,SUM(MASTER_STAKES) MASTER_STAKES  ,SUM(SM_STAKES) SM_STAKES,SUM(ADMIN_STAKES) ADMIN_STAKES FROM JETBET.JB_BET_DETAILS WHERE PARENT=? GROUP BY USER_ID having SUM(NET_AMOUNT)>0";
	
	public static final String GET_SETTLEMENT_MINUS="SELECT USER_ID, SUM(NET_AMOUNT) AMOUNT,SUM(MASTER_STAKES) MASTER_STAKES  ,SUM(SM_STAKES) SM_STAKES,SUM(ADMIN_STAKES) ADMIN_STAKES FROM JETBET.JB_BET_DETAILS WHERE PARENT=? GROUP BY USER_ID having SUM(NET_AMOUNT)<0";
	
	public static final String GET_USER_LIST="WITH RECURSIVE TAB AS(	SELECT USER_ID, PARENT FROM JETBET.JB_USER_DETAILS WHERE USER_ID =  ?  UNION ALL  SELECT J.USER_ID, J.PARENT FROM JETBET.JB_USER_DETAILS J, TAB  WHERE J.PARENT = TAB.USER_ID ) SELECT USER_ID FROM JETBET.JB_USER_DETAILS J1 WHERE J1.USER_ID IN ( SELECT USER_ID FROM TAB) AND UPPER(USER_ROLE)= 'USER'";

	//public static final String UPDATE_AVAIL_BAL_AND_PROFIT_LOSS="UPDATE JETBET.JB_USER_DETAILS SET AVAIL_BALANCE=AVAIL_BALANCE+? , PROFIT_LOSS=PROFIT_LOSS+? , LASTUPDATEDDATE=CURRENT_TIMESTAMP, LASTUPDATEBY='TURTLE_BETS' WHERE USER_ID= ?";
	public static final String UPDATE_AVAIL_BAL_AND_PROFIT_LOSS="UPDATE JETBET.JB_USER_DETAILS SET AVAIL_BALANCE=(CASE WHEN AVAIL_BALANCE+?<0 THEN 0 ELSE AVAIL_BALANCE+? END) , PROFIT_LOSS=PROFIT_LOSS+? , LASTUPDATEDDATE=CURRENT_TIMESTAMP, LASTUPDATEBY='TURTLE_BETS' WHERE USER_ID= ? ";
	
	public static final String RESET_USER_TABLE_ON_SETTLEMENT="UPDATE JETBET.JB_USER_DETAILS SET AVAIL_BALANCE=AVAIL_LIMIT , PROFIT_LOSS=0 , LASTUPDATEDDATE=CURRENT_TIMESTAMP , LASTUPDATEBY=? WHERE USER_ID=?";
	
	public static final String RESET_BET_TABLE_ON_SETTLEMENT="UPDATE JETBET.JB_BET_DETAILS SET BET_SETTLEMENT='SETTLED' , LAST_UPDATED_DATE=CURRENT_TIMESTAMP , LAST_UPDATED_BY=?, REMARKS=? WHERE USER_ID=?";

	public static final String INSERT_PARENT_PROFIT_LOSS="UPDATE JETBET.JB_BET_DETAILS SET BET_SETTLEMENT='SETTLED' , LAST_UPDATED_DATE=CURRENT_TIMESTAMP , LAST_UPDATED_BY=?, REMARKS=? WHERE USER_ID=?";

	public static final String UPDATE_PARENT_PROFIT_LOSS="UPDATE JETBET.JB_BET_DETAILS SET NET_AMOUNT=NET_AMOUNT+?, MASTER_STAKES= MASTER_STAKES+?, SM_STAKES=SM_STAKES+?, ADMIN_STAKES=ADMIN_STAKES+? , LAST_UPDATED_BY='TURTLE_BETS', LAST_UPDATED_DATE=CURRENT_TIMESTAMP WHERE USER_ID= ? AND BET_SETTLEMENT='PENDING' LIMIT 1";

	public static final String MATCH_DASHBOARD_FOR_MASTER="WITH TAB AS ( SELECT PARENT, MATCH_ID,RUNNER_NAME,SUM(MASTER_PL) MASTER_STAKES FROM ( SELECT T2.PARENT AS AD, T1.PARENT AS SUPER_MASTER, T.*  FROM JETBET.JB_BET_DETAILS T, JETBET.JB_USER_DETAILS T1, JETBET.JB_USER_DETAILS T2 WHERE T.PARENT = T1.USER_ID AND  T1.PARENT = T2.USER_ID AND T.MARKET_TYPE='MATCH_ODDS' ) A GROUP BY PARENT,MATCH_ID,RUNNER_NAME HAVING PARENT = ? ) SELECT DISTINCT  A.MATCH_ID,D.MATCH_NAME, D.SPORTS_ID,D.MATCH_OPEN_DATE, B.TEAMA_NAME, B.TEAMB_NAME, B.TEAMC_NAME,COALESCE(C.MASTER_STAKES,0)  AS TEAMA_STAKE ,  COALESCE(C1.MASTER_STAKES,0) AS TEAMB_STAKE,COALESCE(C2.MASTER_STAKES,0) AS TEAMC_STAKE  FROM  JETBET.JB_MATCH_DETAILS D JOIN  JETBET.JB_FANCY_DETAILS A  ON A.MATCH_ID = D.MATCH_ID    JOIN  JETBET.JB_RUNNERS_DETAILS B  ON A.MATCH_ID = B.MATCH_ID LEFT JOIN  TAB C ON B.MATCH_ID = C.MATCH_ID AND B.TEAMA_NAME = C.RUNNER_NAME  LEFT JOIN  TAB C1 ON B.MATCH_ID = C1.MATCH_ID AND B.TEAMB_NAME = C1.RUNNER_NAME  LEFT JOIN  TAB C2 ON B.MATCH_ID = C2.MATCH_ID AND B.TEAMC_NAME = C2.RUNNER_NAME  WHERE A.MARKET_TYPE='MATCH_ODDS'";

	public static final String MATCH_DASHBOARD_FOR_SM="WITH TAB AS ( SELECT SUPER_MASTER, MATCH_ID,RUNNER_NAME,SUM(SM_PL) SM_STAKES FROM ( SELECT T2.PARENT AS AD, T1.PARENT AS SUPER_MASTER, T.* 	FROM JETBET.JB_BET_DETAILS T, JETBET.JB_USER_DETAILS T1, JETBET.JB_USER_DETAILS T2 WHERE T.PARENT = T1.USER_ID AND  T1.PARENT = T2.USER_ID AND T.MARKET_TYPE='MATCH_ODDS' ) A GROUP BY  SUPER_MASTER,MATCH_ID,RUNNER_NAME HAVING SUPER_MASTER = ? ) SELECT DISTINCT  A.MATCH_ID,D.MATCH_NAME, D.SPORTS_ID,D.MATCH_OPEN_DATE, B.TEAMA_NAME, B.TEAMB_NAME, B.TEAMC_NAME,COALESCE(C.SM_STAKES,0)  AS TEAMA_STAKE ,  COALESCE(C1.SM_STAKES,0) AS TEAMB_STAKE,COALESCE(C2.SM_STAKES,0) AS TEAMC_STAKE  FROM  JETBET.JB_MATCH_DETAILS D JOIN  JETBET.JB_FANCY_DETAILS A  ON A.MATCH_ID = D.MATCH_ID JOIN  JETBET.JB_RUNNERS_DETAILS B  ON A.MATCH_ID = B.MATCH_ID LEFT JOIN  TAB C ON B.MATCH_ID = C.MATCH_ID AND B.TEAMA_NAME = C.RUNNER_NAME  LEFT JOIN  TAB C1 ON B.MATCH_ID = C1.MATCH_ID AND B.TEAMB_NAME = C1.RUNNER_NAME  LEFT JOIN  TAB C2 ON B.MATCH_ID = C2.MATCH_ID AND B.TEAMC_NAME = C2.RUNNER_NAME  WHERE A.MARKET_TYPE='MATCH_ODDS'";

	public static final String MATCH_DASHBOARD_FOR_ADMIN="WITH TAB AS ( SELECT AD, MATCH_ID,RUNNER_NAME,SUM(ADMIN_PL) ADM_STAKES FROM ( SELECT T2.PARENT AS AD, T1.PARENT AS SUPER_MASTER, T.*  	FROM JETBET.JB_BET_DETAILS T, JETBET.JB_USER_DETAILS T1, JETBET.JB_USER_DETAILS T2 WHERE T.PARENT = T1.USER_ID AND  T1.PARENT = T2.USER_ID AND T.MARKET_TYPE='MATCH_ODDS' ) A GROUP BY AD,MATCH_ID,RUNNER_NAME HAVING AD = ? ) SELECT DISTINCT  A.MATCH_ID,D.MATCH_NAME, D.SPORTS_ID,D.MATCH_OPEN_DATE, B.TEAMA_NAME, B.TEAMB_NAME, B.TEAMC_NAME,COALESCE(C.ADM_STAKES,0)  AS TEAMA_STAKE ,  COALESCE(C1.ADM_STAKES,0) AS TEAMB_STAKE,COALESCE(C2.ADM_STAKES,0) AS TEAMC_STAKE  FROM   JETBET.JB_MATCH_DETAILS D JOIN  JETBET.JB_FANCY_DETAILS A  ON A.MATCH_ID = D.MATCH_ID  JOIN  JETBET.JB_RUNNERS_DETAILS B  ON A.MATCH_ID = B.MATCH_ID LEFT JOIN  TAB C ON B.MATCH_ID = C.MATCH_ID AND B.TEAMA_NAME = C.RUNNER_NAME  LEFT JOIN  TAB C1 ON B.MATCH_ID = C1.MATCH_ID AND B.TEAMB_NAME = C1.RUNNER_NAME  LEFT JOIN  TAB C2 ON B.MATCH_ID = C2.MATCH_ID AND B.TEAMC_NAME = C2.RUNNER_NAME  WHERE A.MARKET_TYPE='MATCH_ODDS'";

	public static final String GET_FANCY_LIST_BY_MATCH_AND_SPORTS="SELECT FANCY.SPORTS_ID,FANCY.SERIES_ID,FANCY.MATCH_ID,SPORTS.SPORTS_NAME, SERIES.SERIES_NAME,FANCY.MATCH_NAME,FANCY.MARKET_TYPE,FANCY.MARKET_COUNT FROM JETBET.JB_FANCY_DETAILS FANCY,  JETBET.JB_SERIES_DETAILS SERIES, JETBET.JB_SPORTS_DETAILS SPORTS WHERE FANCY.SERIES_ID=SERIES.SERIES_ID AND SERIES.SPORTS_ID=SPORTS.SPORTS_ID AND  FANCY.SPORTS_ID=? AND FANCY.MATCH_ID= ? AND FANCY.IS_ACTIVE='Y' AND FANCY.MARKET_TYPE='MATCH_ODDS' UNION  SELECT  FANCY.SPORTS_ID,FANCY.SERIES_ID,FANCY.MATCH_ID,SPORTS.SPORTS_NAME, SERIES.SERIES_NAME,FANCY.MATCH_NAME,FANCY.MARKET_TYPE,FANCY.MARKET_COUNT FROM JETBET.JB_FANCY_DETAILS FANCY,  JETBET.JB_SERIES_DETAILS SERIES, JETBET.JB_SPORTS_DETAILS SPORTS WHERE 	 FANCY.SERIES_ID=SERIES.SERIES_ID AND SERIES.SPORTS_ID=SPORTS.SPORTS_ID AND  FANCY.SPORTS_ID=? AND FANCY.MATCH_ID=? AND FANCY.IS_ACTIVE='Y' AND FANCY.MARKET_TYPE<>'MATCH_ODDS'";


	public static final String CURRENT_ODDS_POSITION_FOR_MASTER="WITH TAB AS ( SELECT  USER_ID, PARENT, MATCH_ID,RUNNER_NAME,SUM(master_pl) MASTER_STAKES ,MARKET_TYPE FROM ( SELECT T2.PARENT AS AD, T1.PARENT AS SUPER_MASTER, T.*  FROM JETBET.JB_BET_DETAILS T, JETBET.JB_USER_DETAILS T1, JETBET.JB_USER_DETAILS T2 WHERE T.PARENT = T1.USER_ID AND  T1.PARENT = T2.USER_ID AND T.MARKET_TYPE='MATCH_ODDS' )  A GROUP BY USER_ID, PARENT,MATCH_ID,RUNNER_NAME,MARKET_TYPE HAVING PARENT = ? )  SELECT DISTINCT COALESCE(c.USER_ID,c1.user_id, c2.user_id) AS USER_ID, A.MATCH_ID,D.MATCH_NAME, D.SPORTS_ID,D.MATCH_OPEN_DATE, B.TEAMA_NAME, B.TEAMB_NAME, B.TEAMC_NAME,COALESCE(C.MASTER_STAKES,0)  AS  TEAMA_STAKE ,  COALESCE(C1.MASTER_STAKES,0) AS TEAMB_STAKE,COALESCE(C2.MASTER_STAKES,0) AS TEAMC_STAKE  FROM  JETBET.JB_MATCH_DETAILS D JOIN  JETBET.JB_FANCY_DETAILS A  ON A.MATCH_ID = D.MATCH_ID JOIN  JETBET.JB_RUNNERS_DETAILS B  ON A.MATCH_ID = B.MATCH_ID LEFT JOIN  TAB C ON B.MATCH_ID = C.MATCH_ID AND B.TEAMA_NAME = C.RUNNER_NAME  LEFT JOIN  TAB C1 ON B.MATCH_ID = C1.MATCH_ID AND B.TEAMB_NAME = C1.RUNNER_NAME  LEFT JOIN  TAB C2 ON B.MATCH_ID = C2.MATCH_ID AND B.TEAMC_NAME = C2.RUNNER_NAME  WHERE A.MARKET_TYPE='MATCH_ODDS' and c.MARKET_TYPE='MATCH_ODDS' and c1.MARKET_TYPE='MATCH_ODDS' and c2.MARKET_TYPE='MATCH_ODDS' and  a.match_id = ?";

	public static final String CURRENT_ODDS_POSITION_FOR_SM=" WITH TAB AS ( SELECT   PARENT, SUPER_MASTER, MATCH_ID,RUNNER_NAME,SUM(sm_pl) MASTER_STAKES ,MARKET_TYPE FROM ( SELECT T2.PARENT AS AD, T1.PARENT AS SUPER_MASTER, T.*  FROM JETBET.JB_BET_DETAILS T, JETBET.JB_USER_DETAILS T1, JETBET.JB_USER_DETAILS T2 WHERE T.PARENT = T1.USER_ID AND  T1.PARENT = T2.USER_ID AND T.MARKET_TYPE='MATCH_ODDS' )  A GROUP BY PARENT, SUPER_MASTER,MATCH_ID,RUNNER_NAME,MARKET_TYPE HAVING SUPER_MASTER = ? )  SELECT DISTINCT COALESCE(c.PARENT,c1.PARENT, c2.PARENT) AS USER_ID, A.MATCH_ID,D.MATCH_NAME, D.SPORTS_ID,D.MATCH_OPEN_DATE, B.TEAMA_NAME, B.TEAMB_NAME, B.TEAMC_NAME,COALESCE(C.MASTER_STAKES,0)  AS  TEAMA_STAKE ,  COALESCE(C1.MASTER_STAKES,0) AS TEAMB_STAKE,COALESCE(C2.MASTER_STAKES,0) AS TEAMC_STAKE   FROM  JETBET.JB_MATCH_DETAILS D JOIN  JETBET.JB_FANCY_DETAILS A  ON A.MATCH_ID = D.MATCH_ID    JOIN  JETBET.JB_RUNNERS_DETAILS B  ON A.MATCH_ID = B.MATCH_ID LEFT JOIN  TAB C ON B.MATCH_ID = C.MATCH_ID AND B.TEAMA_NAME = C.RUNNER_NAME  LEFT JOIN  TAB C1 ON B.MATCH_ID = C1.MATCH_ID AND B.TEAMB_NAME = C1.RUNNER_NAME  LEFT JOIN  TAB C2 ON B.MATCH_ID = C2.MATCH_ID AND B.TEAMC_NAME = C2.RUNNER_NAME  WHERE A.MARKET_TYPE='MATCH_ODDS' and c.MARKET_TYPE='MATCH_ODDS' and c1.MARKET_TYPE='MATCH_ODDS' and c2.MARKET_TYPE='MATCH_ODDS' and  a.match_id = ? ";

	public static final String CURRENT_ODDS_POSITION_FOR_ADMIN="WITH TAB AS ( SELECT   SUPER_MASTER, AD, MATCH_ID,RUNNER_NAME,SUM(admin_pl) MASTER_STAKES ,MARKET_TYPE FROM ( SELECT T2.PARENT AS AD, T1.PARENT AS SUPER_MASTER, T.*  FROM JETBET.JB_BET_DETAILS T, JETBET.JB_USER_DETAILS T1, JETBET.JB_USER_DETAILS T2 WHERE T.PARENT = T1.USER_ID AND  T1.PARENT = T2.USER_ID AND T.MARKET_TYPE='MATCH_ODDS' )   A GROUP BY SUPER_MASTER, AD,MATCH_ID,RUNNER_NAME,MARKET_TYPE HAVING AD = ? )  SELECT DISTINCT COALESCE(c.SUPER_MASTER,c1.SUPER_MASTER, c2.SUPER_MASTER) AS USER_ID, A.MATCH_ID,D.MATCH_NAME, D.SPORTS_ID,D.MATCH_OPEN_DATE, B.TEAMA_NAME, B.TEAMB_NAME, B.TEAMC_NAME,COALESCE(C.MASTER_STAKES,0)  AS  TEAMA_STAKE ,  COALESCE(C1.MASTER_STAKES,0) AS TEAMB_STAKE,COALESCE(C2.MASTER_STAKES,0) AS TEAMC_STAKE   FROM  JETBET.JB_MATCH_DETAILS D JOIN  JETBET.JB_FANCY_DETAILS A  ON A.MATCH_ID = D.MATCH_ID   JOIN  JETBET.JB_RUNNERS_DETAILS B  ON A.MATCH_ID = B.MATCH_ID LEFT JOIN  TAB C ON B.MATCH_ID = C.MATCH_ID AND B.TEAMA_NAME = C.RUNNER_NAME  LEFT JOIN  TAB  C1 ON B.MATCH_ID = C1.MATCH_ID AND B.TEAMB_NAME = C1.RUNNER_NAME  LEFT JOIN  TAB C2 ON B.MATCH_ID = C2.MATCH_ID AND B.TEAMC_NAME = C2.RUNNER_NAME  WHERE A.MARKET_TYPE='MATCH_ODDS' and c.MARKET_TYPE='MATCH_ODDS' and c1.MARKET_TYPE='MATCH_ODDS' and c2.MARKET_TYPE='MATCH_ODDS' and  a.match_id = ?";

	
}
