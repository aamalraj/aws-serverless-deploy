package com.fantasyinc.gaming.votes.utils;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.amazonaws.services.lambda.runtime.events.SQSBatchResponse;
import com.amazonaws.services.lambda.runtime.events.SQSEvent;
import com.amazonaws.services.lambda.runtime.events.SQSEvent.SQSMessage;
import com.fantasyinc.gaming.votes.model.VotingRecord;

public class MessageParserUtils {

	private static final Logger logger = LoggerFactory.getLogger(MessageParserUtils.class);

	private MessageParserUtils() {
	}

	public static List<VotingRecord> processMessage(final SQSEvent event, final List<SQSBatchResponse.BatchItemFailure> errorMessages) {

		final VotingRecord data = new VotingRecord();

		try {
			final List<VotingRecord> votingRecordList = Optional.ofNullable(event.getRecords())
					.orElseGet(Collections::emptyList).stream().filter(Objects::nonNull)
					.filter(msg -> StringUtils.isNotBlank(msg.getBody()))
					.map(msg -> parseInputMessage(data, msg, errorMessages)).collect(Collectors.toList());

			return CollectionUtils.isEmpty(votingRecordList) ? null : votingRecordList;
		} catch (final Exception ex) {
			logger.error("Error in SQS message processing >>>", ex);
		}

		return Collections.emptyList();
	}

	private static VotingRecord parseInputMessage(final VotingRecord data, final SQSMessage sqsMsg,
			final List<SQSBatchResponse.BatchItemFailure> errorMessages) {

		try {
			final JSONObject jsonMsg = new JSONObject(sqsMsg.getBody());
			logger.debug("Input is a SQSEVENT message with body>> {}", sqsMsg.getBody());
			data.setUserId(jsonMsg.getString("userId"));
			data.setVotingUniqueId(jsonMsg.getString("votingUniqueId"));
			data.setVoteMessage(jsonMsg.getJSONObject("votingMessage").toString());
			data.setMessageId(sqsMsg.getMessageId());
		} catch (JSONException jsonEx) {
			logger.error("Error Parsing the voting record for messageId {} - ", sqsMsg.getMessageId(), jsonEx);
			errorMessages.add(new SQSBatchResponse.BatchItemFailure(sqsMsg.getMessageId()));
		}
		return data;
	}
	
//	public static void main(String[] args) {
//		VotingRecord data = new VotingRecord();
//		SQSMessage sqsMsg = new SQSMessage();
//		List<SQSBatchResponse.BatchItemFailure> errorMessages = new ArrayList<>();
//		sqsMsg.setBody("{\"userId\": \"amalarul\",\"votingUniqueId\": \"e56ed03d-1652-487f-bc2d-57d3af2ff5e5\",\"votingMessage\": {\"myTeam\": {\"QB\": \"Malik Cunningham\",\"RB\": [\"Chuba Hubbard\",\"D'Onta Foreman\"],\"WR\": [\"Kendrick Bourne\",\"Brandon Powell\"],\"TE\": \"Logan Thomas\",\"KR\": \"Zach Ertz\",\"DST\": \"M. Alie-Cox\",\"FLEX\": \"Taysom Hill\"}}}");
//		sqsMsg.setMessageId("19dd0b57-b21e-4ac1-bd88-01bbb068cb78");
//		MessageParserUtils.parseInputMessage(data, sqsMsg, errorMessages);
//		System.out.println(data.getUserId());
//		System.out.println(data.getVotingUniqueId());
//		System.out.println(data.getVoteMessage());
//	}
}
