package com.jetbet.util;

import org.apache.commons.lang3.StringUtils;

public class ResourceConstants {
	
	public static final String USER_NAME="TURTLE_BETS";
	
	public static final String USER_DETAILS="UserDetails";
	
	public static final String USER_ROLES="UserRoles";
	
	public static final String PARENT_LIST="ParentListByRole";
	
	public static final String USER_NAME_AVAILABILITY="UserNameAvailability";
	
	public static final String USER_CONTROLS="UserControls";
	
	public static final String LIST_OF_SPORTS="ListOfSports";
	
	public static final String LIST_OF_SERIES="ListOfSeries";
	
	public static final String LIST_OF_MATHCES="ListOfMatches";
	
	public static final String LIST_OF_FANCY="ListOfFancy";
	
	public static final String PARTNERSHIP="Partnership";
	
	public static final String PARTNERSHIP_PERCENTAGE="PartnershipPercentage";
	
	public static final String GET_SESSION_TOKEN="GetSessionToken";
	
	public static final String SPORTS_CONTROL="SportsControl";
	
	public static final String CHIPS_ALLOCATIONS="chipsAllocations";
	public static final String CHIPS_BALANCE="chipsBalance";
	public static final String CHIPS_HISTORY="chipsHistory";
	public static final String RESET_PASSWORD="resetPassword";
	public static final String CHANGE_PASSWORD="changePassword";
	public static final String MARKET_CATALOGUE="marketCatalogue";
	public static final String MATCH_ODDS="matchOdds";
	public static final String MATCH_ODDS_AND_FANCY="matchOddsAndFancy";
	public static final String PLACE_BETS="placeBets";
	public static final String STAKES_DETAILS="stakesDetails";
	public static final String LIABILITY="calcLiability";
	public static final String USER_REPORT="userReport";
	public static final String USER_DASHBOARD="userDashboard";
	public static final String USER_DASHBOARD_MATCH_LIST="userDashboardMatchList";
	public static final String RUNNERS_PRICE_SIZE="runnersPriceAndSize";
	public static final String USER_HOME="userHome";
	public static final String DECLARE_RESULT="declareResult";
	public static final String OPEN_PLACE_BETS="openPlaceBets";
	public static final String OPEN_PLACE_BETS_COUNT="openPlaceBetsCount";
	public static final String OPEN_PLACE_BETS_BY_SPORTS="openPlaceBetsBySports";
	public static final String CALCULATE_SETTLEMENT="calculateSettlement";
	public static final String BET_SETTLEMENT="betSettlement";
	public static final String SETTLEMENT="Settlement";
	public static final String MATCH_DASHBOARD="matchDashboard";
	public static final String CURRENT_ODDS_POSITION="currentOddsPosition";
	public static final String FANCY_LIST="fancyList";
	public static final String FANCY_POTISION="fancyPosition";
	public static final String USER_PROFIT_LOSS="userPL";
	
	
	public static final String ADMIN="admin";
	public static final String SUPERMASTER="superMaster";
	public static final String MASTER="master";
	public static final String USER="user";
	
	public static final String SUCCESS="Success";
	public static final String FAILED="Failed";
	public static final String EXCEPTION="Exception";
	
	public static final String ERR_001="ERR_01";
	public static final String EXIST="Data Already Exist";
	public static final String USER_EXIST="User Already Exist";
	
	public static final String ERR_002="ERR_02";
	public static final String INSERTION_FAILED="Data Insertion Failed";
	
	public static final String ERR_003="ERR_03";
	public static final String UPDATION_FAILED="Data Updation Failed";
	
	public static final String ERR_004="ERR_04";
	public static final String INSUFFICIENT_AMOUNT="insufficient chips in Account";
	
	public static final String ERR_005="ERR_05";
	public static final String CHIPS_TRANSFER_FAILED="Chips transfer failed";
	
	public static final String ERR_006="ERR_06";
	public static final String PASSWORD_UPDATE_FAILED="Password Updation failed";
	
	public static final String ERR_007="ERR_07";
	public static final String PASSWORD_NOT_MATCH="Password Does not Match";
	
	public static final String ERR_008="ERR_08";
	public static final String USER_LIMIT_EXCEED="User Limit Exceeded!!";
	
	public static final String ERR_009="ERR_09";
	public static final String ACTION_NOT_ALLOWED="This Action Not Allowed!!";
	
	public static final String ERR_010="ERR_10";
	public static final String USER_NOT_EXIST="User Does not Exist!!";
	
