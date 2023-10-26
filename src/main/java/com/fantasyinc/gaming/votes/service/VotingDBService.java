package com.fantasyinc.gaming.votes.service;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.amazonaws.services.lambda.runtime.events.SQSBatchResponse;
import com.fantasyinc.gaming.votes.model.VotingRecord;
import com.fantasyinc.gaming.votes.utils.Constants;

import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.DynamoDbException;

public class VotingDBService {

	private static final Logger logger = LoggerFactory.getLogger(VotingDBService.class);

	private static DynamoDbClient dynamoDbClient;
	private static DynamoDbEnhancedClient enhancedClient;

	private static String tableName = System.getenv(Constants.TABLE_NAME_LAMBDA_ENV);
	private static String awsRegion = System.getenv(Constants.REGION_LAMBDA_ENV);

	private static final TableSchema<VotingRecord> userVotingSchema = TableSchema.fromBean(VotingRecord.class);

	private static void initialize() {
		if (StringUtils.isBlank(tableName)) {
			tableName = Constants.TABLE_NAME_DEFAULT;
		}
		if (StringUtils.isBlank(tableName)) {
			awsRegion = Constants.REGION_DEFAULT.toString();
		}
		if (dynamoDbClient == null) {
			dynamoDbClient = DynamoDbClient.builder().region(Region.of(awsRegion)).build();
		}

		if (enhancedClient == null) {
			enhancedClient = DynamoDbEnhancedClient.builder().dynamoDbClient(dynamoDbClient).build();
		}
	}

	public static List<String> processVotingRecords(List<VotingRecord> voteRecs, final List<SQSBatchResponse.BatchItemFailure> errorMessages) {
		initialize();
		final List<String> votingRecordList = Optional.ofNullable(voteRecs)
				.orElseGet(Collections::emptyList).stream().filter(Objects::nonNull)
				.map(msg -> createOrUpdateVotingRecord(msg, errorMessages)).collect(Collectors.toList());
		
		return CollectionUtils.isEmpty(votingRecordList) ? null : votingRecordList;
	}
	
	private static String createOrUpdateVotingRecord(VotingRecord voteRecord, final List<SQSBatchResponse.BatchItemFailure> errorMessages) {
		DynamoDbTable<VotingRecord> activityTrackerTable = enhancedClient.table(tableName, userVotingSchema);
		try {
			logger.info("Put the Voting record in the table : {}", voteRecord);
			activityTrackerTable.putItem(voteRecord);
			return voteRecord.getMessageId() + "-" + Constants.SUCCESS;
		} catch (DynamoDbException ddbex) {
			logger.error("Error in storing the vote record {}", voteRecord.getMessageId());
			errorMessages.add(new SQSBatchResponse.BatchItemFailure(voteRecord.getMessageId()));
		}
		return voteRecord.getMessageId() + "-" + Constants.FAILURE;
	}
}
