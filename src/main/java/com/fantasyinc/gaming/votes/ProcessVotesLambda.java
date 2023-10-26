package com.fantasyinc.gaming.votes;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.SQSBatchResponse;
import com.amazonaws.services.lambda.runtime.events.SQSEvent;
import com.fantasyinc.gaming.votes.model.VotingRecord;
import com.fantasyinc.gaming.votes.service.VotingDBService;
import com.fantasyinc.gaming.votes.utils.Constants;
import com.fantasyinc.gaming.votes.utils.MessageParserUtils;

public class ProcessVotesLambda implements RequestHandler<SQSEvent, SQSBatchResponse> {

	private static final Logger logger = LoggerFactory.getLogger(ProcessVotesLambda.class);

	@Override
	public SQSBatchResponse handleRequest(SQSEvent inputEvent, Context context) {

		List<SQSBatchResponse.BatchItemFailure> batchItemFailures = new ArrayList<>();

		if (inputEvent == null) {
			logger.error("Lambda was executed with a null request");
			return null;
		}

		MDC.put(Constants.AWS_REQUEST_ID, context.getAwsRequestId());
		logger.debug("INPUT: {} ", inputEvent);
		try {
			List<VotingRecord> parsedVotes = MessageParserUtils.processMessage(inputEvent, batchItemFailures);
			VotingDBService.processVotingRecords(parsedVotes, batchItemFailures);
		} finally {
			MDC.clear();
		}
		return new SQSBatchResponse(batchItemFailures);
	}
}
