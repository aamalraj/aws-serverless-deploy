package com.fantasyinc.gaming.votes.model;

import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbIgnore;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbIgnoreNulls;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbSortKey;

@DynamoDbBean
public class VotingRecord {

	private String votingUniqueId;
	private String userId;
	private String voteMessage;
	private String messageId;
	
	@DynamoDbPartitionKey
	public String getVotingUniqueId() {
		return votingUniqueId;
	}

	public void setVotingUniqueId(String votingUniqueId) {
		this.votingUniqueId = votingUniqueId;
	}
	
	@DynamoDbSortKey
	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	@DynamoDbIgnoreNulls
	public String getVoteMessage() {
		return voteMessage;
	}

	public void setVoteMessage(String voteMessage) {
		this.voteMessage = voteMessage;
	}

	@DynamoDbIgnore
	public String getMessageId() {
		return messageId;
	}

	public void setMessageId(String messageId) {
		this.messageId = messageId;
	}

	@Override
	public String toString() {
		return "VotingRecord [votingUniqueId=" + votingUniqueId + ", userId=" + userId + ", voteMessage=" + voteMessage
				+ ", messageId=" + messageId + "]";
	}

}
