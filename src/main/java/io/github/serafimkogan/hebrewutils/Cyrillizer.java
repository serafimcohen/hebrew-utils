package io.github.serafimkogan.hebrewutils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Cyrillizer {
    private final String source;
    private String stringToTokenize;
    private Map<Integer, String> tokens = new HashMap<>();
    private List<Token> tokensList = new ArrayList<>();
    private String result;

    public Cyrillizer(String source) {
    	if (source == null)
    		throw new NullPointerException("Source cannot be null");

    	this.source = source;

        this.stringToTokenize = source
                .replace(Symbols.Hebrew.ABBREVIATION_TWO_GERESHS, Symbols.Hebrew.DOT)
                .replace(Symbols.Hebrew.ABBREVIATION_TWO_APOSTROPHES, Symbols.Hebrew.DOT)
                .replace(Symbols.Hebrew.ABBREVIATION_QUOTATION_MARK, Symbols.Hebrew.DOT)
                .replace(Symbols.Hebrew.ABBREVIATION_GERSHAYIM, Symbols.Hebrew.DOT);

        this.stringToTokenize = stringToTokenize
        		.replace(Symbols.Hebrew.MAQAF, Symbols.SPACE)
                .replace(Symbols.HYPHEN, Symbols.SPACE);

        this.stringToTokenize = stringToTokenize
                .replace(Symbols.Hebrew.VAV_DAGESH_VAV, Symbols.Hebrew.VAV_VAV);

        this.stringToTokenize = stringToTokenize
                .replaceAll(Symbols.RegEx.ONE_OR_MORE_SPACES, String.valueOf(Symbols.SPACE));

        this.stringToTokenize = stringToTokenize.trim();

        tokenizePredefinedCombination(String.valueOf(Symbols.SPACE), false);
        tokenizePredefinedCombination(String.valueOf(Symbols.DOT), false);
        tokenizePredefinedCombination(Symbols.Hebrew.YUD_YUD, false);
        tokenizePredefinedCombination(Symbols.Hebrew.VAV_VAV, false);
        tokenizePredefinedCombination(Symbols.Hebrew.YUD_VAV, true);

        tokenizeLetters();
        tokenizeDigits();

        generateTokensList();

        StringBuilder builder = new StringBuilder();
        for (Token token : tokensList)
            builder.append(token.getCyrillization());

        result = builder.toString()
        		.replaceAll("аиа" + Symbols.RegEx.WORD_BOUNDARY, "ая")
        		.replaceAll("оиа" + Symbols.RegEx.WORD_BOUNDARY, "оя")
        		.replaceAll("ииа" + Symbols.RegEx.WORD_BOUNDARY, "ия")
        		.replaceAll("уиа" + Symbols.RegEx.WORD_BOUNDARY, "уя")
        		.replaceAll("эиа" + Symbols.RegEx.WORD_BOUNDARY, "уя")
        		.replaceAll("иа"  + Symbols.RegEx.WORD_BOUNDARY, "ия")
	    		.replaceAll("айа", "ая")
	    		.replaceAll("ойа", "оя")
	    		.replaceAll("ийа", "ия")
	    		.replaceAll("эйа", "эя")
	    		.replaceAll("уйа", "уя")
	        	.replaceAll("аио", "аё")
	        	.replaceAll("оио", "оё")
	        	.replaceAll("иио", "иё")
	        	.replaceAll("уио", "уё")
	        	.replaceAll("эио", "уё")
	        	.replaceAll("ёио", "ёё")
        		.replaceAll("йо", "ё")
	        	.replaceAll("уо", "ё")
	        	.replaceAll("ло", "лё")
	        	.replaceAll("лу", "лю")
	        	.replaceAll("ла", "ля")
	        	.replaceAll("лэ", "ле");
    }
    
    public String getResult() {
        return result;
    }
    
    public String getSource() {
        return source;
    }
    
    public String toString() {
        StringBuilder builder = new StringBuilder();

        builder.append("source: ");
        builder.append(source);
        builder.append(" | tokens: ");

        for (Token token : tokensList) {
            builder.append("[");
            builder.append("(");
            builder.append(token.root);
            builder.append(")");
            for (char nikkud : token.nikkudim) {
                builder.append("<");
                builder.append(nikkud);
                builder.append(">");
            }
            builder.append("]");
        }

        builder.append(" | result: ");
        builder.append(result);

        return builder.toString();
    }

    private void tokenizePredefinedCombination(String combination, boolean inTheEnd) {
    	int beginIndex = 0;
        out:
        for (; beginIndex < stringToTokenize.length() - combination.length() + 1; beginIndex++) {
        	if (stringToTokenize.substring(beginIndex, beginIndex + combination.length()).equals(combination)) {
                StringBuilder builder = new StringBuilder();
                builder.append(combination);
                for (int i = beginIndex + combination.length(); i < stringToTokenize.length(); i++) {
                	if (Symbols.Hebrew.isHebrewDiacritics(stringToTokenize.charAt(i))
                            || stringToTokenize.charAt(i) == Symbols.Hebrew.GERESH
                            || stringToTokenize.charAt(i) == Symbols.APOSTROPHE) {
                		builder.append(stringToTokenize.charAt(i));
                	} else {
                		if (inTheEnd && stringToTokenize.charAt(i) != Symbols.SPACE && i != stringToTokenize.length() - 1) {
                			continue out;
                		} else {
                			break;
                		}
                	}
                }

                String fullToken = builder.toString();
        		tokens.put(beginIndex, fullToken);

                StringBuilder spaceBuilder = new StringBuilder();
                for (int i = 0; i < fullToken.length(); i++)
                    spaceBuilder.append(" ");
                stringToTokenize = stringToTokenize.substring(0, beginIndex)
                		+ spaceBuilder.toString()
                		+ stringToTokenize.substring(beginIndex + spaceBuilder.length());

                beginIndex += combination.length();
        	}
        }
    }


    private void tokenizeLetters() {
        for (int i = 0; i < stringToTokenize.length(); i++) {
            if (Symbols.Hebrew.isHebrewLetter(stringToTokenize.charAt(i))) {

                StringBuilder builder = new StringBuilder();
                builder.append(stringToTokenize.charAt(i));

                if (i == stringToTokenize.length() - 1) {
                    tokens.put(i, builder.toString());
                } else {
                    for (int j = i + 1; j <= stringToTokenize.length(); j++) {
                        if (j != stringToTokenize.length()
                                && (Symbols.Hebrew.isHebrewDiacritics(stringToTokenize.charAt(j))
                                || stringToTokenize.charAt(j) == Symbols.Hebrew.GERESH
                                || stringToTokenize.charAt(j) == Symbols.APOSTROPHE)) {
                            builder.append(stringToTokenize.charAt(j));
                        } else {
                            tokens.put(i, builder.toString());
                            i = j - 1;
                            break;
                        }
                    }
                }
            }
        }
    }

    private void tokenizeDigits() {
        for (int i = 0; i < stringToTokenize.length(); i++) {
            if (Character.isDigit(stringToTokenize.charAt(i))) {

                StringBuilder builder = new StringBuilder();
                builder.append(stringToTokenize.charAt(i));

                if (i == stringToTokenize.length() - 1) {
                    tokens.put(i, builder.toString());
                } else {
                    for (int j = i + 1; j <= stringToTokenize.length(); j++) {
                        if (j == stringToTokenize.length() || !Character.isDigit(stringToTokenize.charAt(j))) {
                            tokens.put(i, builder.toString());
                            i = j - 1;
                            break;
                        } else {
                            builder.append(stringToTokenize.charAt(j));
                        }
                    }
                }
            }
        }
    }

    private void generateTokensList() {
        for (int i = 0; i < stringToTokenize.length(); i++) {
            if (tokens.get(i) != null) {
                tokensList.add(new Token(tokens.get(i)));
            }
        }

        for (int i = 0; i < tokensList.size(); i++) {
            Token previousToken;
            if (i == 0) {
                previousToken = null;
            } else {
                previousToken = tokensList.get(i - 1);
            }
            Token nextToken;
            if (i == tokensList.size() - 1) {
                nextToken = null;
            } else {
                nextToken = tokensList.get(i + 1);
            }
            tokensList.get(i).previousToken = previousToken;
            tokensList.get(i).nextToken = nextToken;
        }
    }
    
    private class Token {
    	private Token previousToken;
    	private Token nextToken;

    	private String root;
    	private String fullToken;

    	private Set<Character> nikkudim = new HashSet<>();

    	private boolean isHebrewToken;

    	private Token(String token) {
            fullToken = token;

            StringBuilder rootBuilder = new StringBuilder();
            char firstChar = token.charAt(0);
            if (Symbols.Hebrew.isHebrewLetter(firstChar)) {
                isHebrewToken = true;
                for (char ch : token.toCharArray()) {
                    if (Symbols.Hebrew.isHebrewLetter(ch)) {
                        rootBuilder.append(ch);
                    } else {
                        nikkudim.add(ch);
                    }
                }
                root = rootBuilder.toString();
            } else {
                isHebrewToken = false;
                root = fullToken;
            }
        }

        private String getVowels(boolean dageshAsVowel, boolean uppersDotsAsVowels) {
            if (nikkudim.contains(Symbols.Hebrew.HIRIQ)) {
            	return Symbols.Cyrillic.I;
            }

        	if (nikkudim.contains(Symbols.Hebrew.TSERE) ||
            		nikkudim.contains(Symbols.Hebrew.SHEVA) &&
            			(previousToken == null ||
            				!previousToken.isHebrewToken ||
            				previousToken.nikkudim.contains(Symbols.Hebrew.SHEVA) ||
            				nextToken != null && (nextToken.root.equals(root) || nextToken.root.equals(Symbols.Hebrew.YUD)
            						&& nextToken.nikkudim.size() > 0))) {
            	return Symbols.Cyrillic.E;
            }

            if (nikkudim.contains(Symbols.Hebrew.HOLAM)
                    || nikkudim.contains(Symbols.Hebrew.UPPER_DOT_1) && uppersDotsAsVowels
                    || nikkudim.contains(Symbols.Hebrew.UPPER_DOT_2) && uppersDotsAsVowels
                    || nikkudim.contains(Symbols.Hebrew.QAMATS_KATAN)
                    || nikkudim.contains(Symbols.Hebrew.HATAF_QAMATS)
                    || nikkudim.contains(Symbols.Hebrew.QUBUTS)) {
            	return Symbols.Cyrillic.O;
            }

            if (nikkudim.contains(Symbols.Hebrew.QAMATS)
                    || nikkudim.contains(Symbols.Hebrew.HATAF_PATAH)
                    || nikkudim.contains(Symbols.Hebrew.PATAH)) {
            	return Symbols.Cyrillic.A;
            }

            if (nikkudim.contains(Symbols.Hebrew.SEGOL)
                    || nikkudim.contains(Symbols.Hebrew.HATAF_SEGOL)) {
            	return Symbols.Cyrillic.E;
            }

            if (nikkudim.contains(Symbols.Hebrew.DAGESH) && dageshAsVowel) {
            	return Symbols.Cyrillic.U;
            }

            return "";
        }

        private String getCyrillization() {
            if (!isHebrewToken) {
            	return root;
            }

            if (root.equals(Symbols.Hebrew.ALEF) || root.equals(Symbols.Hebrew.AIN)) {
                return getVowels(true, true);
            }

            if (root.equals(Symbols.Hebrew.VAV_VAV)) {
                return Symbols.Cyrillic.V + getVowels(true, true);
            }

            if (root.equals(Symbols.Hebrew.VAV)) {
                if (previousToken == null || !previousToken.isHebrewToken) {
                    return Symbols.Cyrillic.V + getVowels(true, true);
                } else {
                	String cyrillicVowels = getVowels(true, true);
                    if (!cyrillicVowels.contains(Symbols.Cyrillic.O)
                            && !cyrillicVowels.contains(Symbols.Cyrillic.U)) {
                        return Symbols.Cyrillic.V + getVowels(true, true);
                    } else {
                    	if (cyrillicVowels.contains(Symbols.Cyrillic.U)
                    			&& !previousToken.root.equals(Symbols.Hebrew.BET)
                    			&& !previousToken.root.equals(Symbols.Hebrew.KAF)
                    			&& !previousToken.root.equals(Symbols.Hebrew.PEY)
                    			&& !previousToken.root.equals(Symbols.Hebrew.TET)
                    			&& previousToken.nikkudim.contains(Symbols.Hebrew.DAGESH)) {
                    		return "";
                    	} else {
                            return cyrillicVowels;
                    	}
                    }
                }
            }

            if (root.equals(Symbols.Hebrew.HAY)) {
                if (nextToken != null && nextToken.isHebrewToken)
                    return Symbols.Cyrillic.H + getVowels(false, true);

                if ((nextToken == null || !nextToken.isHebrewToken)
                		&& nikkudim.size() == 0
                		&& previousToken != null
                		&& !previousToken.nikkudim.contains(Symbols.Hebrew.QAMATS)
                		&& !previousToken.nikkudim.contains(Symbols.Hebrew.HATAF_PATAH)
                		&& !previousToken.nikkudim.contains(Symbols.Hebrew.PATAH)
                		&& !previousToken.nikkudim.contains(Symbols.Hebrew.SEGOL)) {
                    return Symbols.Cyrillic.A;
                } else {
                	return getVowels(false, true);
                }
            }

            if (root.equals(Symbols.Hebrew.YUD)) {
            	if (previousToken != null && previousToken.isHebrewToken)
            		nikkudim.removeIf(ch -> (ch == Symbols.Hebrew.HIRIQ));

            	if ((nextToken == null || !nextToken.isHebrewToken)
            			&& previousToken != null && !previousToken.nikkudim.contains(Symbols.Hebrew.HIRIQ)) {
            		return Symbols.Cyrillic.I;
            	}

            	if (previousToken == null || !previousToken.isHebrewToken
            			|| previousToken.root.equals(Symbols.Hebrew.YUD) && !previousToken.nikkudim.contains(Symbols.Hebrew.HIRIQ)
	            			|| (previousToken.nikkudim.contains(Symbols.Hebrew.HOLAM)
	              		    || previousToken.nikkudim.contains(Symbols.Hebrew.UPPER_DOT_1) && previousToken.root.contains(Symbols.Hebrew.VAV)
	                  		|| previousToken.nikkudim.contains(Symbols.Hebrew.UPPER_DOT_2) && previousToken.root.contains(Symbols.Hebrew.VAV)
	                  		|| previousToken.nikkudim.contains(Symbols.Hebrew.DAGESH) && previousToken.root.contains(Symbols.Hebrew.VAV)
	                  		|| previousToken.nikkudim.contains(Symbols.Hebrew.QAMATS)
	                  		|| previousToken.nikkudim.contains(Symbols.Hebrew.SEGOL)
	                  		|| previousToken.nikkudim.contains(Symbols.Hebrew.QAMATS_KATAN)
	                  		|| previousToken.nikkudim.contains(Symbols.Hebrew.SHEVA)
	                  		|| previousToken.nikkudim.contains(Symbols.Hebrew.HATAF_SEGOL)
	                  		|| previousToken.nikkudim.contains(Symbols.Hebrew.HATAF_PATAH)
	                  		|| previousToken.nikkudim.contains(Symbols.Hebrew.HATAF_QAMATS)
	                  		|| previousToken.nikkudim.contains(Symbols.Hebrew.TSERE)
	                  		|| previousToken.nikkudim.contains(Symbols.Hebrew.PATAH)
	                  		|| previousToken.nikkudim.contains(Symbols.Hebrew.QUBUTS)) &&
	            			((nikkudim.size() > 0) || nextToken != null && (nextToken.root.equals(Symbols.Hebrew.ALEF)
	            					|| nextToken.root.equals(Symbols.Hebrew.AIN) || nextToken.root.equals(Symbols.Hebrew.VAV)
	            					&& nextToken.nikkudim.size() == 0))) {
                	return Symbols.Cyrillic.I_SHORT + getVowels(false, true);
            	}

            	if (previousToken != null && !previousToken.nikkudim.contains(Symbols.Hebrew.HIRIQ))
            		return Symbols.Cyrillic.I + getVowels(false, true);

            	return getVowels(false, true);
            }

            if (root.equals(Symbols.Hebrew.YUD_YUD)) {
            	if (nextToken == null || !nextToken.isHebrewToken) {
            		return Symbols.Cyrillic.I;
            	}

            	if (previousToken == null || !previousToken.isHebrewToken
            			|| previousToken.nikkudim.contains(Symbols.Hebrew.HOLAM)
              		    || previousToken.nikkudim.contains(Symbols.Hebrew.UPPER_DOT_1) && previousToken.root.contains(Symbols.Hebrew.VAV)
                  		|| previousToken.nikkudim.contains(Symbols.Hebrew.UPPER_DOT_2) && previousToken.root.contains(Symbols.Hebrew.VAV)
                  		|| previousToken.nikkudim.contains(Symbols.Hebrew.DAGESH) && previousToken.root.contains(Symbols.Hebrew.VAV)
                  		|| previousToken.nikkudim.contains(Symbols.Hebrew.QAMATS)
                  		|| previousToken.nikkudim.contains(Symbols.Hebrew.SEGOL)
                  		|| previousToken.nikkudim.contains(Symbols.Hebrew.QAMATS_KATAN)
                  		|| previousToken.nikkudim.contains(Symbols.Hebrew.SHEVA)
                  		|| previousToken.nikkudim.contains(Symbols.Hebrew.HATAF_SEGOL)
                  		|| previousToken.nikkudim.contains(Symbols.Hebrew.HATAF_PATAH)
                  		|| previousToken.nikkudim.contains(Symbols.Hebrew.HATAF_QAMATS)
                  		|| previousToken.nikkudim.contains(Symbols.Hebrew.TSERE)
                  		|| previousToken.nikkudim.contains(Symbols.Hebrew.PATAH)
                  		|| previousToken.nikkudim.contains(Symbols.Hebrew.QUBUTS)
                  		|| previousToken.nikkudim.contains(Symbols.Hebrew.HIRIQ)) {
                    		return Symbols.Cyrillic.I_SHORT + getVowels(false, true);
            	} else {
            		return Symbols.Cyrillic.A + Symbols.Cyrillic.I;
            	}
            }

            if (root.equals(Symbols.Hebrew.YUD_VAV)) {
                if (previousToken != null && previousToken.getVowels(false, false).contains(Symbols.Cyrillic.A)) {
                    return Symbols.Cyrillic.V + getVowels(true, true);
            	} else {
                    return Symbols.Cyrillic.A + Symbols.Cyrillic.V + getVowels(true, true);
            	}
            }

            if (root.equals(Symbols.Hebrew.BET)) {
                if (nikkudim.contains(Symbols.Hebrew.DAGESH)) {
                    return Symbols.Cyrillic.B + getVowels(false, true);
                } else {
                    return Symbols.Cyrillic.V + getVowels(false, true);
                }
            }
            if (root.equals(Symbols.Hebrew.GIMEL)) {
                if (nikkudim.contains(Symbols.Hebrew.GERESH) || nikkudim.contains(Symbols.APOSTROPHE)) {
                    return Symbols.Cyrillic.D + Symbols.Cyrillic.ZH + getVowels(true, true);
                } else {
                    return Symbols.Cyrillic.G + getVowels(true, true);
                }
            }
            if (root.equals(Symbols.Hebrew.DALET)) {
                return Symbols.Cyrillic.D + getVowels(true, true);
            }
            if (root.equals(Symbols.Hebrew.ZAYIN)) {
                if (nikkudim.contains(Symbols.Hebrew.GERESH) || nikkudim.contains(Symbols.APOSTROPHE)) {
                    return Symbols.Cyrillic.ZH + getVowels(true, true);
                } else {
                    return Symbols.Cyrillic.Z + getVowels(true, true);
                }
            }
            if (root.equals(Symbols.Hebrew.KHET)) {
            	if (nextToken == null || !nextToken.isHebrewToken) {
            		return getVowels(true, true) + Symbols.Cyrillic.KH;
            	} else {
            		return Symbols.Cyrillic.KH + getVowels(true, true);
            	}
            }
            if (root.equals(Symbols.Hebrew.TET)) {
        		return Symbols.Cyrillic.T + getVowels(true, true);
            }
            if (root.equals(Symbols.Hebrew.KAF) || root.equals(Symbols.Hebrew.KAF_SOFIT)) {
                if (nikkudim.contains(Symbols.Hebrew.DAGESH)) {
            		return Symbols.Cyrillic.K + getVowels(false, true);
                } else {
            		return Symbols.Cyrillic.KH + getVowels(false, true);
                }
            }
            if (root.equals(Symbols.Hebrew.LAMED)) {
            	String vowels = getVowels(true, true);
            	if (vowels.equals("") && (nextToken == null || !nextToken.isHebrewToken))
            		return Symbols.Cyrillic.L + Symbols.Cyrillic.SOFT;
            	else
            		return Symbols.Cyrillic.L + vowels;
            }
            if (root.equals(Symbols.Hebrew.MEM) || root.equals(Symbols.Hebrew.MEM_SOFIT)) {
        		return Symbols.Cyrillic.M + getVowels(true, true);
            }
            if (root.equals(Symbols.Hebrew.NUN) || root.equals(Symbols.Hebrew.NUN_SOFIT)) {
        		return Symbols.Cyrillic.N + getVowels(true, true);
            }
            if (root.equals(Symbols.Hebrew.SAMEH)) {
        		return Symbols.Cyrillic.S + getVowels(true, true);
            }
            if (root.equals(Symbols.Hebrew.PEY) || root.equals(Symbols.Hebrew.PEY_SOFIT)) {
                if (nikkudim.contains(Symbols.Hebrew.DAGESH)) {
            		return Symbols.Cyrillic.P + getVowels(false, true);
                } else {
            		return Symbols.Cyrillic.F + getVowels(false, true);
                }
            }
            if (root.equals(Symbols.Hebrew.TSADI) || root.equals(Symbols.Hebrew.TSADI_SOFIT)) {
                if (nikkudim.contains(Symbols.Hebrew.GERESH) || nikkudim.contains(Symbols.APOSTROPHE)) {
            		return Symbols.Cyrillic.CH + getVowels(true, true);
                } else {
            		return Symbols.Cyrillic.TS + getVowels(true, true);
                }
            }
            if (root.equals(Symbols.Hebrew.KUF)) {
        		return Symbols.Cyrillic.K + getVowels(true, true);
            }
            if (root.equals(Symbols.Hebrew.RESH)) {
        		return Symbols.Cyrillic.R + getVowels(true, true);
            }
            if (root.equals(Symbols.Hebrew.SHIN)) {
                if (nikkudim.contains(Symbols.Hebrew.SIN_DOT)
                        || nikkudim.contains(Symbols.Hebrew.UPPER_DOT_1)
                        || nikkudim.contains(Symbols.Hebrew.UPPER_DOT_2)) {
            		return Symbols.Cyrillic.S + getVowels(true, false);
                } else {
            		return Symbols.Cyrillic.SH + getVowels(true, false);
                }
            }
            if (root.equals(Symbols.Hebrew.TAV)) {
        		return Symbols.Cyrillic.T + getVowels(false, true);
            }

            return "";
        }
    } 
}
