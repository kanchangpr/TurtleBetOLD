package com.jetbet.util;

public class QueryListConstant {
	public static final String UPDATE_FROM_USER_ACC_CHIPS_SQL="UPDATE JETBET.JB_USER_DETAILS SET CHIPS= ?, LASTUPDATEDDATE=CURRENT_TIMESTAMP, LASTUPDATEBY=? WHERE USER_ID= ?";

	public static final String UPDATE_TO_USER_ACC_CHIPS_SQL="UPDATE JETBET.JB_USER_DETAILS SET CHIPS= ?, LASTUPDATEDDATE=CURRENT_TIMESTAMP, LASTUPDATEBY=? WHERE USER_ID= ?";

	public static final String GET_PARENT_LIST_SQL="SELECT USER_ID FROM JETBET.JB_USER_DETAILS WHERE USER_ROLE = (SELECT USER_ROLE FROM JETBET.JB_USER_ROLES "
			+ "WHERE USER_LEVEL< (SELECT USER_LEVEL FROM JETBET.JB_USER_ROLES WHERE UPPER(USER_ROLE)= ? ) ORDER BY ID DESC LIMIT 1) ";
	
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
	
	public static final String FANCY_LIST="SELECT MATCH.MATCH_NAME AS MATCH_NAME, FANCY.* FROM JETBET.JB_MATCH_DETAILS MATCH , JETBET.JB_FANCY_DETAILS FANCY WHERE  FANCY.MATCH_ID = MATCH.MATCH_ID ORDER BY MATCH_NAME";
}
