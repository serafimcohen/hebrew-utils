package io.github.serafimkogan.hebrewutils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;


public class Cyrillizer {
    private String source;
    private String stringToTokenize;
    private HashMap<Integer, String> tokens = new HashMap<>();
    private ArrayList<Token> tokensList = new ArrayList<>();
    private String result;
	  
    public Cyrillizer(String source) {
    	if (source == null)
    		throw new NullPointerException("Source cannot be null");  	
    	
    	this.source = source;
    	
        this.stringToTokenize = source
                .replace(Symbols.Hebrew.abbreviation_two_gereshs, 					String.valueOf(Symbols.dot))
                .replace(Symbols.Hebrew.abbreviation_two_apostrophes, 				String.valueOf(Symbols.dot))
                .replace(Symbols.Hebrew.abbreviation_quotation_mark, 				Symbols.dot)
                .replace(Symbols.Hebrew.abbreviation_gershayim, 					Symbols.dot);
       
        this.stringToTokenize = stringToTokenize
        		.replace(Symbols.Hebrew.maqaf,				 						Symbols.space)
                .replace(Symbols.hyphen, 											Symbols.space);
        
        this.stringToTokenize = stringToTokenize
                .replace(Symbols.Hebrew.vav_dagesh_vav, 							Symbols.Hebrew.vav_vav);

        this.stringToTokenize = stringToTokenize
                .replaceAll(" +", String.valueOf(Symbols.space));

        this.stringToTokenize = stringToTokenize.trim();
        
        tokenizePredefinedCombination(String.valueOf(Symbols.space), false);
        tokenizePredefinedCombination(String.valueOf(Symbols.dot), false);
        tokenizePredefinedCombination(Symbols.Hebrew.yud_yud, false);
        tokenizePredefinedCombination(Symbols.Hebrew.vav_vav, false);
        tokenizePredefinedCombination(Symbols.Hebrew.yud_vav, true);
               
        tokenizeLetters();
        tokenizeDigits();
        
        generateTokensSet();
        
        StringBuilder builder = new StringBuilder();
        for (Token token : tokensList)
            builder.append(token.getCyrillization());

        result = builder.toString()
        		.replaceAll("аиа" + Symbols.RegEx.word_boundary, "ая")
        		.replaceAll("оиа" + Symbols.RegEx.word_boundary, "оя")
        		.replaceAll("ииа" + Symbols.RegEx.word_boundary, "ия")
        		.replaceAll("уиа" + Symbols.RegEx.word_boundary, "уя")
        		.replaceAll("эиа" + Symbols.RegEx.word_boundary, "уя")
        		.replaceAll("иа"  + Symbols.RegEx.word_boundary, "ия")     		
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
    
    public String getMetaData() {
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
    
    private class Token {
    	private Token previousToken;
    	private Token nextToken;

    	private String root;
    	private String fullToken;

    	private ArrayList<Character> nikkudim = new ArrayList<>();

    	private boolean isHebrewToken;

    	private Token (String token) {
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
            
            Collections.sort(nikkudim);
        }
    	
    
        private String getVowels(boolean dageshAsVowel, boolean uppersDotsAsVowels) {
            if (nikkudim.contains(Symbols.Hebrew.hiriq)) 
            	return Symbols.Cyrillic.i;
            
        	if (nikkudim.contains(Symbols.Hebrew.tsere) || 
            		nikkudim.contains(Symbols.Hebrew.sheva) && 
            			(previousToken == null || 
            				!previousToken.isHebrewToken || 
            				previousToken.nikkudim.contains(Symbols.Hebrew.sheva) || 
            				nextToken != null && (nextToken.root.equals(root) || nextToken.root.equals(Symbols.Hebrew.yud) && nextToken.nikkudim.size() > 0))) {
            	return Symbols.Cyrillic.e;
            }
            
            if (nikkudim.contains(Symbols.Hebrew.holam)
                    || nikkudim.contains(Symbols.Hebrew.upper_dot_1) && uppersDotsAsVowels
                    || nikkudim.contains(Symbols.Hebrew.upper_dot_2) && uppersDotsAsVowels
                    || nikkudim.contains(Symbols.Hebrew.qamats_katan)
                    || nikkudim.contains(Symbols.Hebrew.hataf_qamats)
                    || nikkudim.contains(Symbols.Hebrew.qubuts)) 
            	return Symbols.Cyrillic.o;
            
            if (nikkudim.contains(Symbols.Hebrew.qamats)
                    || nikkudim.contains(Symbols.Hebrew.hataf_patah)
                    || nikkudim.contains(Symbols.Hebrew.patah)) 
            	return Symbols.Cyrillic.a;
            
            if (nikkudim.contains(Symbols.Hebrew.segol)
                    || nikkudim.contains(Symbols.Hebrew.hataf_segol)) 
            	return Symbols.Cyrillic.e;
            
            if (nikkudim.contains(Symbols.Hebrew.dagesh) && dageshAsVowel) 
            	return Symbols.Cyrillic.u;
            
            return "";
        }
        
        private String getCyrillization() {
            if (!isHebrewToken)
            	return root;
            
            if (root.equals(Symbols.Hebrew.alef) || root.equals(Symbols.Hebrew.ain)) {
                return getVowels(true, true);
            }
            
            if (root.equals(Symbols.Hebrew.vav_vav)) {
                return Symbols.Cyrillic.v + getVowels(true, true);
            }
            
            if (root.equals(Symbols.Hebrew.vav)) {
                if (previousToken == null || !previousToken.isHebrewToken) {
                    return Symbols.Cyrillic.v + getVowels(true, true);
                } else {
                	String cyrillicVowels = getVowels(true, true);
                    if (!cyrillicVowels.contains(Symbols.Cyrillic.o)
                            && !cyrillicVowels.contains(Symbols.Cyrillic.u)) {
                        return Symbols.Cyrillic.v + getVowels(true, true);
                    } else {
                    	if (cyrillicVowels.contains(Symbols.Cyrillic.u)
                    			&& !previousToken.root.equals(Symbols.Hebrew.bet)
                    			&& !previousToken.root.equals(Symbols.Hebrew.kaf)
                    			&& !previousToken.root.equals(Symbols.Hebrew.pey)
                    			&& !previousToken.root.equals(Symbols.Hebrew.tet)
                    			&& previousToken.nikkudim.contains(Symbols.Hebrew.dagesh)) {
                    		return "";
                    	} else {
                            return cyrillicVowels;
                    	}
                    }
                }
            } 
            
            if (root.equals(Symbols.Hebrew.hay)) {
                if (nextToken != null && nextToken.isHebrewToken)
                    return Symbols.Cyrillic.h + getVowels(false, true);
                
                if ((nextToken == null || !nextToken.isHebrewToken) 
                		&& nikkudim.size() == 0 
                		&& previousToken != null 
                		&& !previousToken.nikkudim.contains(Symbols.Hebrew.qamats)
                		&& !previousToken.nikkudim.contains(Symbols.Hebrew.hataf_patah)
                		&& !previousToken.nikkudim.contains(Symbols.Hebrew.patah)
                		&& !previousToken.nikkudim.contains(Symbols.Hebrew.segol)) { 
                    return Symbols.Cyrillic.a;
                } else {
                	return getVowels(false, true);
                }
            } 
            
            if (root.equals(Symbols.Hebrew.yud)) {
            	if (previousToken != null && previousToken.isHebrewToken)
            		nikkudim.removeIf(ch -> (ch == Symbols.Hebrew.hiriq));
				
            	if ((nextToken == null || !nextToken.isHebrewToken) && previousToken != null && !previousToken.nikkudim.contains(Symbols.Hebrew.hiriq)) {
            		return Symbols.Cyrillic.i;
            	}
            	
            	if (previousToken == null || !previousToken.isHebrewToken 
            			|| previousToken.root.equals(Symbols.Hebrew.yud) && !previousToken.nikkudim.contains(Symbols.Hebrew.hiriq)
	            			|| (previousToken.nikkudim.contains(Symbols.Hebrew.holam)
	              		    || previousToken.nikkudim.contains(Symbols.Hebrew.upper_dot_1) && previousToken.root.contains(Symbols.Hebrew.vav)
	                  		|| previousToken.nikkudim.contains(Symbols.Hebrew.upper_dot_2) && previousToken.root.contains(Symbols.Hebrew.vav)
	                  		|| previousToken.nikkudim.contains(Symbols.Hebrew.dagesh) && previousToken.root.contains(Symbols.Hebrew.vav)
	                  		|| previousToken.nikkudim.contains(Symbols.Hebrew.qamats)
	                  		|| previousToken.nikkudim.contains(Symbols.Hebrew.segol)
	                  		|| previousToken.nikkudim.contains(Symbols.Hebrew.qamats_katan)
	                  		|| previousToken.nikkudim.contains(Symbols.Hebrew.sheva)
	                  		|| previousToken.nikkudim.contains(Symbols.Hebrew.hataf_segol)
	                  		|| previousToken.nikkudim.contains(Symbols.Hebrew.hataf_patah)
	                  		|| previousToken.nikkudim.contains(Symbols.Hebrew.hataf_qamats)
	                  		|| previousToken.nikkudim.contains(Symbols.Hebrew.tsere)
	                  		|| previousToken.nikkudim.contains(Symbols.Hebrew.patah)
	                  		|| previousToken.nikkudim.contains(Symbols.Hebrew.qubuts)) &&
	            			((nikkudim.size() > 0) || nextToken != null && (nextToken.root.equals(Symbols.Hebrew.alef) || nextToken.root.equals(Symbols.Hebrew.ain) || nextToken.root.equals(Symbols.Hebrew.vav) && nextToken.nikkudim.size() == 0))) {
                	return Symbols.Cyrillic.i_short + getVowels(false, true);
            	}
            	
            	if (previousToken != null && !previousToken.nikkudim.contains(Symbols.Hebrew.hiriq))
            		return Symbols.Cyrillic.i + getVowels(false, true);
            	
            	return getVowels(false, true);
            } 
            
            if (root.equals(Symbols.Hebrew.yud_yud)) {
            	if (nextToken == null || !nextToken.isHebrewToken) {
            		return Symbols.Cyrillic.i;
            	}
            	
            	if (previousToken == null || !previousToken.isHebrewToken 
            			|| previousToken.nikkudim.contains(Symbols.Hebrew.holam)
              		    || previousToken.nikkudim.contains(Symbols.Hebrew.upper_dot_1) && previousToken.root.contains(Symbols.Hebrew.vav)
                  		|| previousToken.nikkudim.contains(Symbols.Hebrew.upper_dot_2) && previousToken.root.contains(Symbols.Hebrew.vav)
                  		|| previousToken.nikkudim.contains(Symbols.Hebrew.dagesh) && previousToken.root.contains(Symbols.Hebrew.vav)
                  		|| previousToken.nikkudim.contains(Symbols.Hebrew.qamats)
                  		|| previousToken.nikkudim.contains(Symbols.Hebrew.segol)
                  		|| previousToken.nikkudim.contains(Symbols.Hebrew.qamats_katan)
                  		|| previousToken.nikkudim.contains(Symbols.Hebrew.sheva)
                  		|| previousToken.nikkudim.contains(Symbols.Hebrew.hataf_segol)
                  		|| previousToken.nikkudim.contains(Symbols.Hebrew.hataf_patah)
                  		|| previousToken.nikkudim.contains(Symbols.Hebrew.hataf_qamats)
                  		|| previousToken.nikkudim.contains(Symbols.Hebrew.tsere)
                  		|| previousToken.nikkudim.contains(Symbols.Hebrew.patah)
                  		|| previousToken.nikkudim.contains(Symbols.Hebrew.qubuts)
                  		|| previousToken.nikkudim.contains(Symbols.Hebrew.hiriq)) {
                    		return Symbols.Cyrillic.i_short + getVowels(false, true);
            	} else {
            		return Symbols.Cyrillic.a + Symbols.Cyrillic.i;
            	}
            }
          
            if (root.equals(Symbols.Hebrew.yud_vav)) {
                if (previousToken != null && previousToken.getVowels(false, false).contains(Symbols.Cyrillic.a)) {
                    return Symbols.Cyrillic.v + getVowels(true, true);
            	} else {
                    return Symbols.Cyrillic.a + Symbols.Cyrillic.v + getVowels(true, true);
            	}
            }
            
            if (root.equals(Symbols.Hebrew.bet)) {
                if (nikkudim.contains(Symbols.Hebrew.dagesh)) {
                    return Symbols.Cyrillic.b + getVowels(false, true);
                } else {
                    return Symbols.Cyrillic.v + getVowels(false, true);
                }
            }
            if (root.equals(Symbols.Hebrew.gimel)) {
                if (nikkudim.contains(Symbols.Hebrew.geresh) || nikkudim.contains(Symbols.apostrophe)) {
                    return Symbols.Cyrillic.d + Symbols.Cyrillic.zh + getVowels(true, true);
                } else {
                    return Symbols.Cyrillic.g + getVowels(true, true);
                }
            }
            if (root.equals(Symbols.Hebrew.dalet)) {
                return Symbols.Cyrillic.d + getVowels(true, true);
            }
            if (root.equals(Symbols.Hebrew.zayin)) {
                if (nikkudim.contains(Symbols.Hebrew.geresh) || nikkudim.contains(Symbols.apostrophe)) {
                    return Symbols.Cyrillic.zh + getVowels(true, true);
                } else {
                    return Symbols.Cyrillic.z + getVowels(true, true);
                }
            }
            if (root.equals(Symbols.Hebrew.khet)) {
            	if (nextToken == null || !nextToken.isHebrewToken) {
            		return getVowels(true, true) + Symbols.Cyrillic.kh;
            	} else {
            		return Symbols.Cyrillic.kh + getVowels(true, true);
            	}
            }
            if (root.equals(Symbols.Hebrew.tet)) {
        		return Symbols.Cyrillic.t + getVowels(true, true);
            }
            if (root.equals(Symbols.Hebrew.kaf) || root.equals(Symbols.Hebrew.kaf_sofit)) {
                if (nikkudim.contains(Symbols.Hebrew.dagesh)) {
            		return Symbols.Cyrillic.k + getVowels(false, true);
                } else {
            		return Symbols.Cyrillic.kh + getVowels(false, true);
                }
            }
            if (root.equals(Symbols.Hebrew.lamed)) {
            	String vowels = getVowels(true, true);
            	if (vowels.equals("") && (nextToken == null || !nextToken.isHebrewToken))
            		return Symbols.Cyrillic.l + Symbols.Cyrillic.soft;
            	else 
            		return Symbols.Cyrillic.l + vowels;
            }
            if (root.equals(Symbols.Hebrew.mem) || root.equals(Symbols.Hebrew.mem_sofit)) {
        		return Symbols.Cyrillic.m + getVowels(true, true);
            }
            if (root.equals(Symbols.Hebrew.nun) || root.equals(Symbols.Hebrew.nun_sofit)) {
        		return Symbols.Cyrillic.n + getVowels(true, true);
            }
            if (root.equals(Symbols.Hebrew.sameh)) {
        		return Symbols.Cyrillic.s + getVowels(true, true);
            }
            if (root.equals(Symbols.Hebrew.pey) || root.equals(Symbols.Hebrew.pey_sofit)) {
                if (nikkudim.contains(Symbols.Hebrew.dagesh)) {
            		return Symbols.Cyrillic.p + getVowels(false, true);
                } else {
            		return Symbols.Cyrillic.f + getVowels(false, true);
                }
            }
            if (root.equals(Symbols.Hebrew.tsadi) || root.equals(Symbols.Hebrew.tsadi_sofit)) {
                if (nikkudim.contains(Symbols.Hebrew.geresh) || nikkudim.contains(Symbols.apostrophe)) {
            		return Symbols.Cyrillic.ch + getVowels(true, true);
                } else {
            		return Symbols.Cyrillic.ts + getVowels(true, true);
                }
            }
            if (root.equals(Symbols.Hebrew.kuf)) {
        		return Symbols.Cyrillic.k + getVowels(true, true);
            }
            if (root.equals(Symbols.Hebrew.resh)) {
        		return Symbols.Cyrillic.r + getVowels(true, true);
            }
            if (root.equals(Symbols.Hebrew.shin)) {
                if (nikkudim.contains(Symbols.Hebrew.sin_dot)
                        || nikkudim.contains(Symbols.Hebrew.upper_dot_1)
                        || nikkudim.contains(Symbols.Hebrew.upper_dot_2)) {
            		return Symbols.Cyrillic.s + getVowels(true, false);
                } else {
            		return Symbols.Cyrillic.sh + getVowels(true, false);
                }
            }
            if (root.equals(Symbols.Hebrew.tav)) {
        		return Symbols.Cyrillic.t + getVowels(false, true);
            }
            
            return "";
        }
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
                            || stringToTokenize.charAt(i) == Symbols.Hebrew.geresh
                            || stringToTokenize.charAt(i) == Symbols.apostrophe) {
                		builder.append(stringToTokenize.charAt(i));
                	} else {
                		if (inTheEnd && stringToTokenize.charAt(i) != Symbols.space && i != stringToTokenize.length() - 1)
                			continue out;
                		else
                			break;
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
                                || stringToTokenize.charAt(j) == Symbols.Hebrew.geresh
                                || stringToTokenize.charAt(j) == Symbols.apostrophe)) {
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

    private void generateTokensSet() {

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
}