	public static final String ERR_011="ERR_11";
	public static final String PARTNERSHIP_INVALID="Partnership between all stakeholders is greater than 100!!";
	public static final String S_B_COMMISION_INVALID="Odd/Session Commission should not be greater than parent's Odd/Session Commision";
	
	public static final String ERR_012="ERR_12";
	public static final String OLD_PASSWORD_WRONG="Current Password is Wrong!!";
	
	public static final String ERR_013="ERR_13";
	public static final String USER_BETTING_LOCK="Betting locked for User!!";
	
	public static final String ERR_014="ERR_14";
	public static final String MINIMUM_STAKE_ERROR="Minimum Stake should be ";
	
	public static final String ERR_015="ERR_15";
	public static final String ODDS_MISMATCHED="Odds Mismatched!!!";
	
	public static final String ERR_016="ERR_16";
	public static final String CHIPS_GREATER_THAN_ZERO="Chips should be greater than 0";
	
	public static final String BET_PLACED="Bet Placed Successfully!! Bet ID: ";
	public static final String USER_UPDATED="User Detail updated Successfully!! ";
	
	public static final String ERR_EXCEPTION="ERR_EXEPCTION";
	public static final String INSERTED="Data Inserted Successfully";
	public static final String USER_INSERTED="User Added Successfully";
	public static final String UPDATED="Data Updated Successfully";
	public static final String PASSWORD_UPDATED="Password Updated Successfully";
	public static final String SETTLEMENT_SUCCESS="Settlement done!!";
	public static final String SETTLEMENT_FAILED="Settlement failed!!";
	
	public static final String LOCK_USER="LOCKUSER";
	public static final String UNLOCK_USER="UNLOCKUSER";
	public static final String LOCK_BETTING="LOCKBETTING";
	public static final String UNLOCK_BETTING="UNLOCKBETTING";
	public static final String CLOSE_ACC="CLOSEACC";
	public static final String OPEN_ACC="OPENACC";
	
	public static final String DEPOSIT="DEPOSIT";
	public static final String WITHDRAW="WITHDRAW";
	
	public class BetType{
		public static final String BET_HISTORY="BetHisotry";
		public static final String ACCOUNT_HISTORY="AccountHisotry";
		public static final String LOGIN_HISTORY="LoginHisotry";
		public static final String PROFIT_AND_LOSS_HISTORY="PLHisotry";
	}
	
	public static final String SPORTS_PAGE="SPORTS_PAGE";
	public static final String SERIES_PAGE="SERIES_PAGE";
	public static final String MATCH_PAGE="MATCH_PAGE";
	public static final String FANCY_PAGE="FANCY_PAGE";
	
	public static final String SPORTS_TABLE="JETBET.JB_SPORTS_DETAILS";
	public static final String SERIES_TABLE="JETBET.JB_SERIES_DETAILS";
	public static final String MATCH_TABLE="JETBET.JB_MATCH_DETAILS";
	public static final String FANCY_TABLE="JETBET.JB_FANCY_DETAILS";
	
	public static final String SPORTS_ID="SPORTS_ID";
	public static final String SERIES_ID="SERIES_ID";
	public static final String MATCH_ID="MATCH_ID";
	public static final String MARKET_ID="MATCH_ID";
	
	public static final String SPORTS_UPDATED_BY="SPORTS_UPDATED_BY";
	public static final String SERIES_UPDATED_BY="SERIES_UPDATED_BY";
	public static final String MATCH_UPDATED_BY="MATCH_UPDATED_BY";
	
	public static final String SPORTS_UPDATED_DATE  ="SPORTS_UPDATED_DATE  ";
	public static final String SERIES_UPDATED_DATE  ="SERIES_UPDATED_DATE  ";
	public static final String MATCH_UPDATED_DATE ="MATCH_UPDATED_DATE ";
	
	public static final String WON  ="WON";
	public static final String LOST  ="LOST";
	
	public static final String BACK  ="BACK";
	public static final String LAY  ="LAY";
	
	public static final String IN_PLAY  ="In Play";
	public static final String NOT_IN_PLAY  ="NOT_IN_PLAY";
	
	public static String checkNulString(String aString) {
		return StringUtils.isBlank(aString) ? "" : aString;
	}
	
	public class Stakes{
		public static final double STAKE1=1000;
		public static final double STAKE2=2000;
		public static final double STAKE3=3000;
		public static final double STAKE4=4000;
		public static final double STAKE5=5000;
	}
	
	
}
