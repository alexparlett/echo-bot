/*
 * Copyright 2002-2015 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.homonoia.echo.core.bot.event.parser;

import org.springframework.expression.EvaluationException;
import org.springframework.expression.spel.ExpressionState;
import org.springframework.expression.spel.SpelEvaluationException;
import org.springframework.expression.spel.SpelMessage;
import org.springframework.expression.spel.ast.Operator;
import org.springframework.expression.spel.ast.SpelNodeImpl;
import org.springframework.expression.spel.support.BooleanTypedValue;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

/**
 * Implements the matches operator. Matches takes two operands:
 * The first is a String and the second is a Java regex.
 * It will return {@code true} when {@link #getValue} is called
 * if the first operand matches the regex.
 *
 * @author Andy Clement
 * @author Juergen Hoeller
 * @since 3.0
 */
public class OperatorContains extends Operator {

	private final ConcurrentMap<String, Pattern> patternCache = new ConcurrentHashMap<String, Pattern>();

	public OperatorContains(int pos, SpelNodeImpl... operands) {
		super("contains", pos, operands);
	}

	/**
	 * Check the first operand matches the regex specified as the second operand.
	 * @param state the expression state
	 * @return {@code true} if the first operand matches the regex specified as the
	 * second operand, otherwise {@code false}
	 * @throws EvaluationException if there is a problem evaluating the expression
	 * (e.g. the regex is invalid)
	 */
	@Override
	public BooleanTypedValue getValueInternal(ExpressionState state) throws EvaluationException {
		SpelNodeImpl leftOp = getLeftOperand();
		SpelNodeImpl rightOp = getRightOperand();
		Object left = leftOp.getValue(state);
		Object right = rightOp.getValueInternal(state).getValue();

		if (!(left instanceof String)) {
			throw new SpelEvaluationException(leftOp.getStartPosition(),
					SpelMessage.INVALID_FIRST_OPERAND_FOR_MATCHES_OPERATOR, left);
		}
		if (!(right instanceof String)) {
			throw new SpelEvaluationException(rightOp.getStartPosition(),
					SpelMessage.INVALID_SECOND_OPERAND_FOR_MATCHES_OPERATOR, right);
		}

		try {
			String leftString = (String) left;
			String rightString = (String) right;
			Pattern pattern = this.patternCache.get(rightString);
			if (pattern == null) {
				pattern = Pattern.compile(rightString);
				this.patternCache.putIfAbsent(rightString, pattern);
			}
			Matcher matcher = pattern.matcher(leftString);
			return BooleanTypedValue.forValue(matcher.find());
		}
		catch (PatternSyntaxException ex) {
			throw new SpelEvaluationException(rightOp.getStartPosition(), ex, SpelMessage.INVALID_PATTERN, right);
		}
	}

}
