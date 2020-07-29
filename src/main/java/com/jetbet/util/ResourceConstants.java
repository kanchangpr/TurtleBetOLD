package com.jetbet.util;

import org.apache.commons.lang3.StringUtils;

public class ResourceConstants {
	public static final String USER_DETAILS="UserDetails";
	
	public static final String USER_ROLES="UserRoles/{role}";
	
	public static final String PARENT_LIST="ParentListByRole/{role}";
	
	public static final String USER_NAME_AVAILABILITY="UserNameAvailability/{userName}";
	
	public static final String USER_CONTROLS="UserControls";
	
	public static final String LIST_OF_SPORTSS="ListOfSports";
	
	public static final String LIST_OF_SERIES="ListOfSeries";
	
	public static final String LIST_OF_MATHCES="ListOfMatches";
	
	public static final String LIST_OF_ODDS="ListOfOdds";
	
	public static final String GET_SESSION_TOKEN="GetSessionToken";
	
	public static final String CHIPS_ALLOCATIONS="chipsAllocations";
	public static final String CHIPS_BALANCE="chipsBalance/{userId}";
	public static final String CHANGE_PASSWORD="changePassword";
	
	public static final String SUPERMASTER="superMaster";
	public static final String MASTER="master";
	public static final String USER="user";
	
	public static final String SUCCESS="Success";
	public static final String FAILED="Failed";
	public static final String EXCEPTION="Exception";
	
	public static final String ERR_001="ERR_01";
	public static final String EXIST="Data Already Exist";
	
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
	
	public static final String ERR_EXCEPTION="ERR_EXEPCTION";
	public static final String INSERTED="Data Inserted Successfully";
	public static final String UPDATED="Data Updated Successfully";
	public static final String PASSWORD_UPDATED="Password Updated Successfully";
	
	public static final String LOCK_USER="LOCKUSER";
	public static final String UNLOCK_USER="UNLOCKUSER";
	public static final String LOCK_BETTING="LOCKBETTING";
	public static final String UNLOCK_BETTING="UNLOCKBETTING";
	public static final String CLOSE_ACC="CLOSEACC";
	public static final String OPEN_ACC="OPENACC";
	
	public static final String DEPOSIT="DEPOSIT";
	public static final String WITHDRAW="WITHDRAW";
	
	
	
	public static String checkNulString(String aString) {
		return StringUtils.isBlank(aString) ? null : aString;
	}
	
}
