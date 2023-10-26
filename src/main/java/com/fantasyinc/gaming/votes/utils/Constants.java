package com.fantasyinc.gaming.votes.utils;

import software.amazon.awssdk.regions.Region;

public class Constants {

	private Constants() {
	}

	public static final String AWS_REQUEST_ID = "AWSRequestId";
	public static final String SUCCESS = "Success";
	public static final int ERROR_CODE_500 = 500;
	public static final String ERROR_WHILE_INSERTING_VOTING_RECORD = "Error Occured while adding/updating Voting Record";
	public static final Region REGION_DEFAULT = Region.US_EAST_1;
	public static final String REGION_LAMBDA_ENV = "awsRegion";
	public static final String TABLE_NAME_DEFAULT = "UserVotingRecordsTable";
	public static final String TABLE_NAME_LAMBDA_ENV = "tableName";
	public static final String FAILURE = "Failure";
}