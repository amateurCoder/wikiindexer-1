package edu.buffalo.cse.ir.wikiindexer.tokenizer.rules;

import edu.buffalo.cse.ir.wikiindexer.tokenizer.TokenStream;
import edu.buffalo.cse.ir.wikiindexer.tokenizer.TokenizerException;
import edu.buffalo.cse.ir.wikiindexer.tokenizer.rules.TokenizerRule.RULENAMES;

@RuleClass(className = RULENAMES.DATES)
public class DatesRule implements TokenizerRule {

	String[] months = { "january", "february", "march", "april", "may", "june",
			"july", "august", "september", "october", "november", "december" };
	String year, month, day, hour, minute, second;
	int dayIndex, yearIndex, monthIndex, minIndex, maxIndex, timeIndex;

	@Override
	public void apply(TokenStream stream) throws TokenizerException {
		if (stream != null) {
			String token = "", finalToken = "";
			boolean commaDay = false, commaYear = false, fullStop = false;
			// String [] year; String [] month; String [] day; = "", hour = "",
			hour = "";
			minute = "";
			second = "";
			String concatDateString = "", concatTimeString = "";
			String[] tempArr;
			int i, j, nTokens = 0;
			stream.previous();
			// Accumulating all the tokens in the stream
			while (stream.hasNext()) {
				nTokens++;
				token = stream.next();
				if (token != null) {
					if (stream.hasNext()) {
						finalToken += token + " ";
					} else {
						finalToken += token;
					}
				}
			}

			if (finalToken != null) {
				tempArr = finalToken.split(" ");
				for (i = 0; i < tempArr.length; i++) {
					if (tempArr[i].matches("\\d+,?")) {
						System.out.println("#" + tempArr[i]);
						// Day with one or two digits
						if (tempArr[i].matches("\\d{1,2},?")) {
//							String temp = tempArr[i].replace(",", "");
//							if (!temp.equals(tempArr[i])) {
//								commaDay = true;
//							}
//							tempArr[i] = temp;
							day = tempArr[i];
							System.out.println("Day:" + day);
							dayIndex = i;
						}
						// Year
						if (tempArr[i].matches("\\d{4},?")) {
							String temp = tempArr[i].replace(",", "");
						/*	if (!temp.equals(tempArr[i])) {
								commaYear = true;
							}*/
							tempArr[i] = temp;
							year = tempArr[i];
							System.out.println("Year:" + year);
							yearIndex = i;
						}
					}
					if (tempArr[i].matches("(AD|BC)")) {
						if (i > 0) {
							if (tempArr[i - 1] == day) {
								day = "";
								// Calculating year
								year = tempArr[i - 1];
								for (j = 0; j < (4 - tempArr[i - 1].length()); j++) {
									year = "0" + year;
								}
								if (tempArr[i].matches("BC")) {
									year = "-" + year;
								}
								tempArr[i] = "";
							}
						}
						System.out.println("@!@!@!Year:" + year);
						yearIndex = i - 1;
					}
					// Matching time of format HH:MM:SS
					/*if (tempArr[i].matches("\\d+:*\\d*:*\\d*")) {
						System.out.println("Timeeeeeee:" + tempArr[i]);
						setTimeParameters(tempArr[i]);
						timeIndex = i;
					}
					// Matching itme of format HH:MM:SSAM
					if (tempArr[i].matches("\\d+:*\\d*:*\\d*(AM|PM)\\.?")) {
						System.out.println("Timeeeeeee1:" + tempArr[i]);
						String[] timeArray = tempArr[i].split(":");
						if (timeArray.length == 1) {
							String[] tempArray = timeArray[0]
									.split("(?<=[\\w&&\\D])(?=\\d)");
							hour = getTimeData(tempArray[0], tempArr[1]);
						}
						if (timeArray.length == 2) {
							hour = timeArray[0];
							String[] tempArray = timeArray[1]
									.split("(?<=[\\w&&\\D])(?=\\d)");
							minute = getTimeData(tempArray[0], tempArr[1]);
						}
						if (timeArray.length == 3) {
							hour = timeArray[0];
							minute = timeArray[1];
							String[] tempArray = timeArray[2]
									.split("(?<=[\\w&&\\D])(?=\\d)");
							second = getTimeData(tempArray[0], tempArr[1]);
						}
						String temp = tempArr[i].replace("\\.", "");
						if (temp != tempArr[i]) {
							fullStop = true;
						}
						timeIndex = i;
					}
					if (tempArr[i].matches("(AM|PM)\\.?")) {
						System.out.println("Timeeeeeee2:" + tempArr[i]);
						hour = getTimeData(hour, tempArr[i]);
						tempArr[i] = "";
						String temp = tempArr[i].replace("\\.", "");
						if (temp != tempArr[i]) {
							fullStop = true;
						}
					}*/

					// Setting value of month as the Index of the month name
					// found from "months" list.
					for (j = 0; j < months.length; j++) {
						if (tempArr[i].toLowerCase().equals(months[j])) {
							month = String.valueOf(j + 1);
							monthIndex = i;
						}
					}
					System.out.println("Month:" + month);
				}
				if (year.equals("")) {
					year = "1900";
					yearIndex = 100;
				}
				if (month.equals("")) {
					month = "01";
					monthIndex = 100;
				}
				if (day.equals("")) {
					day = "01";
					dayIndex = 100;
				}

				/*if (minute == "") {
					minute = "00";
				}

				if (second == "") {
					second = "00";
				}
*/
				System.out.println("@@@Day" + day);
				System.out.println("@@@Month:" + month);
				System.out.println("@@@Year" + year);

				concatDateString = getConcatenatedDate(year, month, day);
//				concatTimeString = getConcatenatedTime(hour, minute, second);

				System.out.println("!!!Day Index:" + dayIndex);
				System.out.println("!!!Month Index:" + monthIndex);
				System.out.println("!!!Year Index:" + yearIndex);

				minIndex = getMinIndex(dayIndex, monthIndex, yearIndex);
				maxIndex = getMaxIndex(dayIndex, monthIndex, yearIndex);

				System.out.println("!!!Min Index:" + minIndex);
				System.out.println("!!!Max Index:" + maxIndex);

				for (i = 0; i < tempArr.length; i++) {
					System.out.println("$$" + tempArr[i] + "$$");
				}

				// TODO:Check minIndex and maxIndex values while creating final
				// array
				finalToken = "";
				// if number of tokens = 1
				System.out.println("Temp Length***" + tempArr.length);
				for (i = 0; i < tempArr.length; i++) {
					if (tempArr[i] != "") {
						System.out.println("Val of I::::"+i);
						if (i == minIndex) {
							tempArr[i] = concatDateString;
							// Appending a comma if already exists
							/*if (commaDay) {
								tempArr[i] += ",";
								commaDay= false;
							}*/
							
							
							/*int flag=0;
							if (commaDay || commaYear) {
								flag=1;
								commaDay=false;
								commaYear=false;
							}
							if(flag==1){
								finalToken = finalToken+ tempArr[i] + ", ";
								flag=0;
							}else{*/
								finalToken += tempArr[i] + " ";
//							}
							
						/*	if(flag==1){
								
								flag=0;
							}
							finalToken = finalToken + " ";*/
							System.out.println("&!" + finalToken + "&!");
							// Adding the offset to the current index
							i = i + (maxIndex - minIndex);
						}/*else if(i==timeIndex){
							tempArr[i]=concatTimeString;
							// Appending a dot if already exists
							if (fullStop) {
								tempArr[i] += ".";
								fullStop= false;
							}
							finalToken += tempArr[i] + " ";
						}*/
						else if (i == tempArr.length - 1) {
							finalToken += tempArr[i];
						} else {
							finalToken += tempArr[i] + " ";
							System.out.println("&&" + finalToken + "&&");
						}
					}
				}
				System.out.println("%%Final:" + finalToken + "%%");
				stream.previous();
				stream.set(finalToken.trim());
				stream.next();
			}
			stream.reset();
		}
		
	}

	/*private String getConcatenatedTime(String hour, String minute, String second) {
		String str = hour + ":" + minute + ":" + second;
		return str;
	}

	private void setTimeParameters(String string) {
		String[] timeArray = string.split(":");
		hour = timeArray[0];
		if (timeArray.length == 2) {
			minute = timeArray[1];
		}
		if (timeArray.length == 3) {
			second = timeArray[2];
		}
	}

	private String getTimeData(String numericalValue, String amPM) {
		if (amPM == "PM") {
			int hour = Integer.parseInt(numericalValue);
			return (String.valueOf(hour + 12));
		}
		return numericalValue;
	}
*/
	private String getConcatenatedDate(String year, String month, String day) {
		if (month.length() != 2) {
			System.out.println("Month before:" + month);
			month = "0" + month;
			System.out.println("Month after:" + month);
		}
		if(day.length()==2){
			if(day.substring(1).equals(",")){
				day = "0" + day;
			}
		}
		if (day.length() < 2){// || c==',') {
			day = "0" + day;
		}
		return (year + month + day);
	}

	private int getMaxIndex(int dayIndex, int monthIndex, int yearIndex) {
		int maxIndex = dayIndex;
		if (yearIndex == 100 || monthIndex == 100 || dayIndex == 100) {
			if (yearIndex == 100) {
				if (monthIndex == 100) {
					if (dayIndex == 100) {
						maxIndex = 100;
					} else {
						maxIndex = dayIndex;
					}
				} else {
					if (dayIndex == 100) {
						maxIndex = monthIndex;
					} else {
						maxIndex = dayIndex;
					}
				}
			} else {
				if (monthIndex == 100) {
					if (dayIndex == 100) {
						maxIndex = yearIndex;
					} else {
						maxIndex = dayIndex;
					}
				} else {
					if (dayIndex == 100) {
						maxIndex = monthIndex;
					}
				}
			}

		} else {
			if (maxIndex <= monthIndex) {
				maxIndex = monthIndex;
			}
			if (maxIndex <= yearIndex) {
				maxIndex = yearIndex;
			}
		}

		return maxIndex;
	}

	private int getMinIndex(int dayIndex, int monthIndex, int yearIndex) {
		int minIndex = dayIndex;
		// If any of three is empty
		if (yearIndex == 100 || monthIndex == 100 || dayIndex == 100) {
			if (yearIndex == 100) {
				if (monthIndex == 100) {
					if (dayIndex == 100) {
						minIndex = 100;
					} else {
						minIndex = dayIndex;
					}
				} else {
					if (dayIndex == 100) {
						minIndex = monthIndex;
					} else {
						minIndex = monthIndex;
					}
				}
			} else {
				if (monthIndex == 100) {
					if (dayIndex == 100) {
						minIndex = yearIndex;
					} else {
						minIndex = yearIndex;
					}
				} else {
					if (dayIndex == 100) {
						minIndex = yearIndex;
					}
				}
			}

		} else {
			if (minIndex >= monthIndex) {
				minIndex = monthIndex;
			}
			if (minIndex >= yearIndex) {
				minIndex = yearIndex;
			}
		}

		return minIndex;

	}
}
